package dev.rangesh.HelloWorld.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentGradeDto {

    @NotBlank(message = "Grade is required")
    @Pattern(regexp = "^[A-F][+-]?$", message = "Grade must be a valid letter grade, e.g. A, A+, B-, C")
    private String grade;
}

