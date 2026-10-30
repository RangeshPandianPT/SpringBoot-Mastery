package dev.rangesh.HelloWorld.controller;

import dev.rangesh.HelloWorld.dto.EnrollmentGradeDto;
import dev.rangesh.HelloWorld.dto.EnrollmentRequestDto;
import dev.rangesh.HelloWorld.dto.EnrollmentResponseDto;
import dev.rangesh.HelloWorld.model.Enrollment;
import dev.rangesh.HelloWorld.model.EnrollmentStatus;
import dev.rangesh.HelloWorld.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollment Management", description = "Endpoints for managing student course enrollments, status, and grading")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    private EnrollmentResponseDto convertToDto(Enrollment enrollment) {
        return EnrollmentResponseDto.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getName())
                .courseId(enrollment.getCourse().getId())
                .courseCode(enrollment.getCourse().getCourseCode())
                .courseTitle(enrollment.getCourse().getTitle())
                .status(enrollment.getStatus())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .grade(enrollment.getGrade())
                .build();
    }

    @PostMapping
    @Operation(summary = "Enroll student in a course", description = "Creates a new enrollment record for a student in a specific course")
    public ResponseEntity<EnrollmentResponseDto> enrollStudent(@Valid @RequestBody EnrollmentRequestDto request) {
        Enrollment saved = enrollmentService.enrollStudent(request.getStudentId(), request.getCourseId());
        return new ResponseEntity<>(convertToDto(saved), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all enrollments", description = "Fetch a paginated list of enrollments, optionally filtered by status")
    public ResponseEntity<Page<EnrollmentResponseDto>> getAllEnrollments(
            @RequestParam(required = false) EnrollmentStatus status,
            Pageable pageable) {
        Page<Enrollment> page = enrollmentService.getAllEnrollments(status, pageable);
        return ResponseEntity.ok(page.map(this::convertToDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID", description = "Fetch a single enrollment record by its unique identifier")
    public ResponseEntity<EnrollmentResponseDto> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(enrollmentService.getEnrollmentById(id)));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get enrollments by student ID", description = "Fetch all course enrollments for a given student")
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        List<Enrollment> list = enrollmentService.getEnrollmentsByStudent(studentId);
        return ResponseEntity.ok(list.stream().map(this::convertToDto).toList());
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get enrollments by course ID", description = "Fetch all student enrollments (course roster) for a given course")
    public ResponseEntity<List<EnrollmentResponseDto>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        List<Enrollment> list = enrollmentService.getEnrollmentsByCourse(courseId);
        return ResponseEntity.ok(list.stream().map(this::convertToDto).toList());
    }

    @PatchMapping("/{id}/grade")
    @Operation(summary = "Assign or update course grade", description = "Sets grade for an enrollment and automatically marks status as COMPLETED")
    public ResponseEntity<EnrollmentResponseDto> updateGrade(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentGradeDto gradeDto) {
        Enrollment updated = enrollmentService.updateGrade(id, gradeDto.getGrade());
        return ResponseEntity.ok(convertToDto(updated));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update enrollment status", description = "Change enrollment status to ENROLLED, COMPLETED, or DROPPED")
    public ResponseEntity<EnrollmentResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam EnrollmentStatus status) {
        Enrollment updated = enrollmentService.updateStatus(id, status);
        return ResponseEntity.ok(convertToDto(updated));
    }

    @PatchMapping("/{id}/drop")
    @Operation(summary = "Drop course enrollment", description = "Marks the student enrollment as DROPPED")
    public ResponseEntity<EnrollmentResponseDto> dropEnrollment(@PathVariable Long id) {
        Enrollment dropped = enrollmentService.dropEnrollment(id);
        return ResponseEntity.ok(convertToDto(dropped));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete enrollment", description = "Permanently removes an enrollment record")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}

