-- liquibase formatted sql
-- changeset admin:1
CREATE SCHEMA IF NOT EXISTS policy_management;

-- changeset admin:2
CREATE TABLE policy_management.roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- changeset admin:3
INSERT INTO policy_management.roles (id, name)
SELECT 1, 'ROLE_USER' FROM DUAL 
WHERE NOT EXISTS (SELECT 1 FROM policy_management.roles WHERE id = 1);

-- changeset admin:4
INSERT INTO policy_management.roles (id, name)
SELECT 2, 'ROLE_ADMIN' FROM DUAL 
WHERE NOT EXISTS (SELECT 1 FROM policy_management.roles WHERE id = 2);


-- changeset admin:5
CREATE TABLE policy_management.users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- changeset admin:6
CREATE TABLE policy_management.user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) 
        REFERENCES policy_management.users(id),
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) 
        REFERENCES policy_management.roles(id)
);

-- changeset admin:7
CREATE TABLE policy_management.policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    premium_amount DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    CONSTRAINT fk_policies_created_by FOREIGN KEY (created_by) 
        REFERENCES policy_management.users(id)
);
