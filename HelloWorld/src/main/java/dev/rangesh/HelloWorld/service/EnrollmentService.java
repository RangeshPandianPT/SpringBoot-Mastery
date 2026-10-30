package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.annotation.LogExecutionTime;
import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Course;
import dev.rangesh.HelloWorld.model.Enrollment;
import dev.rangesh.HelloWorld.model.EnrollmentStatus;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.repository.CourseRepository;
import dev.rangesh.HelloWorld.repository.EnrollmentRepository;
import dev.rangesh.HelloWorld.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @LogExecutionTime
    public Page<Enrollment> getAllEnrollments(EnrollmentStatus status, Pageable pageable) {
        if (status != null) {
            return enrollmentRepository.findByStatus(status, pageable);
        }
        return enrollmentRepository.findAll(pageable);
    }

    @LogExecutionTime
    @Cacheable(value = "enrollments", key = "#id")
    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
    }

    @LogExecutionTime
    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return enrollmentRepository.findByStudentId(studentId);
    }

    @LogExecutionTime
    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        return enrollmentRepository.findByCourseId(courseId);
    }

    @LogExecutionTime
    @Transactional
    public Enrollment enrollStudent(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        Optional<Enrollment> existingOpt = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (existingOpt.isPresent()) {
            Enrollment existing = existingOpt.get();
            if (existing.getStatus() == EnrollmentStatus.ENROLLED) {
                throw new IllegalArgumentException(
                        "Student is already actively enrolled in course: " + course.getCourseCode());
            }
            // Re-activate enrollment
            existing.setStatus(EnrollmentStatus.ENROLLED);
            existing.setEnrollmentDate(LocalDate.now());
            existing.setGrade(null);
            return enrollmentRepository.save(existing);
        }

        Enrollment newEnrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .status(EnrollmentStatus.ENROLLED)
                .enrollmentDate(LocalDate.now())
                .grade(null)
                .build();

        return enrollmentRepository.save(newEnrollment);
    }

    @LogExecutionTime
    @Transactional
    @CachePut(value = "enrollments", key = "#id")
    public Enrollment updateGrade(Long id, String grade) {
        Enrollment enrollment = getEnrollmentById(id);
        if (enrollment.getStatus() == EnrollmentStatus.DROPPED) {
            throw new IllegalArgumentException("Cannot assign grade to a dropped course enrollment.");
        }
        enrollment.setGrade(grade.toUpperCase().trim());
        enrollment.setStatus(EnrollmentStatus.COMPLETED);
        return enrollmentRepository.save(enrollment);
    }

    @LogExecutionTime
    @Transactional
    @CachePut(value = "enrollments", key = "#id")
    public Enrollment updateStatus(Long id, EnrollmentStatus status) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    @LogExecutionTime
    @Transactional
    @CachePut(value = "enrollments", key = "#id")
    public Enrollment dropEnrollment(Long id) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollment.setStatus(EnrollmentStatus.DROPPED);
        return enrollmentRepository.save(enrollment);
    }

    @LogExecutionTime
    @Transactional
    @CacheEvict(value = "enrollments", key = "#id")
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = getEnrollmentById(id);
        enrollmentRepository.delete(enrollment);
    }
}

