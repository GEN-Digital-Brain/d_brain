CREATE TABLE classes (
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