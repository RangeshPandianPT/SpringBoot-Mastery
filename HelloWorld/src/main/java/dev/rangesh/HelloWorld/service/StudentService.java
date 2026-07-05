package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = getStudentById(id);
        existingStudent.setName(studentDetails.getName());
        existingStudent.setCourse(studentDetails.getCourse());
        return studentRepository.save(existingStudent);
    }

    public void deleteStudent(Long id) {
        Student existingStudent = getStudentById(id);
        studentRepository.delete(existingStudent);
    }
}
