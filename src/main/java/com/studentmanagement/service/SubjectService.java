
package com.studentmanagement.service;

import com.studentmanagement.model.Subject;
import com.studentmanagement.repository.SubjectRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    // Add a subject
    public Subject addSubject(Subject subject) {
        if (subject.getMaxInternalMarks() == null ||
            subject.getMaxExternalMarks() == null ||
            subject.getMaxInternalMarks() <= 0 ||
            subject.getMaxExternalMarks() <= 0) {
            throw new IllegalArgumentException(
                    "Maximum marks must be greater than zero");
        }

        return subjectRepository.save(subject);
    }

    // Get all subjects
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // Get subject by ID
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Subject not found: " + id));
    }

    // Get subjects by branch
    public List<Subject> getSubjectsByBranch(String branch) {
        return subjectRepository.findByBranchIgnoreCase(branch);
    }

    // Get subjects by semester
    public List<Subject> getSubjectsBySemester(Integer semester) {
        return subjectRepository.findBySemester(semester);
    }

    // Get subjects by branch and semester
    public List<Subject> getSubjectsByBranchAndSemester(
            String branch, Integer semester) {
        return subjectRepository
                .findByBranchIgnoreCaseAndSemester(branch, semester);
    }

    // Update subject
    public Subject updateSubject(Long id, Subject updatedSubject) {
        Subject existing = getSubjectById(id);

        existing.setSubjectCode(updatedSubject.getSubjectCode());
        existing.setSubjectName(updatedSubject.getSubjectName());
        existing.setBranch(updatedSubject.getBranch());
        existing.setSemester(updatedSubject.getSemester());
        existing.setMaxInternalMarks(
                updatedSubject.getMaxInternalMarks());
        existing.setMaxExternalMarks(
                updatedSubject.getMaxExternalMarks());

        return subjectRepository.save(existing);
    }

    // Delete subject
    public void deleteSubject(Long id) {
        Subject subject = getSubjectById(id);
        subjectRepository.delete(subject);
    }
}
