package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.annotation.LogExecutionTime;
import dev.rangesh.HelloWorld.event.StudentCreatedEvent;
import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public StudentService(StudentRepository studentRepository, ApplicationEventPublisher eventPublisher) {
        this.studentRepository = studentRepository;
        this.eventPublisher = eventPublisher;
    }

    @LogExecutionTime
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    @LogExecutionTime
    @Cacheable(value = "students", key = "#id")
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    @LogExecutionTime
    public Student addStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        // Publish event asynchronously via Spring ApplicationEventPublisher
        eventPublisher.publishEvent(new StudentCreatedEvent(savedStudent));
        return savedStudent;
    }

    @LogExecutionTime
    @CachePut(value = "students", key = "#id")
    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = getStudentById(id);
        existingStudent.setName(studentDetails.getName());
        existingStudent.setCourse(studentDetails.getCourse());
        return studentRepository.save(existingStudent);
    }

    @LogExecutionTime
    @CacheEvict(value = "students", key = "#id")
    public void deleteStudent(Long id) {
        Student existingStudent = getStudentById(id);
        studentRepository.delete(existingStudent);
    }
}
