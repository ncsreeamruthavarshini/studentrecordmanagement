
package com.studentmanagement.repository;

import com.studentmanagement.model.AcademicRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademicRecordRepository
        extends JpaRepository<AcademicRecord, Long> {

    List<AcademicRecord> findByStudentId(Long studentId);

    List<AcademicRecord> findBySubjectId(Long subjectId);

    List<AcademicRecord> findByAcademicYear(Integer academicYear);

    List<AcademicRecord> findByStudentIdAndAcademicYear(
            Long studentId, Integer academicYear);

    List<AcademicRecord> findByStudentBranchIgnoreCaseAndStudentStudyYear(
            String branch, Integer studyYear);
}
