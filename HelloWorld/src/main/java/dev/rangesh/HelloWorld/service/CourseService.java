package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.annotation.LogExecutionTime;
import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Course;
import dev.rangesh.HelloWorld.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @LogExecutionTime
    public Page<Course> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @LogExecutionTime
    public Page<Course> getCoursesByDepartment(String department, Pageable pageable) {
        return courseRepository.findByDepartmentIgnoreCase(department, pageable);
    }

    @LogExecutionTime
    @Cacheable(value = "courses", key = "#id")
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    @LogExecutionTime
    public Course getCourseByCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with code: " + courseCode));
    }

    @LogExecutionTime
    @Transactional
    public Course addCourse(Course course) {
        if (courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new IllegalArgumentException("Course with code '" + course.getCourseCode() + "' already exists.");
        }
        return courseRepository.save(course);
    }

    @LogExecutionTime
    @Transactional
    @CachePut(value = "courses", key = "#id")
    public Course updateCourse(Long id, Course courseDetails) {
        Course existingCourse = getCourseById(id);

        if (!existingCourse.getCourseCode().equalsIgnoreCase(courseDetails.getCourseCode())
                && courseRepository.existsByCourseCode(courseDetails.getCourseCode())) {
            throw new IllegalArgumentException("Course code '" + courseDetails.getCourseCode() + "' is already in use.");
        }

        existingCourse.setCourseCode(courseDetails.getCourseCode());
        existingCourse.setTitle(courseDetails.getTitle());
        existingCourse.setDescription(courseDetails.getDescription());
        existingCourse.setCredits(courseDetails.getCredits());
        existingCourse.setDepartment(courseDetails.getDepartment());

        return courseRepository.save(existingCourse);
    }

    @LogExecutionTime
    @Transactional
    @CacheEvict(value = "courses", key = "#id")
    public void deleteCourse(Long id) {
        Course existingCourse = getCourseById(id);
        courseRepository.delete(existingCourse);
    }
}
