package com.studentmanagement.controller;

import com.studentmanagement.model.Subject;
import com.studentmanagement.service.SubjectService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    // Add a subject
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Subject addSubject(@Valid @RequestBody Subject subject) {
        return subjectService.addSubject(subject);
    }

    // Get all subjects or filter them
    @GetMapping
    public List<Subject> getSubjects(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer semester) {

        if (branch != null && semester != null) {
            return subjectService.getSubjectsByBranchAndSemester(
                    branch, semester);
        }

        if (branch != null) {
            return subjectService.getSubjectsByBranch(branch);
        }

        if (semester != null) {
            return subjectService.getSubjectsBySemester(semester);
        }

        return subjectService.getAllSubjects();
    }

    // Get subject by ID
    @GetMapping("/{id}")
    public Subject getSubjectById(@PathVariable Long id) {
        return subjectService.getSubjectById(id);
    }

    // Update subject
    @PutMapping("/{id}")
    public Subject updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody Subject subject) {

        return subjectService.updateSubject(id, subject);
    }

    // Delete subject
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
    }
}
