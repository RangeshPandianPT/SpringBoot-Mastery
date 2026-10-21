package dev.rangesh.HelloWorld.controller;

import dev.rangesh.HelloWorld.dto.CourseDto;
import dev.rangesh.HelloWorld.model.Course;
import dev.rangesh.HelloWorld.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course Management", description = "Endpoints for managing academic courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    private CourseDto convertToDto(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .description(course.getDescription())
                .credits(course.getCredits())
                .department(course.getDepartment())
                .build();
    }

    private Course convertToEntity(CourseDto dto) {
        return Course.builder()
                .id(dto.getId())
                .courseCode(dto.getCourseCode())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .credits(dto.getCredits())
                .department(dto.getDepartment())
                .build();
    }

    @GetMapping
    @Operation(summary = "Get all courses", description = "Fetch a paginated list of all courses, optionally filtered by department")
    public ResponseEntity<Page<CourseDto>> getAllCourses(
            @RequestParam(required = false) String department,
            Pageable pageable) {
        Page<Course> page = (department != null && !department.isBlank())
                ? courseService.getCoursesByDepartment(department, pageable)
                : courseService.getAllCourses(pageable);
        return ResponseEntity.ok(page.map(this::convertToDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Fetch a course by its database ID")
    public ResponseEntity<CourseDto> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(convertToDto(courseService.getCourseById(id)));
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get course by code", description = "Fetch a course by its unique code (e.g. CS101)")
    public ResponseEntity<CourseDto> getCourseByCode(@PathVariable String code) {
        return ResponseEntity.ok(convertToDto(courseService.getCourseByCode(code)));
    }

    @PostMapping
    @Operation(summary = "Create course", description = "Add a new course to the catalog")
    public ResponseEntity<CourseDto> addCourse(@Valid @RequestBody CourseDto courseDto) {
        Course saved = courseService.addCourse(convertToEntity(courseDto));
        return new ResponseEntity<>(convertToDto(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course", description = "Update details of an existing course")
    public ResponseEntity<CourseDto> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDto courseDto) {
        Course updated = courseService.updateCourse(id, convertToEntity(courseDto));
        return ResponseEntity.ok(convertToDto(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course", description = "Remove a course by its ID")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
