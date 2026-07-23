$ErrorActionPreference = "Stop"

git add HelloWorld\pom.xml
git commit -m "build: Add dependencies for Security, PostgreSQL, Flyway, Cache"

git add HelloWorld\docker-compose.yml
git commit -m "infra: Add docker-compose for PostgreSQL database"

git add HelloWorld\src\main\resources\application.properties
git commit -m "config: Update active profile and add JWT properties"

git add HelloWorld\src\main\resources\application-dev.properties
git commit -m "config: Add dev properties for PostgreSQL and Flyway"

git add HelloWorld\src\main\resources\db\migration\V1__Init_Schema.sql
git commit -m "db: Add initial Flyway migration script"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\model\AppUser.java
git commit -m "feat: Add AppUser entity for authentication"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\repository\AppUserRepository.java
git commit -m "feat: Add AppUserRepository for user data access"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\dto\AuthRequest.java
git commit -m "feat: Add AuthRequest DTO for login"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\dto\AuthResponse.java
git commit -m "feat: Add AuthResponse DTO for JWT token response"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\security\CustomUserDetails.java
git commit -m "security: Add CustomUserDetails implementation"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\security\CustomUserDetailsService.java
git commit -m "security: Add CustomUserDetailsService for loading users"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\security\JwtUtil.java
git commit -m "security: Add JwtUtil for token generation and validation"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\security\JwtAuthenticationFilter.java
git commit -m "security: Add JwtAuthenticationFilter for securing endpoints"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\security\SecurityConfig.java
git commit -m "security: Configure Spring Security and define access rules"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\controller\AuthController.java
git commit -m "feat: Add AuthController for authentication endpoints"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\HelloWorldApplication.java
git commit -m "config: Enable caching in main application class"

git add HelloWorld\src\main\java\dev\rangesh\HelloWorld\service\StudentService.java
git commit -m "perf: Add caching annotations to StudentService"

git push
