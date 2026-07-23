$ErrorActionPreference = "Stop"

git add pom.xml
git commit -m "build: Add Lombok, OpenAPI, and Actuator dependencies"

git add src/main/resources/application.properties
git commit -m "config: Configure Actuator endpoints and OpenAPI paths"

git add src/main/java/dev/rangesh/HelloWorld/model/Student.java
git commit -m "refactor: Optimize Student entity with Lombok annotations"

git add src/main/java/dev/rangesh/HelloWorld/dto/StudentDto.java
git commit -m "refactor: Optimize StudentDto with Lombok annotations"

git add src/main/java/dev/rangesh/HelloWorld/config/DataLoader.java
git commit -m "feat: Add DataLoader for initial database seeding"

git add src/main/java/dev/rangesh/HelloWorld/service/StudentService.java
git commit -m "feat: Implement pagination support in StudentService"

git add src/main/java/dev/rangesh/HelloWorld/controller/StudentController.java
git commit -m "feat: Implement pagination support in StudentController"

git add src/test/java/dev/rangesh/HelloWorld/service/StudentServiceTest.java
git commit -m "test: Add comprehensive unit tests for StudentService"

git add src/test/java/dev/rangesh/HelloWorld/controller/StudentControllerTest.java
git commit -m "test: Add integration tests for StudentController using MockMvc"

git push
