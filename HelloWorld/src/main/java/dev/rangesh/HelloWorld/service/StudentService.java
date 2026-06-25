package dev.rangesh.HelloWorld.service;

import dev.rangesh.HelloWorld.exception.ResourceNotFoundException;
import dev.rangesh.HelloWorld.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    // Using an in-memory list to simulate a database for basic learning
    private final List<Student> students = new ArrayList<>();
    private long idCounter = 1;

    public StudentService() {
        // Add some initial data
        students.add(new Student(idCounter++, "Alice Smith", "Computer Science"));
        students.add(new Student(idCounter++, "Bob Johnson", "Mathematics"));
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student getStudentById(Long id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public Student addStudent(Student student) {
        student.setId(idCounter++);
        students.add(student);
        return student;
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = getStudentById(id);
        existingStudent.setName(studentDetails.getName());
        existingStudent.setCourse(studentDetails.getCourse());
        return existingStudent;
    }

    public void deleteStudent(Long id) {
        Student existingStudent = getStudentById(id);
        students.remove(existingStudent);
    }
}
