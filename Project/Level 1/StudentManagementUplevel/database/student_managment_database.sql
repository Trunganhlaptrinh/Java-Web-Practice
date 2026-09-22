-- ============================================
-- Student Course Management - DB script (Level 1)
-- ============================================

DROP DATABASE IF EXISTS student_course_management;
CREATE DATABASE student_course_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE student_course_management;

-- ============================================
-- Bang goc: departments
-- ============================================
CREATE TABLE departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================
-- Bang teachers: 1 department co nhieu teacher
-- ============================================
CREATE TABLE teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department_id INT NOT NULL,
    CONSTRAINT fk_teacher_department
        FOREIGN KEY (department_id) REFERENCES departments(id)
        ON DELETE RESTRICT
);

-- ============================================
-- Bang courses: 1 department co nhieu course
-- ============================================
CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    department_id INT NOT NULL,
    CONSTRAINT fk_course_department
        FOREIGN KEY (department_id) REFERENCES departments(id)
        ON DELETE RESTRICT
);

-- ============================================
-- Bang trung gian N-N: course <-> teacher
-- 1 course co the nhieu teacher day, 1 teacher day nhieu course
-- ============================================
CREATE TABLE course_teacher (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    teacher_id INT NOT NULL,
    CONSTRAINT fk_ct_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_ct_teacher
        FOREIGN KEY (teacher_id) REFERENCES teachers(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_course_teacher UNIQUE (course_id, teacher_id)
);

-- ============================================
-- Bang students
-- ============================================
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    semester VARCHAR(20) NOT NULL
);

-- ============================================
-- Bang trung gian N-N: student <-> course (dang ky mon)
-- ============================================
CREATE TABLE enrollments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    enrolled_date DATE NOT NULL,
    CONSTRAINT fk_enroll_student
        FOREIGN KEY (student_id) REFERENCES students(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_enroll_course
        FOREIGN KEY (course_id) REFERENCES courses(id)
        ON DELETE RESTRICT,
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id)
);

-- ============================================
-- Du lieu mau de test
-- ============================================
INSERT INTO departments (name) VALUES
    ('Computer Science'),
    ('Business');

INSERT INTO teachers (name, department_id) VALUES
    ('Nguyen Van A', 1),
    ('Tran Thi B', 1),
    ('Le Van C', 2);

INSERT INTO courses (name, department_id) VALUES
    ('Java Programming', 1),
    ('Database Systems', 1),
    ('Marketing 101', 2);

INSERT INTO course_teacher (course_id, teacher_id) VALUES
    (1, 1), -- Java Programming - Nguyen Van A
    (1, 2), -- Java Programming - Tran Thi B (2 teacher cung day 1 mon)
    (2, 2), -- Database Systems - Tran Thi B
    (3, 3); -- Marketing 101 - Le Van C

INSERT INTO students (name, semester) VALUES
    ('Pham Thi D', 'HK1 2026'),
    ('Hoang Van E', 'HK1 2026');

INSERT INTO enrollments (student_id, course_id, enrolled_date) VALUES
    (1, 1, '2026-09-01'),
    (1, 2, '2026-09-01'),
    (2, 1, '2026-09-02');