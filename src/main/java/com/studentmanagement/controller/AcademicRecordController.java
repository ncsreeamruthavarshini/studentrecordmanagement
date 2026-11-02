package com.studentmanagement.controller;

import com.studentmanagement.model.AcademicRecord;
import com.studentmanagement.service.AcademicRecordService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-records")
@CrossOrigin(origins = "*")
public class AcademicRecordController {

    private final AcademicRecordService recordService;

    public AcademicRecordController(AcademicRecordService recordService) {
        this.recordService = recordService;
    }

    // Add marks and attendance
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AcademicRecord addRecord(
            @RequestBody AcademicRecord record) {

        return recordService.addRecord(record);
    }

    // Get all academic records
    @GetMapping
    public List<AcademicRecord> getAllRecords() {
        return recordService.getAllRecords();
    }

    // Get records by student
    @GetMapping("/student/{studentId}")
    public List<AcademicRecord> getRecordsByStudent(
            @PathVariable Long studentId) {

        return recordService.getRecordsByStudent(studentId);
    }

    // Get records by subject
    @GetMapping("/subject/{subjectId}")
    public List<AcademicRecord> getRecordsBySubject(
            @PathVariable Long subjectId) {

        return recordService.getRecordsBySubject(subjectId);
    }

    // Get records by academic year
    @GetMapping("/academic-year/{year}")
    public List<AcademicRecord> getRecordsByAcademicYear(
            @PathVariable Integer year) {

        return recordService.getRecordsByAcademicYear(year);
    }

    // Filter by branch and study year
    @GetMapping("/filter")
    public List<AcademicRecord> getRecordsByBranchAndYear(
            @RequestParam String branch,
            @RequestParam Integer studyYear) {

        return recordService.getRecordsByBranchAndStudyYear(
                branch, studyYear);
    }

    // Update marks and attendance
    @PutMapping("/{id}")
    public AcademicRecord updateRecord(
            @PathVariable Long id,
            @RequestBody AcademicRecord record) {

        return recordService.updateRecord(id, record);
    }

    // Delete academic record
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
    }
}
