package dev.rangesh.HelloWorld.dto;

public class StudentDto {
    private Long id;
    private String fullName;
    private String enrolledCourse;

    public StudentDto() {
    }

    public StudentDto(Long id, String fullName, String enrolledCourse) {
        this.id = id;
        this.fullName = fullName;
        this.enrolledCourse = enrolledCourse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEnrolledCourse() {
        return enrolledCourse;
    }

    public void setEnrolledCourse(String enrolledCourse) {
        this.enrolledCourse = enrolledCourse;
    }
}
