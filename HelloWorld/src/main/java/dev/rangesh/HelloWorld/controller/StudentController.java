package dev.rangesh.HelloWorld.controller;

import dev.rangesh.HelloWorld.dto.StudentDto;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // Dependency Injection via constructor
    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Convert Entity to DTO
    private StudentDto convertToDto(Student student) {
        return new StudentDto(student.getId(), student.getName(), student.getCourse());
    }

    // Convert DTO to Entity
    private Student convertToEntity(StudentDto studentDto) {
        return new Student(studentDto.getId(), studentDto.getFullName(), studentDto.getEnrolledCourse());
    }

    // GET /api/students
    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(students);
    }

    // GET /api/students/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(convertToDto(student));
    }

    // POST /api/students
    @PostMapping
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto studentDto) {
        Student student = convertToEntity(studentDto);
        Student savedStudent = studentService.addStudent(student);
        return new ResponseEntity<>(convertToDto(savedStudent), HttpStatus.CREATED);
    }

    // PUT /api/students/{id}
    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @RequestBody StudentDto studentDto) {
        Student student = convertToEntity(studentDto);
        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(convertToDto(updatedStudent));
    }

    // DELETE /api/students/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
