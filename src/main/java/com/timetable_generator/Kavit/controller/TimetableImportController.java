package com.timetable_generator.Kavit.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timetable_generator.Kavit.dto.ExcelRequest;
import com.timetable_generator.Kavit.serviceimpl.TimetableImportServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/excel")


@RequiredArgsConstructor
public class TimetableImportController {

    
    private final TimetableImportServiceImpl timetableImportServiceImpl;

    
    @PostMapping("/import")
    public ResponseEntity<?> importTimetableExcel(@RequestBody ExcelRequest excelRequest){

        timetableImportServiceImpl.saveImportData(excelRequest);
        return ResponseEntity.ok(Map.of("message","successs fully"));
    }
    
}
