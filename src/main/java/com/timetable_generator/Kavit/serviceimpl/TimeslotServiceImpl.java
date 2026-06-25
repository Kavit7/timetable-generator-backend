package com.timetable_generator.Kavit.serviceimpl;

import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;

import org.springframework.stereotype.Service;

import com.timetable_generator.Kavit.dto.TimeslotGenerationRequest;
import com.timetable_generator.Kavit.model.Timeslot;
import com.timetable_generator.Kavit.model.DayOfWeekEnum;
import com.timetable_generator.Kavit.model.School;
import com.timetable_generator.Kavit.model.SchoolSettings;
import com.timetable_generator.Kavit.repository.SchoolRepository;
import com.timetable_generator.Kavit.repository.SchoolSettingsRepository;
import com.timetable_generator.Kavit.repository.TeacherAvailabilityRepository;
import com.timetable_generator.Kavit.repository.TimeslotRepository;
import com.timetable_generator.Kavit.repository.TimetableRepository;
import com.timetable_generator.Kavit.services.TimeslotService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor

public class TimeslotServiceImpl implements TimeslotService{
      private final TimeslotRepository timeslotRepository;
      private final SchoolSettingsRepository schoolSettingsRepository;
      private final SchoolRepository schoolRepository;
       private final TeacherAvailabilityRepository teacherAvailabilityRepository;
       private final TimetableRepository timetableRepository;



    @Transactional
    @Override
    public List<Timeslot> generateTimeslots(TimeslotGenerationRequest request) {
         School school = schoolRepository.findById(request.getSchoolId())
         .orElseThrow( ()-> new RuntimeException("School not found"));


         validateRequest(request);

         saveOrUpdateRequest(request,school);

         teacherAvailabilityRepository.deleteBySchool(school);
         timetableRepository.deleteBySchool(school);
         timeslotRepository.deleteBySchool(school);


         List<Timeslot> allSlots = new ArrayList<>();



         for (String dayName : request.getSelectedDays()){


            DayOfWeekEnum day = DayOfWeekEnum.valueOf(dayName);
             
            // Morning Session same rule including friday
            allSlots.addAll( generatePeriodsWithBreaks(
                day,
                toLocalTime(request.getMorningStart()),
                toLocalTime(request.getMorningEnd()),
                request.getPeriodLength(),
                request.getBreakAfter(),
                request.getBreakDuration(),
                school

            ));

            if (day ==DayOfWeekEnum.Friday && request.getFridayReligiousStart() != null && request.getFridayReligiousEnd() != null){

                allSlots.addAll(generateFridayEvening(request, school));
            }


            else {
                allSlots.addAll(generatePeriodsWithBreaks(day,
                    toLocalTime(request.getEveningStart()),
                    toLocalTime(request.getEveningEnd()),
                    request.getPeriodLength(),
                    request.getBreakAfter(),
                    request.getBreakDuration(),
                    school
                ));
            }
         }
        return timeslotRepository.saveAll(allSlots);

    }

        // ---------- generation helpers ----------
 
    /**
     * Fills [start, end) with periods of periodMinutes each. After every
     * `breakAfter` consecutive periods, inserts one break slot of
     * breakDuration minutes (is_break = true) before continuing. Stops
     * as soon as the next period (or break) would run past `end`.
     */



    private List<Timeslot> generateFridayEvening(TimeslotGenerationRequest request,
            School school) {
            LocalTime eveningStart = toLocalTime(request.getEveningStart());
            LocalTime religiousStart =toLocalTime(request.getFridayReligiousStart());
            LocalTime religiousEnd= toLocalTime(request.getFridayReligiousEnd());


            List<Timeslot> slots= generatePeriodsWithBreaks(DayOfWeekEnum.Friday,
                eveningStart,
                religiousStart,
                request.getPeriodLength(),
                request.getBreakAfter(),
                request.getBreakDuration(),
                school
            );

            slots.add(buildSlot(DayOfWeekEnum.Friday,religiousStart,religiousEnd,true,school));

        return slots;
    }


    private Timeslot buildSlot(DayOfWeekEnum day, LocalTime start, LocalTime end, boolean isBreak,
            School school) {

                Timeslot timeslot= new Timeslot();
                timeslot.setDay(day);
                timeslot.setStartTime(start);
                timeslot.setEndTime(end);
                timeslot.setIsBreak(isBreak);
                timeslot.setSchool(school);
             return timeslot;
       
    }

    private List<Timeslot> generatePeriodsWithBreaks(DayOfWeekEnum day, LocalTime start,
            LocalTime end, Integer periodMinutes, Integer breakAfter, Integer breakDuration, School school) {
                 List<Timeslot> slots = new ArrayList<>();
                 LocalTime cursor =start;
                 int periodSinceBreak=0;
                
                 boolean useBreaks = breakAfter !=null && breakAfter > 0 && breakDuration != null && breakDuration > 0;



                  while (!cursor.plusMinutes(periodMinutes).isAfter(end)){
                    LocalTime slotEnd = cursor.plusMinutes(periodMinutes);
                    slots.add(buildSlot(day,cursor,slotEnd,false,school));
                    cursor=slotEnd;


                    periodSinceBreak++;

                    if (useBreaks && periodSinceBreak == breakAfter){
                       LocalTime breakEnd= cursor.plusMinutes(breakDuration);
                       if (!breakEnd.isAfter(end)){
                        slots.add(buildSlot(day,cursor,breakEnd,true,school));
                       }
                       periodSinceBreak =0;
                    }
                  }
                return slots;

    }


    private LocalTime toLocalTime(String value) {
             return LocalTime.parse(value);
    }

    // ---------- settings persistence ----------
    private void saveOrUpdateRequest(TimeslotGenerationRequest request, School school) {
          SchoolSettings settings = schoolSettingsRepository.findBySchool(school)
          .orElseGet(SchoolSettings::new);

          settings.setSchool(school);
          settings.setMorningStart(toLocalTime(request.getMorningStart()));
          settings.setMorningEnd(toLocalTime(request.getMorningEnd()));
          settings.setAfternoonStart(toLocalTime(request.getEveningStart()));
          settings.setAfternoonEnd(toLocalTime(request.getEveningEnd()));
          settings.setBreakAfter(request.getBreakAfter());
          settings.setBreakDuration(request.getBreakDuration());
          settings.setPeriodDuration(request.getPeriodLength());
          schoolSettingsRepository.save(settings);
    }


    private void validateRequest(TimeslotGenerationRequest request){
      
        if (request.getSelectedDays() == null || request.getSelectedDays().isEmpty()) {
            throw new RuntimeException("At least one study day must be selected.");
        }
        if (request.getPeriodLength() == null || request.getPeriodLength() <= 0) {
            throw new RuntimeException("Period length must be a positive number.");
        }
 
        LocalTime morningStart = toLocalTime(request.getMorningStart());
        LocalTime morningEnd = toLocalTime(request.getMorningEnd());
        LocalTime eveningStart = toLocalTime(request.getEveningStart());
        LocalTime eveningEnd = toLocalTime(request.getEveningEnd());
 
        if (!morningStart.isBefore(morningEnd)) {
            throw new RuntimeException("Morning start must be before morning end.");
        }
        if (!eveningStart.isBefore(eveningEnd)) {
            throw new RuntimeException("Evening start must be before evening end.");
        }
 
        if (request.getSelectedDays().contains("Friday")
                && request.getFridayReligiousStart() != null
                && request.getFridayReligiousEnd() != null) {
            LocalTime religiousStart = toLocalTime(request.getFridayReligiousStart());
            LocalTime religiousEnd = toLocalTime(request.getFridayReligiousEnd());
 
            if (religiousStart.isBefore(eveningStart) || religiousEnd.isAfter(eveningEnd)) {
                throw new RuntimeException(
                    "Friday religious period must stay within the evening session.");
            }
            if (!religiousStart.isBefore(religiousEnd)) {
                throw new RuntimeException(
                    "Friday religious period start must be before its end.");
            }
        }
    }





    public List<Timeslot> getTimeslot(Long schoolId){
        School school = schoolRepository.findById(schoolId)
        .orElseThrow(()-> new RuntimeException("school not found"));
       List<Timeslot> slots =timeslotRepository.findAllBySchool(school);
       return   slots;
    }
    }
    

