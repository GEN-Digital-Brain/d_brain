CREATE TABLE IF NOT EXISTS students (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    age INT NOT NULL CHECK (age > 0),
    first_semester_grade DECIMAL(3,1) NOT NULL CHECK (first_semester_grade <= 10.0),
    second_semester_grade DECIMAL(3,1) NOT NULL CHECK (second_semester_grade <= 10.0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Validation rules
    CONSTRAINT chk_student_name_not_blank CHECK (LENGTH(name) >= 3),
    CONSTRAINT chk_student_email_not_blank CHECK (LENGTH(email) >= 3),
    CONSTRAINT chk_first_semester_grade_range CHECK (first_semester_grade >= 0 AND first_semester_grade <= 10),
    CONSTRAINT chk_second_semester_grade_range CHECK (second_semester_grade >= 0 AND second_semester_grade <= 10)
);

CREATE TABLE IF NOT EXISTS classes (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    instructor VARCHAR(255) NOT NULL,
    student_id BINARY(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Validation rules
    CONSTRAINT chk_class_name_not_blank CHECK (LENGTH(name) >= 3),
    CONSTRAINT chk_instructor_not_blank CHECK (LENGTH(instructor) > 0),
    
    -- Foreign key for the student (1:N)
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS employees (
    employee_id BINARY(16) PRIMARY KEY,
    employee_name VARCHAR(255) NOT NULL,
    employee_email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    position VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);