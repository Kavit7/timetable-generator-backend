package com.timetable_generator.Kavit.model;


// Constants are Title Case ("Monday", not "MONDAY") on purpose —
// this matches the MySQL column definition exactly:
// enum('Monday','Tuesday','Wednesday','Thursday','Friday')
// so @Enumerated(EnumType.STRING) writes a value MySQL accepts directly.
public enum DayOfWeekEnum {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday
}
