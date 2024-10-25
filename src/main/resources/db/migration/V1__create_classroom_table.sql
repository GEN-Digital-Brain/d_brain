CREATE TABLE classes (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    instructor VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Constraints de validação
    CONSTRAINT chk_class_name_not_blank CHECK (LENGTH(name) >= 3),
    CONSTRAINT chk_instructor_not_blank CHECK (LENGTH(instructor) > 0)
);
