CREATE TABLE enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES students(id) ON DELETE CASCADE,
    course_id BIGINT NOT NULL REFERENCES courses(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'ENROLLED',
    enrollment_date DATE NOT NULL,
    grade VARCHAR(10),
    CONSTRAINT uk_student_course UNIQUE (student_id, course_id)
);

CREATE INDEX idx_enrollments_student ON enrollments(student_id);
CREATE INDEX idx_enrollments_course ON enrollments(course_id);

-- Seed initial enrollments
INSERT INTO enrollments (student_id, course_id, status, enrollment_date, grade)
VALUES
    (1, 1, 'ENROLLED', '2026-09-01', NULL),
    (1, 2, 'COMPLETED', '2026-01-15', 'A'),
    (2, 2, 'ENROLLED', '2026-09-01', NULL),
    (2, 4, 'ENROLLED', '2026-09-01', NULL);

