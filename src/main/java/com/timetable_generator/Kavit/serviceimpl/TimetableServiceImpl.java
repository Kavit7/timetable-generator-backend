package com.timetable_generator.Kavit.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timetable_generator.Kavit.model.ClassSubject;
import com.timetable_generator.Kavit.model.ClassTable;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Subject;
import com.timetable_generator.Kavit.model.Teacher;
import com.timetable_generator.Kavit.model.TeacherSubjectClass;
import com.timetable_generator.Kavit.model.Timeslot;
import com.timetable_generator.Kavit.model.Timetable;
import com.timetable_generator.Kavit.model.TimetableGroup;
import com.timetable_generator.Kavit.repository.ClassSubjectRepository;
import com.timetable_generator.Kavit.repository.ClassTableRepository;
import com.timetable_generator.Kavit.repository.SchoolRepository;
import com.timetable_generator.Kavit.repository.SubjectRepository;
import com.timetable_generator.Kavit.repository.TeacherAvailabilityRepository;
import com.timetable_generator.Kavit.repository.TeacherRepository;
import com.timetable_generator.Kavit.repository.TeacherSubjectClassRepository;
import com.timetable_generator.Kavit.repository.TimeslotRepository;
import com.timetable_generator.Kavit.repository.TimetableGroupRepository;
import com.timetable_generator.Kavit.repository.TimetableRepository;
import com.timetable_generator.Kavit.response.TimetableResponseDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional


@RequiredArgsConstructor
public class TimetableServiceImpl {
    
    private final TimetableRepository timetableRepository;
    private final TeacherSubjectClassRepository teacherSubjectClassRepository;
    private final ClassTableRepository classTableRepository;
    private final TeacherAvailabilityRepository teacherAvailabilityRepository;
    private final TeacherRepository teacherRepository;
    private final ClassSubjectRepository classSubjectRepository;
    private final TimeslotRepository timeslotRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolRepository schoolRepository;
    private final TimetableGroupRepository timetableGroupRepository;

  public  Map <String ,Object> generateTimetable(Long schoolId){
         
        School school =schoolRepository.findById(schoolId)
        .orElseThrow(() -> new RuntimeException("School not found"));
         timetableRepository.deleteBySchool(school);
         timetableGroupRepository.deleteBySchool(school);


         List<Timeslot> allTimeslots = timeslotRepository.findBySchool(school);
                List<Timeslot> teachingSlots = allTimeslots.stream()
            .filter(t -> !Boolean.TRUE.equals(t.getIsBreak()))
            .sorted(Comparator.comparing(Timeslot::getDay)
                .thenComparing(Timeslot::getStartTime))
            .collect(Collectors.toList());


            //Teacher availability: timeslot_id → Set<teacher_id>
        Map<Long ,Set<Long>> availableTeachersPerSlot = new HashMap<>();
        teacherAvailabilityRepository.findBySchoolAndIsAvailableTrue(school)
        .forEach(ta -> availableTeachersPerSlot
            .computeIfAbsent(ta.getTimeslot().getId(), k-> new HashSet<>())
            .add(ta.getTeacher().getId())
        );


         // TeacherSubjectClass list

         List<TeacherSubjectClass> assignments = teacherSubjectClassRepository.findBySchool(school);



         Map<Long ,List<ClassSubject>> classSubjectsMap= classSubjectRepository.findBySchool(school)
         .stream().collect(Collectors.groupingBy(cs -> cs.getClassTable().getId()));

         // Classes zote
        List<ClassTable> classes = classTableRepository.findBySchool(school);

        // ── STEP 3: Tracking structures ────────────────────────────
        // teacher_id → Set<timeslot_id> (slots ambazo teacher ameshapangiwa)
        Map<Long, Set<Long>> teacherBusySlots = new HashMap<>();

        // class_id → Set<timeslot_id> (slots ambazo class imeshapangiwa)
        Map<Long, Set<Long>> classBusySlots = new HashMap<>();

        // Timetable entries zitakazohifadhiwa
        List<Timetable> entriesToSave = new ArrayList<>();

        // ── STEP 4: Kwa kila class, unda TimetableGroup ────────────
        Map<Long, TimetableGroup> classToGroup = new HashMap<>();
        for (ClassTable cls : classes) {
            TimetableGroup group = new TimetableGroup();
            group.setName(cls.getName());
            group.setSchool(school);
            TimetableGroup saved = timetableGroupRepository.save(group);
            classToGroup.put(cls.getId(), saved);
        }

        // ── STEP 5: Build assignment tasks ─────────────────────────
        // Kwa kila class → kila subject → periods_per_week assignments zinahitajika
        // Tumia structure: AssignmentTask { classId, subjectId, teacherId, periodsRemaining }

        record AssignmentTask(long classId, long subjectId, long teacherId, int periodsPerWeek) {}

        List<AssignmentTask> tasks = new ArrayList<>();

        // teacher_subject_class: (class_id, subject_id) → teacher_id
        Map<String, Long> subjectClassToTeacher = new HashMap<>();
        for (TeacherSubjectClass tsc : assignments) {
            String key = tsc.getClassTable().getId() + "_" + tsc.getSubject().getId();
            subjectClassToTeacher.put(key, tsc.getTeacher().getId());
        }

        for (ClassTable cls : classes) {
            List<ClassSubject> subjects = classSubjectsMap.getOrDefault(cls.getId(), List.of());
            for (ClassSubject cs : subjects) {
                String key = cls.getId() + "_" + cs.getSubject().getId();
                Long teacherId = subjectClassToTeacher.get(key);
                if (teacherId == null) continue; // hakuna teacher wa subject hii kwa class hii
                int periods = cs.getPeriodsPerWeek() != null ? cs.getPeriodsPerWeek() : 1;
                tasks.add(new AssignmentTask(cls.getId(), cs.getSubject().getId(), teacherId, periods));
            }
        }

        // ── STEP 6: Assign kila task kwa timeslots ─────────────────
        // Panga tasks kwa random kidogo ili usije ukajaza siku moja tu
        Collections.shuffle(tasks);

        for (AssignmentTask task : tasks) {
            int periodsLeft = task.periodsPerWeek();
            TimetableGroup group = classToGroup.get(task.classId());
            if (group == null) continue;
            
            Set<Long> teacherBusy =
                teacherBusySlots.computeIfAbsent(task.teacherId(), k -> new HashSet<>());
            Set<Long> classBusy =
                classBusySlots.computeIfAbsent(task.classId(), k -> new HashSet<>());

            // Panga slots kwa siku tofauti tofauti (spread across week)
            List<Timeslot> shuffledSlots = new ArrayList<>(teachingSlots);
            Collections.shuffle(shuffledSlots);

            for (Timeslot slot : shuffledSlots) {
                if (periodsLeft <= 0) break;

                // Skip kama slot hii imeshachukuliwa na teacher au class hii
                if (teacherBusy.contains(slot.getId())) continue;
                if (classBusy.contains(slot.getId())) continue;

                // Angalia teacher ana availability kwa slot hii
                Set<Long> availableTeachers =
                    availableTeachersPerSlot.getOrDefault(slot.getId(), Set.of());
                if (!availableTeachers.contains(task.teacherId())) continue;

                // ✅ Slot inapatikana — assign!
                teacherBusy.add(slot.getId());
                classBusy.add(slot.getId());
                Teacher teacher = teacherRepository.findTeacherById(task.teacherId());
                Subject subject = subjectRepository.findSubjectById(task.subjectId());
                Timeslot timeslot= timeslotRepository.findTimeslotById(slot.getId());
                TimetableGroup tmg = timetableGroupRepository.findTimetableGroupById(group.getId());

                Timetable entry = new Timetable();
                entry.setTimeslot(timeslot);
                entry.setTimetableGroup(tmg);
                entry.setSchool(school);
                entry.setTeacher(teacher);
                entry.setSubject(subject);
                entriesToSave.add(entry);

                periodsLeft--;
            }
        }

        // ── STEP 7: Save all entries kwa batch ─────────────────────
        timetableRepository.saveAll(entriesToSave);

        // ── STEP 8: Return summary ──────────────────────────────────
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("totalEntriesCreated", entriesToSave.size());
        result.put("totalClasses", classes.size());
        result.put("totalGroupsCreated", classToGroup.size());
        result.put("message", "Timetable generated successfully");
        return result;
    }

    // ── Fetch timetable iliyoundwa kwa display ──────────────────────
    public List<TimetableResponseDto> getTimetable(Long schoolId) {
         School school = schoolRepository.findById(schoolId)
         .orElseThrow(()-> new RuntimeException("School not found"));
        // Join manually kwa queries
        List<Timetable> entries = timetableRepository.findBySchool(school);
        List<Timeslot> timeslots = timeslotRepository.findBySchool(school);
        List<TimetableGroup> groups = timetableGroupRepository.findBySchool(school);
        List<TeacherSubjectClass> tscs = teacherSubjectClassRepository.findBySchool(school);
        List<Teacher> teachers = teacherRepository.findBySchool(school);
        List<Subject> subjects = subjectRepository.findBySchool(school);

        // Build lookup maps
        Map<Long, Timeslot> timeslotMap = timeslots.stream()
            .collect(Collectors.toMap(Timeslot::getId, t -> t));
        Map<Long, TimetableGroup> groupMap = groups.stream()
            .collect(Collectors.toMap(TimetableGroup::getId, g -> g));
        Map<Long, Teacher> teacherMap = teachers.stream()
            .collect(Collectors.toMap(Teacher::getId, t -> t));
        Map<Long, Subject> subjectMap = subjects.stream()
            .collect(Collectors.toMap(Subject::getId, s -> s));

        // group_id → class_id (from classToGroup mapping via name)
        List<ClassTable> classTables = classTableRepository.findBySchool(school);
        Map<String, Long> classNameToId = classTables.stream()
            .collect(Collectors.toMap(ClassTable::getName, ClassTable::getId));

        List<TimetableResponseDto> result = new ArrayList<>();

        for (Timetable entry : entries) {
            Timeslot slot = timeslotMap.get(entry.getTimeslot().getId());
            TimetableGroup group = groupMap.get(entry.getTimetableGroup().getId());
            if (slot == null || group == null) continue;

            // Find teacher + subject kwa class hii
            Long classId = classNameToId.get(group.getName());
            String subjectName = "—";
            String teacherName = "—";

            if (classId != null) {
             teacherName = entry.getTeacher() != null
        ? entry.getTeacher().getName()
        : "—";

              subjectName = entry.getSubject() != null
        ? entry.getSubject().getName()
        : "—";
            }

            result.add(new TimetableResponseDto(
                group.getName(),
                slot.getDay(),
                slot.getStartTime().toString(),
                slot.getEndTime().toString(),
                subjectName,
                teacherName,
                slot.getIsBreak(),
                slot.getId(),
                group.getId()
            ));
        }

        return result;



    }

}
