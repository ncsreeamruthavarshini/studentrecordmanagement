package com.studentmanagement.controller;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Add student
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@Valid @RequestBody Student student) {
        return studentService.addStudent(student);
    }

    // Get all students or filter by branch, year and semester
    @GetMapping
    public List<Student> getAllStudents(
            @RequestParam(required = false) String branch,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer semester) {

        if (branch != null && year != null && semester != null) {
            return studentService.getStudentsByBranchYearAndSemester(
                    branch, year, semester);
        }

        if (branch != null && year != null) {
            return studentService.getStudentsByBranchAndYear(branch, year);
        }

        if (branch != null) {
            return studentService.getStudentsByBranch(branch);
        }

        if (year != null) {
            return studentService.getStudentsByYear(year);
        }

        if (semester != null) {
            return studentService.getStudentsBySemester(semester);
        }

        return studentService.getAllStudents();
    }

    // Get student by ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    // Search by registration number
    @GetMapping("/registration/{registrationNumber}")
    public Student getByRegistrationNumber(
            @PathVariable String registrationNumber) {
        return studentService.getStudentByRegistrationNumber(
                registrationNumber);
    }

    // Update student
    @PutMapping("/{id}")
    public Student updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    // Delete student
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }
}
