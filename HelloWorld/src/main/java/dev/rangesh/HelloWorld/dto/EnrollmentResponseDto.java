package dev.rangesh.HelloWorld.dto;

import dev.rangesh.HelloWorld.model.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponseDto {

    private Long id;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseCode;
    private String courseTitle;
    private EnrollmentStatus status;
    private LocalDate enrollmentDate;
    private String grade;
}

