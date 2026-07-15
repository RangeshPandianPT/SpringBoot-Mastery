CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    course VARCHAR(255) NOT NULL
);

CREATE TABLE app_users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Insert some initial data
INSERT INTO students (name, course) VALUES ('John Doe', 'Computer Science');
INSERT INTO students (name, course) VALUES ('Jane Smith', 'Mathematics');

-- Insert a default admin user (password is 'password' encoded with BCrypt)
INSERT INTO app_users (username, password, role) VALUES ('admin', '$2a$10$wY1twJwXwM/1z7C.j8c2sOS6gB3.FmBf4VpWk.4kR27jV.M/57XnC', 'ROLE_ADMIN');
