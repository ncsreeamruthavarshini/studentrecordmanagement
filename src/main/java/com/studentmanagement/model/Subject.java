
package com.studentmanagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String subjectCode;

    @NotBlank
    private String subjectName;

    @NotBlank
    private String branch;

    @NotNull
    @Min(1)
    private Integer semester;

    @NotNull
    @Min(1)
    private Integer maxInternalMarks;

    @NotNull
    @Min(1)
    private Integer maxExternalMarks;

    public Subject() {
    }

    public Long getId() {
        return id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getBranch() {
        return branch;
    }

    public Integer getSemester() {
        return semester;
    }

    public Integer getMaxInternalMarks() {
        return maxInternalMarks;
    }

    public Integer getMaxExternalMarks() {
        return maxExternalMarks;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public void setMaxInternalMarks(Integer maxInternalMarks) {
        this.maxInternalMarks = maxInternalMarks;
    }

    public void setMaxExternalMarks(Integer maxExternalMarks) {
        this.maxExternalMarks = maxExternalMarks;
    }
}
