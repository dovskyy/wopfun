CREATE TABLE IF NOT EXISTS "group" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS child (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE,
    group_id BIGINT,
    diagnosis TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_child_group FOREIGN KEY (group_id) REFERENCES "group" (id)
);

CREATE TABLE IF NOT EXISTS child_note (
    id BIGSERIAL PRIMARY KEY,
    child_id BIGINT NOT NULL,
    note_date DATE NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_child_note_child FOREIGN KEY (child_id) REFERENCES child (id)
);

CREATE TABLE IF NOT EXISTS generated_document (
    id BIGSERIAL PRIMARY KEY,
    child_id BIGINT NOT NULL,
    doc_type VARCHAR(255) NOT NULL,
    generated_at TIMESTAMP NOT NULL,
    generated_by VARCHAR(255),
    ai_strengths TEXT,
    ai_recommendations TEXT,
    ai_goals TEXT,
    file_path VARCHAR(255),
    status VARCHAR(255) NOT NULL,
    CONSTRAINT fk_generated_document_child FOREIGN KEY (child_id) REFERENCES child (id)
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    role VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);
