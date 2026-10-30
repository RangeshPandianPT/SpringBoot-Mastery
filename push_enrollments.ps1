$ErrorActionPreference = "Stop"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/model/EnrollmentStatus.java
git commit -m "feat(model): add EnrollmentStatus enum"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/model/Enrollment.java
git commit -m "feat(model): add Enrollment entity with Student and Course relations"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/dto/EnrollmentRequestDto.java
git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/dto/EnrollmentResponseDto.java
git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/dto/EnrollmentGradeDto.java
git commit -m "feat(dto): add Enrollment request, response, and grade DTOs with validation"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/repository/EnrollmentRepository.java
git commit -m "feat(repository): add EnrollmentRepository with custom queries"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/service/EnrollmentService.java
git commit -m "feat(service): add EnrollmentService with grading, caching, and transactions"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/controller/EnrollmentController.java
git commit -m "feat(controller): add EnrollmentController REST endpoints with Swagger annotations"

git add HelloWorld/src/main/resources/db/migration/V3__Create_Enrollments_Table.sql
git commit -m "db: add Flyway V3 migration for enrollments table"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/config/DataLoader.java
git commit -m "feat(config): seed sample enrollments in DataLoader on startup"

git add HelloWorld/src/test/java/dev/rangesh/HelloWorld/service/EnrollmentServiceTest.java
git commit -m "test(service): add EnrollmentServiceTest with 18 unit tests"

git push origin main

