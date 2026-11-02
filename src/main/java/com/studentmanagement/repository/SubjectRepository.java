
package com.studentmanagement.repository;

import com.studentmanagement.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByBranchIgnoreCase(String branch);

    List<Subject> findBySemester(Integer semester);

    List<Subject> findByBranchIgnoreCaseAndSemester(
            String branch, Integer semester);

    Optional<Subject> findBySubjectCode(String subjectCode);
}
