
package com.studentmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotNull(message = "Admission year is required")
    private Integer admissionYear;

    @NotNull(message = "Study year is required")
    private Integer studyYear;

    @NotNull(message = "Semester is required")
    private Integer semester;

    @NotBlank(message = "Section is required")
    private String section;

    public Student() {
    }

    public Student(String registrationNumber, String name,
                   String branch, Integer admissionYear,
                   Integer studyYear, Integer semester,
                   String section) {
        this.registrationNumber = registrationNumber;
        this.name = name;
        this.branch = branch;
        this.admissionYear = admissionYear;
        this.studyYear = studyYear;
        this.semester = semester;
        this.section = section;
    }

    public Long getId() { return id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getName() { return name; }
    public String getBranch() { return branch; }
    public Integer getAdmissionYear() { return admissionYear; }
    public Integer getStudyYear() { return studyYear; }
    public Integer getSemester() { return semester; }
    public String getSection() { return section; }

    public void setId(Long id) { this.id = id; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public void setName(String name) { this.name = name; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setAdmissionYear(Integer admissionYear) { this.admissionYear = admissionYear; }
    public void setStudyYear(Integer studyYear) { this.studyYear = studyYear; }
    public void setSemester(Integer semester) { this.semester = semester; }
    public void setSection(String section) { this.section = section; }
}
