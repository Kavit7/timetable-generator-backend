package com.timetable_generator.Kavit.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelRequest
{
    private UniqueData uniqueData;
    private List<ExcelRecords> records;
    private Long schoolId;
    
    
}
