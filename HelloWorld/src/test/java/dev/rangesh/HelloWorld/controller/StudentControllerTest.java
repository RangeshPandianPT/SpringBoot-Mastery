package dev.rangesh.HelloWorld.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.rangesh.HelloWorld.dto.StudentDto;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Student student;
    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .name("John Doe")
                .course("Computer Science")
                .build();
                
        studentDto = StudentDto.builder()
                .id(1L)
                .fullName("John Doe")
                .enrolledCourse("Computer Science")
                .build();
    }

    @Test
    void testGetAllStudents() throws Exception {
        Page<Student> page = new PageImpl<>(List.of(student));
        when(studentService.getAllStudents(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/students")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$.content[0].enrolledCourse").value("Computer Science"));
    }

    @Test
    void testGetStudentById() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/api/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void testAddStudent() throws Exception {
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        when(studentService.updateStudent(eq(1L), any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/api/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/api/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
