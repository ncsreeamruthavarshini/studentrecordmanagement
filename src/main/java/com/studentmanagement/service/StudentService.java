package com.studentmanagement.service;

import com.studentmanagement.model.Student;
import com.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Add a new student
    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by database ID
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with ID: " + id));
    }

    // Search by registration number
    public Student getStudentByRegistrationNumber(String registrationNumber) {
        return studentRepository.findAll()
                .stream()
                .filter(student -> student.getRegistrationNumber()
                        .equalsIgnoreCase(registrationNumber))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Student not found: " + registrationNumber));
    }

    // Filter by branch
    public List<Student> getStudentsByBranch(String branch) {
        return studentRepository.findByBranchIgnoreCase(branch);
    }

    // Filter by study year
    public List<Student> getStudentsByYear(Integer year) {
        return studentRepository.findByStudyYear(year);
    }

    // Filter by semester
    public List<Student> getStudentsBySemester(Integer semester) {
        return studentRepository.findBySemester(semester);
    }

    // Filter by branch and year
    public List<Student> getStudentsByBranchAndYear(
            String branch, Integer year) {
        return studentRepository
                .findByBranchIgnoreCaseAndStudyYear(branch, year);
    }

    // Filter by branch, year and semester
    public List<Student> getStudentsByBranchYearAndSemester(
            String branch, Integer year, Integer semester) {
        return studentRepository
                .findByBranchIgnoreCaseAndStudyYearAndSemester(
                        branch, year, semester);
    }

    // Update student
    public Student updateStudent(Long id, Student updatedStudent) {
        Student existingStudent = getStudentById(id);

        existingStudent.setRegistrationNumber(
                updatedStudent.getRegistrationNumber());
        existingStudent.setName(updatedStudent.getName());
        existingStudent.setBranch(updatedStudent.getBranch());
        existingStudent.setAdmissionYear(updatedStudent.getAdmissionYear());
        existingStudent.setStudyYear(updatedStudent.getStudyYear());
        existingStudent.setSemester(updatedStudent.getSemester());
        existingStudent.setSection(updatedStudent.getSection());

        return studentRepository.save(existingStudent);
    }

    // Delete student
    public void deleteStudent(Long id) {
        Student existingStudent = getStudentById(id);
        studentRepository.delete(existingStudent);
    }
}
