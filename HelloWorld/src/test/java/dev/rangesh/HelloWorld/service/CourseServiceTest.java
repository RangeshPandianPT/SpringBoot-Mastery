package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Course;
import dev.rangesh.HelloWorld.repository.CourseRepository;
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
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;

    @BeforeEach
    void setUp() {
        course = Course.builder()
                .id(1L)
                .courseCode("CS101")
                .title("Introduction to Computer Science")
                .description("Fundamentals of programming and algorithms")
                .credits(4)
                .department("Computer Science")
                .build();
    }

    @Test
    void testGetAllCourses() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> page = new PageImpl<>(List.of(course));

        when(courseRepository.findAll(pageable)).thenReturn(page);

        Page<Course> result = courseService.getAllCourses(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("CS101", result.getContent().get(0).getCourseCode());
        verify(courseRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetCourseById_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals("CS101", result.getCourseCode());
        assertEquals("Introduction to Computer Science", result.getTitle());
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(99L));
    }

    @Test
    void testGetCourseByCode_Success() {
        when(courseRepository.findByCourseCode("CS101")).thenReturn(Optional.of(course));

        Course result = courseService.getCourseByCode("CS101");

        assertNotNull(result);
        assertEquals("CS101", result.getCourseCode());
    }

    @Test
    void testAddCourse_Success() {
        when(courseRepository.existsByCourseCode("CS101")).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.addCourse(course);

        assertNotNull(result);
        assertEquals("CS101", result.getCourseCode());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testAddCourse_DuplicateCode_ThrowsException() {
        when(courseRepository.existsByCourseCode("CS101")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> courseService.addCourse(course));
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void testUpdateCourse_Success() {
        Course updatedDetails = Course.builder()
                .courseCode("CS101")
                .title("Advanced CS")
                .description("Updated description")
                .credits(3)
                .department("Computer Science")
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = courseService.updateCourse(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Advanced CS", course.getTitle());
        assertEquals(3, course.getCredits());
    }

    @Test
    void testDeleteCourse_Success() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        assertDoesNotThrow(() -> courseService.deleteCourse(1L));
        verify(courseRepository, times(1)).delete(course);
    }
}
