$ErrorActionPreference = "Stop"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/model/Course.java
git commit -m "feat(model): add Course entity with JPA mapping"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/dto/CourseDto.java
git commit -m "feat(dto): add CourseDto with Bean Validation constraints"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/repository/CourseRepository.java
git commit -m "feat(repository): add CourseRepository with custom query methods"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/service/CourseService.java
git commit -m "feat(service): add CourseService with caching and transactional support"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/controller/CourseController.java
git commit -m "feat(controller): add CourseController REST endpoints with Swagger annotations"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/config/OpenApiConfig.java
git commit -m "config: add OpenApiConfig with JWT Bearer security scheme"

git add HelloWorld/src/main/resources/db/migration/V2__Create_Courses_Table.sql
git commit -m "db: add Flyway V2 migration for courses table"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/config/DataLoader.java
git commit -m "feat(config): seed sample courses in DataLoader on startup"

git add HelloWorld/src/main/java/dev/rangesh/HelloWorld/exception/GlobalExceptionHandler.java
git commit -m "fix(exception): handle IllegalArgumentException with 400 Bad Request"

git add HelloWorld/src/test/java/dev/rangesh/HelloWorld/service/CourseServiceTest.java
git commit -m "test(service): add CourseServiceTest with 8 unit tests"

git push origin main
    