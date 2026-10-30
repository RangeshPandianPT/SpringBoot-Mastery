package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Course;
import dev.rangesh.HelloWorld.model.Enrollment;
import dev.rangesh.HelloWorld.model.EnrollmentStatus;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.repository.CourseRepository;
import dev.rangesh.HelloWorld.repository.EnrollmentRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Student student;
    private Course course;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .name("Alice Smith")
                .course("Computer Science")
                .build();

        course = Course.builder()
                .id(1L)
                .courseCode("CS101")
                .title("Intro to Computer Science")
                .credits(4)
                .department("Computer Science")
                .build();

        enrollment = Enrollment.builder()
                .id(10L)
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ENROLLED)
                .enrollmentDate(LocalDate.now())
                .grade(null)
                .build();
    }

    @Test
    void testGetAllEnrollments_WithoutStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Enrollment> page = new PageImpl<>(List.of(enrollment));
        when(enrollmentRepository.findAll(pageable)).thenReturn(page);

        Page<Enrollment> result = enrollmentService.getAllEnrollments(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(enrollmentRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetAllEnrollments_WithStatusFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Enrollment> page = new PageImpl<>(List.of(enrollment));
        when(enrollmentRepository.findByStatus(EnrollmentStatus.ENROLLED, pageable)).thenReturn(page);

        Page<Enrollment> result = enrollmentService.getAllEnrollments(EnrollmentStatus.ENROLLED, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(enrollmentRepository, times(1)).findByStatus(EnrollmentStatus.ENROLLED, pageable);
    }

    @Test
    void testGetEnrollmentById_Success() {
        when(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment));

        Enrollment result = enrollmentService.getEnrollmentById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(EnrollmentStatus.ENROLLED, result.getStatus());
    }

    @Test
    void testGetEnrollmentById_NotFound() {
        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.getEnrollmentById(999L));
    }

    @Test
    void testGetEnrollmentsByStudent_Success() {
        when(studentRepository.existsById(1L)).thenReturn(true);
        when(enrollmentRepository.findByStudentId(1L)).thenReturn(List.of(enrollment));

        List<Enrollment> result = enrollmentService.getEnrollmentsByStudent(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("CS101", result.get(0).getCourse().getCourseCode());
    }

    @Test
    void testGetEnrollmentsByStudent_NotFound() {
        when(studentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.getEnrollmentsByStudent(99L));
    }

    @Test
    void testGetEnrollmentsByCourse_Success() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        when(enrollmentRepository.findByCourseId(1L)).thenReturn(List.of(enrollment));

        List<Enrollment> result = enrollmentService.getEnrollmentsByCourse(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice Smith", result.get(0).getStudent().getName());
    }

    @Test
    void testGetEnrollmentsByCourse_NotFound() {
        when(courseRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.getEnrollmentsByCourse(99L));
    }

    @Test
    void testEnrollStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.empty());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        Enrollment result = enrollmentService.enrollStudent(1L, 1L);

        assertNotNull(result);
        assertEquals(EnrollmentStatus.ENROLLED, result.getStatus());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void testEnrollStudent_StudentNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.enrollStudent(99L, 1L));
    }

    @Test
    void testEnrollStudent_CourseNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> enrollmentService.enrollStudent(1L, 99L));
    }

    @Test
    void testEnrollStudent_AlreadyEnrolled_ThrowsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.of(enrollment));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> enrollmentService.enrollStudent(1L, 1L));
        assertTrue(ex.getMessage().contains("already actively enrolled"));
    }

    @Test
    void testEnrollStudent_ReEnrollDroppedStudent_Success() {
        Enrollment droppedEnrollment = Enrollment.builder()
                .id(10L)
                .student(student)
                .course(course)
                .status(EnrollmentStatus.DROPPED)
                .enrollmentDate(LocalDate.now().minusMonths(2))
                .grade(null)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.findByStudentIdAndCourseId(1L, 1L)).thenReturn(Optional.of(droppedEnrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.enrollStudent(1L, 1L);

        assertNotNull(result);
        assertEquals(EnrollmentStatus.ENROLLED, result.getStatus());
        verify(enrollmentRepository, times(1)).save(droppedEnrollment);
    }

    @Test
    void testUpdateGrade_Success() {
        when(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.updateGrade(10L, "A+");

        assertNotNull(result);
        assertEquals("A+", result.getGrade());
        assertEquals(EnrollmentStatus.COMPLETED, result.getStatus());
    }

    @Test
    void testUpdateGrade_DroppedEnrollment_ThrowsException() {
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        when(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment));

        assertThrows(IllegalArgumentException.class, () -> enrollmentService.updateGrade(10L, "A"));
    }

    @Test
    void testUpdateStatus_Success() {
        when(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.updateStatus(10L, EnrollmentStatus.COMPLETED);

        assertNotNull(result);
        assertEquals(EnrollmentStatus.COMPLETED, result.getStatus());
    }

    @Test
    void testDropEnrollment_Success() {
        when(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.dropEnrollment(10L);

        assertNotNull(result);
        assertEquals(EnrollmentStatus.DROPPED, result.getStatus());
    }

    @Test
    void testDeleteEnrollment_Success() {
        when(enrollmentRepository.findById(10L)).thenReturn(Optional.of(enrollment));
        doNothing().when(enrollmentRepository).delete(enrollment);

        assertDoesNotThrow(() -> enrollmentService.deleteEnrollment(10L));
        verify(enrollmentRepository, times(1)).delete(enrollment);
    }
}

