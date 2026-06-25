package com.timetable_generator.Kavit.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable_generator.Kavit.dto.SessionDto;
import com.timetable_generator.Kavit.dto.TimeslotGenerationRequest;
import com.timetable_generator.Kavit.model.Timeslot;
import com.timetable_generator.Kavit.response.CountResponse;
import com.timetable_generator.Kavit.response.TimetableResponseDto;
import com.timetable_generator.Kavit.serviceimpl.TimeslotServiceImpl;
import com.timetable_generator.Kavit.serviceimpl.TimetableServiceImpl;
import com.timetable_generator.Kavit.serviceimpl.SessionArrangeServiceImpl;
import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
public class TimeslotController {
    
private final TimeslotServiceImpl timeslotServiceImpl;
private final SessionArrangeServiceImpl sessionArrangeServiceImpl;
private final TimetableServiceImpl timetableServiceImpl;

    @PostMapping("/generate-timeslots")
    public ResponseEntity<List<Timeslot>> generateTimeslots(
            @RequestBody TimeslotGenerationRequest request) {
        List<Timeslot> result = timeslotServiceImpl.generateTimeslots(request);
        return ResponseEntity.ok(result);
    }


     @PostMapping("/simulate")
      public ResponseEntity<CountResponse> counter(@RequestBody SessionDto request){
      CountResponse response = sessionArrangeServiceImpl.simulateSession(request.getSchoolId());
    return ResponseEntity.ok(response);
    
   }
   @GetMapping("/timeslots/{schoolId}")
   public ResponseEntity<?> getTimeslots(@PathVariable Long schoolId){
      List<Timeslot> slots= timeslotServiceImpl.getTimeslot(schoolId);
return ResponseEntity.ok(slots);


   }


    @PostMapping("/generate/{schoolId}")
    public ResponseEntity<Map<String, Object>> generate(@PathVariable Long schoolId) {
        Map<String, Object> result = timetableServiceImpl.generateTimetable(schoolId);
        return ResponseEntity.ok(result);
    }

    // Fetch timetable iliyoundwa
    @GetMapping("fetchtimetable/{schoolId}")
    public ResponseEntity<List<TimetableResponseDto>> getTimetable(
            @PathVariable Long schoolId) {
        List<TimetableResponseDto> timetable = timetableServiceImpl.getTimetable(schoolId);
        return ResponseEntity.ok(timetable);
    }

    
}
