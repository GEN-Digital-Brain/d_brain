CREATE TABLE students (
    student_id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    teacher_name VARCHAR(255) NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    first_semester_grade DECIMAL(3,1) NOT NULL,
    second_semester_grade DECIMAL(3,1) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_name_not_blank CHECK (LENGTH(name) > 0),
    CONSTRAINT chk_age_not_null CHECK (age IS NOT NULL),
    CONSTRAINT chk_teacher_name_not_blank CHECK (LENGTH(teacher_name) > 0),
    CONSTRAINT chk_room_number_not_blank CHECK (LENGTH(room_number) > 0),
    CONSTRAINT chk_first_semester_grade_not_null CHECK (first_semester_grade IS NOT NULL),
    CONSTRAINT chk_second_semester_grade_not_null CHECK (second_semester_grade IS NOT NULL)
);