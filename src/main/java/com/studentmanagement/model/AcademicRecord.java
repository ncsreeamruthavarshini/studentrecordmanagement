
package com.studentmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(
    name = "academic_records",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"student_id", "subject_id", "academic_year"}
        )
    }
)
public class AcademicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotNull
    private Integer academicYear;

    @NotNull
    @Min(0)
    private Integer internalMarks;

    @NotNull
    @Min(0)
    private Integer externalMarks;

    @NotNull
    @Min(0)
    private Integer classesConducted;

    @NotNull
    @Min(0)
    private Integer classesAttended;

    public AcademicRecord() {
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public Integer getInternalMarks() {
        return internalMarks;
    }

    public Integer getExternalMarks() {
        return externalMarks;
    }

    public Integer getClassesConducted() {
        return classesConducted;
    }

    public Integer getClassesAttended() {
        return classesAttended;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public void setInternalMarks(Integer internalMarks) {
        this.internalMarks = internalMarks;
    }

    public void setExternalMarks(Integer externalMarks) {
        this.externalMarks = externalMarks;
    }

    public void setClassesConducted(Integer classesConducted) {
        this.classesConducted = classesConducted;
    }

    public void setClassesAttended(Integer classesAttended) {
        this.classesAttended = classesAttended;
    }

    public Integer getTotalMarks() {
        return internalMarks + externalMarks;
    }

    public Double getAttendancePercentage() {
        if (classesConducted == 0) {
            return 0.0;
        }

        return (classesAttended * 100.0) / classesConducted;
    }
}
