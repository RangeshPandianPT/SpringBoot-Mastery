package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .name("John Doe")
                .course("Computer Science")
                .build();
    }

    @Test
    void testGetAllStudents() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> page = new PageImpl<>(List.of(student));
        
        when(studentRepository.findAll(pageable)).thenReturn(page);

        Page<Student> result = studentService.getAllStudents(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).getName());
        verify(studentRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetStudentById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            studentService.getStudentById(2L);
        });
    }

    @Test
    void testAddStudent() {
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student result = studentService.addStudent(student);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testUpdateStudent() {
        Student updatedDetails = Student.builder().name("Jane Doe").course("Physics").build();
        
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student); // student object gets modified in service

        Student result = studentService.updateStudent(1L, updatedDetails);

        assertEquals("Jane Doe", result.getName());
        assertEquals("Physics", result.getCourse());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        studentService.deleteStudent(1L);

        verify(studentRepository, times(1)).delete(student);
    }
}
