CREATE TABLE courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    credits INT NOT NULL,
    department VARCHAR(100) NOT NULL
);

-- Seed initial courses
INSERT INTO courses (course_code, title, description, credits, department)
VALUES
    ('CS101', 'Intro to Computer Science', 'Foundational concepts of computing and algorithms', 4, 'Computer Science'),
    ('MATH201', 'Linear Algebra', 'Vector spaces, matrices, and linear transformations', 3, 'Mathematics'),
    ('PHYS101', 'General Physics I', 'Mechanics, wave motion, and thermodynamics', 4, 'Physics'),
    ('ENG105', 'Academic Writing', 'Critical reading, rhetoric, and research writing', 3, 'Humanities');
