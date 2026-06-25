package com.timetable_generator.Kavit.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timetable_generator.Kavit.model.ClassSubject;
import com.timetable_generator.Kavit.model.ClassTable;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Subject;
import com.timetable_generator.Kavit.model.Teacher;
import com.timetable_generator.Kavit.model.TeacherAvailability;
import com.timetable_generator.Kavit.model.Timeslot;
import com.timetable_generator.Kavit.repository.ClassSubjectRepository;
import com.timetable_generator.Kavit.repository.ClassTableRepository;
import com.timetable_generator.Kavit.repository.SchoolRepository;
import com.timetable_generator.Kavit.repository.SubjectRepository;
import com.timetable_generator.Kavit.repository.TeacherAvailabilityRepository;
import com.timetable_generator.Kavit.repository.TeacherSubjectClassRepository;
import com.timetable_generator.Kavit.repository.TimeslotRepository;
import com.timetable_generator.Kavit.response.CountResponse;
import com.timetable_generator.Kavit.services.SessionArrangeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SessionArrangeServiceImpl implements SessionArrangeService {

    private final SchoolRepository schoolRepository;
    private final TimeslotRepository timeslotRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherSubjectClassRepository teacherSubjectClassRepository;
    private final ClassSubjectRepository classSubjectRepository;
    private final ClassTableRepository classTableRepository;
    private final TeacherAvailabilityRepository teacherAvailabilityRepository; // inject

    @Transactional
    @Override
    public CountResponse simulateSession(Long request) {
        School school = schoolRepository.findById(request)
                .orElseThrow(() -> new RuntimeException("School not found"));

        // 1. Populate teacher_availability
        populateTeacherAvailability(school);

        // 2. Update periods per week (existing logic)
        CountResponse response = updatePeriodPerWeek(school);
        return response;
    }

    /**
     * Populates teacher_availability for all teachers and teaching timeslots.
     * If a record already exists, it is left unchanged (to preserve manual changes).
     * Sets is_available = true by default.
     */
    @Transactional
    public void populateTeacherAvailability(School school) {


           
        // Get all distinct teachers in this school (from teacher_subject_class)
        List<Teacher> teachers = teacherSubjectClassRepository.findDistinctTeachersBySchool(school);
        if (teachers.isEmpty()) {
            throw new RuntimeException("No teachers found for school: " + school.getName());
        }

        // Get all non-break timeslots for this school
        List<Timeslot> teachingSlots = timeslotRepository.findBySchoolAndIsBreak(school, false);
        if (teachingSlots.isEmpty()) {
            throw new RuntimeException("No teaching timeslots found for school: " + school.getName());
        }

        // Prepare records to save
        List<TeacherAvailability> toSave = new ArrayList<>();
        for (Teacher teacher : teachers) {
            for (Timeslot slot : teachingSlots) {
                // Check if already exists (to avoid duplication)
                boolean exists = teacherAvailabilityRepository
                        .existsByTeacherAndTimeslotAndSchool(teacher, slot, school);
                if (!exists) {
                    TeacherAvailability ta = new TeacherAvailability();
                    ta.setTeacher(teacher);
                    ta.setTimeslot(slot);
                    ta.setSchool(school);
                    toSave.add(ta);
                }
            }
        }

        // Bulk save
        if (!toSave.isEmpty()) {
            teacherAvailabilityRepository.saveAll(toSave);
        }
    }

    // ---------- (YOUR EXISTING updatePeriodPerWeek METHOD, UNCHANGED) ----------
    private CountResponse updatePeriodPerWeek(School school) {
        Long teachingSlots = timeslotRepository.countBySchoolAndIsBreak(school, false);
        List<ClassTable> classes = classTableRepository.findAllBySchool(school);
        List<Subject> subjects = subjectRepository.findAllBySchool(school);

        CountResponse countResponse = new CountResponse();
        countResponse.setSlots(teachingSlots);

        Map<Long, Double> weightMap = new HashMap<>();
        Map<Long, Long> classMap = new HashMap<>();
        Map<Long, Long> teacherMap = new HashMap<>();

        for (Subject subject : subjects) {
            long teacherCount = teacherSubjectClassRepository.countDistinctTeachersBySubjectAndSchool(subject, school);
            long classCount = teacherSubjectClassRepository.countBySubjectPerClass(subject, school);
            double weight = (teacherCount == 0) ? 0 : (double) classCount / teacherCount;
            weightMap.put(subject.getId(), weight);
            classMap.put(subject.getId(), classCount);
            teacherMap.put(subject.getId(), teacherCount);
        }

        countResponse.setSubjectWeight(weightMap);
        countResponse.setClassCount(classMap);
        countResponse.setTeacherCount(teacherMap);

        double totalWeight = weightMap.values().stream().mapToDouble(Double::doubleValue).sum();
        if (totalWeight <= 0) {
            throw new RuntimeException("Total weight is zero, cannot distribute periods");
        }

        Map<Long, Integer> periodMap = new HashMap<>();
        Map<Long, Double> remainderMap = new HashMap<>();
        int totalAssigned = 0;

        for (Subject subject : subjects) {
            double subjWeight = weightMap.getOrDefault(subject.getId(), 0.0);
            double rawPeriods = (subjWeight / totalWeight) * teachingSlots;
            int floorValue = (int) Math.floor(rawPeriods);
            double remainder = rawPeriods - floorValue;
            periodMap.put(subject.getId(), floorValue);
            remainderMap.put(subject.getId(), remainder);
            totalAssigned += floorValue;
        }

        int remaining = (int) (teachingSlots - totalAssigned);
        while (remaining > 0) {
            Long maxKey = null;
            double maxValue = -1;
            for (Map.Entry<Long, Double> entry : remainderMap.entrySet()) {
                if (entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
                    maxKey = entry.getKey();
                }
            }
            if (maxKey == null) break;
            periodMap.put(maxKey, periodMap.get(maxKey) + 1);
            remainderMap.put(maxKey, 0.0);
            remaining--;
        }

        for (ClassTable cls : classes) {
            for (Subject subject : subjects) {
                Integer finalPeriods = periodMap.get(subject.getId());
                if (finalPeriods == null) continue;
                ClassSubject cs = classSubjectRepository.findByClassTable_IdAndSubject_Id(cls.getId(), subject.getId());
                if (cs != null) {
                    cs.setPeriodsPerWeek(finalPeriods);
                    classSubjectRepository.save(cs);
                }
            }
        }

        return countResponse;
    }
}