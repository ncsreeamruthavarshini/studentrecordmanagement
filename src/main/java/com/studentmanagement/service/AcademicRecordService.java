
package com.studentmanagement.service;

import com.studentmanagement.model.AcademicRecord;
import com.studentmanagement.model.Student;
import com.studentmanagement.model.Subject;

import com.studentmanagement.repository.AcademicRecordRepository;
import com.studentmanagement.repository.StudentRepository;
import com.studentmanagement.repository.SubjectRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicRecordService {

    private final AcademicRecordRepository recordRepository;
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;

    public AcademicRecordService(
            AcademicRecordRepository recordRepository,
            StudentRepository studentRepository,
            SubjectRepository subjectRepository) {

        this.recordRepository = recordRepository;
        this.studentRepository = studentRepository;
        this.subjectRepository = subjectRepository;
    }

    // Validate marks and attendance
    private void validateRecord(AcademicRecord record, Subject subject) {

        if (record.getInternalMarks() == null ||
            record.getExternalMarks() == null ||
            record.getClassesConducted() == null ||
            record.getClassesAttended() == null ||
            record.getAcademicYear() == null) {

            throw new IllegalArgumentException(
                    "All academic record fields are required");
        }

        if (record.getInternalMarks() < 0 ||
            record.getInternalMarks() > subject.getMaxInternalMarks()) {

            throw new IllegalArgumentException(
                    "Internal marks exceed the allowed limit");
        }

        if (record.getExternalMarks() < 0 ||
            record.getExternalMarks() > subject.getMaxExternalMarks()) {

            throw new IllegalArgumentException(
                    "External marks exceed the allowed limit");
        }

        if (record.getClassesConducted() < 0 ||
            record.getClassesAttended() < 0 ||
            record.getClassesAttended() > record.getClassesConducted()) {

            throw new IllegalArgumentException(
                    "Invalid attendance values");
        }
    }

    // Add academic record
    public AcademicRecord addRecord(AcademicRecord record) {

        if (record.getStudent() == null ||
            record.getStudent().getId() == null ||
            record.getSubject() == null ||
            record.getSubject().getId() == null) {

            throw new IllegalArgumentException(
                    "Student ID and Subject ID are required");
        }

        Student student = studentRepository
                .findById(record.getStudent().getId())
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Subject subject = subjectRepository
                .findById(record.getSubject().getId())
                .orElseThrow(() ->
                        new RuntimeException("Subject not found"));

        validateRecord(record, subject);

        record.setStudent(student);
        record.setSubject(subject);

        return recordRepository.save(record);
    }

    // Get all academic records
    public List<AcademicRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    // Get records for a student
    public List<AcademicRecord> getRecordsByStudent(Long studentId) {
        return recordRepository.findByStudentId(studentId);
    }

    // Get records for a subject
    public List<AcademicRecord> getRecordsBySubject(Long subjectId) {
        return recordRepository.findBySubjectId(subjectId);
    }

    // Get records by academic year
    public List<AcademicRecord> getRecordsByAcademicYear(Integer year) {
        return recordRepository.findByAcademicYear(year);
    }

    // Get records by student and academic year
    public List<AcademicRecord> getStudentRecordsByYear(
            Long studentId, Integer year) {

        return recordRepository
                .findByStudentIdAndAcademicYear(studentId, year);
    }

    // Get records by branch and study year
    public List<AcademicRecord> getRecordsByBranchAndStudyYear(
            String branch, Integer studyYear) {

        return recordRepository
                .findByStudentBranchIgnoreCaseAndStudentStudyYear(
                        branch, studyYear);
    }

    // Update academic record
    public AcademicRecord updateRecord(
            Long id, AcademicRecord updatedRecord) {

        AcademicRecord existing = recordRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Academic record not found"));

        if (updatedRecord.getSubject() == null ||
            updatedRecord.getSubject().getId() == null) {

            throw new IllegalArgumentException("Subject ID is required");
        }

        Subject subject = subjectRepository
                .findById(updatedRecord.getSubject().getId())
                .orElseThrow(() ->
                        new RuntimeException("Subject not found"));

        validateRecord(updatedRecord, subject);

        existing.setSubject(subject);
        existing.setAcademicYear(updatedRecord.getAcademicYear());
        existing.setInternalMarks(updatedRecord.getInternalMarks());
        existing.setExternalMarks(updatedRecord.getExternalMarks());
        existing.setClassesConducted(updatedRecord.getClassesConducted());
        existing.setClassesAttended(updatedRecord.getClassesAttended());

        return recordRepository.save(existing);
    }

    // Delete academic record
    public void deleteRecord(Long id) {
        AcademicRecord record = recordRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Academic record not found"));

        recordRepository.delete(record);
    }
}
