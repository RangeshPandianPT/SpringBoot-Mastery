package dev.rangesh.HelloWorld.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {

    private Long id;

    @NotBlank(message = "Course code is required")
    @Pattern(regexp = "^[A-Z]{2,4}[0-9]{3,4}$", message = "Course code must match format like CS101 or MATH2001")
    private String courseCode;

    @NotBlank(message = "Course title is required")
    @Size(min = 3, max = 150, message = "Course title must be between 3 and 150 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Credits are required")
    @Min(value = 1, message = "Credits must be at least 1")
    @Max(value = 6, message = "Credits cannot exceed 6")
    private Integer credits;

    @NotBlank(message = "Department is required")
    private String department;
}
