package com.studentmanagement.repository;

import com.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByBranchIgnoreCase(String branch);

    List<Student> findByStudyYear(Integer studyYear);

    List<Student> findBySemester(Integer semester);

    List<Student> findByBranchIgnoreCaseAndStudyYear(
            String branch, Integer studyYear);

    List<Student> findByBranchIgnoreCaseAndStudyYearAndSemester(
            String branch, Integer studyYear, Integer semester);
}
