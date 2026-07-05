package dev.rangesh.HelloWorld.config;

import dev.rangesh.HelloWorld.model.Student;
import dev.rangesh.HelloWorld.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(StudentRepository repository) {
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
        };
    }
}
