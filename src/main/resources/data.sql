-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

-- Create tasks table
CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    due_date DATE,
    status ENUM('TODO', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'TODO',
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert sample data
INSERT IGNORE INTO users (username, password, email) VALUES 
('admin', '$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoQP91oGcgLwpnvCzTBFZxuuL8iC', 'admin@taskmanager.com');

INSERT IGNORE INTO tasks (title, description, due_date, status, user_id) VALUES
('Complete project', 'Finish the task manager application', '2023-12-31', 'IN_PROGRESS', 1),
('Learn Spring Boot', 'Study Spring Security and JWT', '2023-11-30', 'TODO', 1);