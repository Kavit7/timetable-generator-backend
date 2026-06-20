package com.timetable_generator.Kavit.serviceimpl;
import com.timetable_generator.Kavit.model.ClassSubject;
import com.timetable_generator.Kavit.model.ClassTable;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.Subject;
import com.timetable_generator.Kavit.model.Teacher;
import com.timetable_generator.Kavit.model.TeacherSubjectClass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.timetable_generator.Kavit.dto.ExcelRecords;
import com.timetable_generator.Kavit.dto.ExcelRequest;
import com.timetable_generator.Kavit.repository.ClassSubjectRepository;
import com.timetable_generator.Kavit.repository.ClassTableRepository;
import com.timetable_generator.Kavit.repository.SchoolRepository;
import com.timetable_generator.Kavit.repository.SubjectRepository;
import com.timetable_generator.Kavit.repository.TeacherRepository;
import com.timetable_generator.Kavit.repository.TeacherSubjectClassRepository;
import com.timetable_generator.Kavit.services.TimetableImportService;

import lombok.RequiredArgsConstructor;





@Service
@RequiredArgsConstructor
public class TimetableImportServiceImpl implements TimetableImportService{



   private final SchoolRepository schoolRepository; 
   private final TeacherRepository teacherRepository;
   private final ClassTableRepository classTableRepository;
   private final SubjectRepository subjectRepository;
   private final ClassSubjectRepository classSubjectRepository;
   private final TeacherSubjectClassRepository teacherSubjectClassRepository;

    @Override
    public void saveImportData(ExcelRequest request) {
       Long schoolId = request.getSchoolId();
       School school = schoolRepository.findById(schoolId)
       .orElseThrow( ()-> new RuntimeException("School not found")); 



       Map<String, ClassTable> classTableCache = new HashMap<>();
       Map<String, Teacher> teacherCache= new HashMap<>();
       Map<String, Subject> subjectCache= new HashMap<>();

        for (String name : request.getUniqueData().getClasses()){
                 ClassTable ct = findorCreateClass(name,school);
                 classTableCache.put(name.toLowerCase(),ct);
        }
        for (String name : request.getUniqueData().getTeachers()){
            Teacher tc = findorCreateTeacher(name, school);
            teacherCache.put(name.toLowerCase(),tc);
        }

        for(String name: request.getUniqueData().getSubjects()){
            Subject sb = findorCreateSubject(name,school);
            subjectCache.put(name.toLowerCase(),sb);
        }



                 // classSubject table catch
        Map<String ,ClassSubject> classSubjectCache= new HashMap<>();
        for (ExcelRecords row   : request.getRecords()){
             ClassTable ct = classTableCache.get(row.getClassName().toLowerCase());
             Subject sc = subjectCache.get(row.getSubject().toLowerCase());


            if (ct == null || sc == null){
              throw new IllegalStateException( "Row references a class/subject not present in uniqueData: "
                        + row.getClassName() + " / " + row.getSubject());              
            }


          String ckey = ct.getId() + "-"  +sc.getId();
  
        classSubjectCache.computeIfAbsent(ckey, k -> findorCreateClassSubject(ct,sc,school) );
         
}



         for (ExcelRecords row : request.getRecords()) {
            Teacher teacher = teacherCache.get(row.getTeacher().toLowerCase());
            ClassTable ct = classTableCache.get(row.getClassName().toLowerCase());
            Subject subject = subjectCache.get(row.getSubject().toLowerCase());

            if (teacher == null) {
                throw new IllegalStateException(
                    "Row references a teacher not present in uniqueData: "
                        + row.getTeacher());
            }
            findOrCreateTeacherSubjectClass(teacher,ct,subject,school);
        }
    }
    private TeacherSubjectClass findOrCreateTeacherSubjectClass(Teacher teacher, ClassTable ct, Subject subject, School school) {
          return teacherSubjectClassRepository.findByClassTableAndSubjectAndTeacherAndSchool(ct,subject,teacher,school)
          .orElseGet(()->{
            TeacherSubjectClass tsc = new TeacherSubjectClass();
            tsc.setTeacher(teacher);
            tsc.setSchool(school);
            tsc.setSubject(subject);
            tsc.setClassTable(ct);
            return teacherSubjectClassRepository.save(tsc);
          });
    }
    private ClassSubject findorCreateClassSubject(ClassTable ct, Subject sc, School school) {
         return classSubjectRepository.findByClassTableAndSubject(ct,sc)
         .orElseGet(()->{
            ClassSubject cs = new ClassSubject();
            cs.setSchool(school);
            cs.setSubject(sc);
            cs.setClassTable(ct);
            return classSubjectRepository.save(cs);
         });
            
    }
    private Subject findorCreateSubject(String name, School school) {

        return subjectRepository.findByNameIgnoreCaseAndSchool(name, school)
        .orElseGet(()-> {
            Subject sc = new Subject();
            sc.setName(name);
            sc.setSchool(school);
            return subjectRepository.save(sc);
        });
       
    }

    private Teacher findorCreateTeacher(String name, School school) {
        return teacherRepository.findByNameIgnoreCaseAndSchool(name, school)
        .orElseGet(() -> {
            Teacher tc = new Teacher();
            tc.setName(name);
            tc.setSchool(school);
            return teacherRepository.save(tc);
        });
    }

    private ClassTable findorCreateClass(String name, School school) {
        return classTableRepository.findByNameIgnoreCaseAndSchool(name, school)
        .orElseGet(() ->{
            ClassTable ct = new ClassTable();
            ct.setName(name);
            ct.setSchool(school);
            return classTableRepository.save(ct);
        });
    }
}


