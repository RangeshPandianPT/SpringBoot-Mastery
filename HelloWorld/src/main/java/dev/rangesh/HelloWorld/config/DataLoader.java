package dev.rangesh.HelloWorld.config;

import dev.rangesh.HelloWorld.model.AppUser;
import dev.rangesh.HelloWorld.model.Course;
import dev.rangesh.HelloWorld.model.Enrollment;
import dev.rangesh.HelloWorld.model.EnrollmentStatus;
import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.repository.AppUserRepository;
import dev.rangesh.HelloWorld.repository.CourseRepository;
import dev.rangesh.HelloWorld.repository.EnrollmentRepository;
import dev.rangesh.HelloWorld.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(StudentRepository repository,
                                  AppUserRepository userRepository,
                                  CourseRepository courseRepository,
                                  EnrollmentRepository enrollmentRepository) {
        return args -> {
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        Student.builder().name("Alice Smith").course("Computer Science").build(),
                        Student.builder().name("Bob Johnson").course("Mathematics").build(),
                        Student.builder().name("Charlie Brown").course("Physics").build(),
                        Student.builder().name("Diana Prince").course("Engineering").build(),
                        Student.builder().name("Evan Wright").course("History").build()
                ));
                System.out.println("Loaded 5 dummy student records into the database.");
            }
            if (userRepository.count() == 0) {
                userRepository.save(AppUser.builder()
                        .username("admin")
                        .password("$2a$10$wY1twJwXwM/1z7C.j8c2sOS6gB3.FmBf4VpWk.4kR27jV.M/57XnC") // "password"
                        .role("ROLE_ADMIN")
                        .build());
                System.out.println("Loaded admin user into the database.");
            }
            if (courseRepository.count() == 0) {
                courseRepository.saveAll(List.of(
                        Course.builder().courseCode("CS101").title("Intro to Computer Science").description("Foundational concepts of computing").credits(4).department("Computer Science").build(),
                        Course.builder().courseCode("MATH201").title("Linear Algebra").description("Vector spaces and matrix calculus").credits(3).department("Mathematics").build(),
                        Course.builder().courseCode("PHYS101").title("General Physics I").description("Mechanics, waves, and thermodynamics").credits(4).department("Physics").build(),
                        Course.builder().courseCode("ENG105").title("Academic Writing").description("Critical rhetoric and research methods").credits(3).department("Humanities").build()
                ));
                System.out.println("Loaded 4 dummy course records into the database.");
            }
            if (enrollmentRepository.count() == 0 && repository.count() > 0 && courseRepository.count() > 0) {
                List<Student> students = repository.findAll();
                List<Course> courses = courseRepository.findAll();
                enrollmentRepository.saveAll(List.of(
                        Enrollment.builder()
                                .student(students.get(0))
                                .course(courses.get(0))
                                .status(EnrollmentStatus.ENROLLED)
                                .enrollmentDate(LocalDate.now().minusMonths(1))
                                .grade(null)
                                .build(),
                        Enrollment.builder()
                                .student(students.get(0))
                                .course(courses.get(1))
                                .status(EnrollmentStatus.COMPLETED)
                                .enrollmentDate(LocalDate.now().minusMonths(6))
                                .grade("A")
                                .build(),
                        Enrollment.builder()
                                .student(students.get(1))
                                .course(courses.get(0))
                                .status(EnrollmentStatus.ENROLLED)
                                .enrollmentDate(LocalDate.now().minusWeeks(2))
                                .grade(null)
                                .build()
                ));
                System.out.println("Loaded 3 dummy enrollment records into the database.");
            }
        };
    }
}
