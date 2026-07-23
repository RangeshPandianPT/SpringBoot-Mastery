package dev.rangesh.HelloWorld.event;

import dev.rangesh.HelloWorld.model.Student;

/**
 * Domain Event fired when a new Student entity is created.
 */
public class StudentCreatedEvent {

    private final Student student;
    private final long timestamp;

    public StudentCreatedEvent(Student student) {
        this.student = student;
        this.timestamp = System.currentTimeMillis();
    }

    public Student getStudent() {
        return student;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "StudentCreatedEvent{" +
                "studentId=" + student.getId() +
                ", studentName='" + student.getName() + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
