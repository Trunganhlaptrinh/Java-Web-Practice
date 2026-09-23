-- Personal Expense Manager - Level 1 database

DROP DATABASE IF EXISTS personal_expense_manager;
CREATE DATABASE personal_expense_manager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE personal_expense_manager;
SET NAMES utf8mb4;

CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_category_name_type UNIQUE (name, type)
);

CREATE TABLE payment_methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount BIGINT NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL,
    description VARCHAR(255) NOT NULL,
    transaction_date DATE NOT NULL,
    category_id INT NULL,
    payment_method_id INT NULL,
    note TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT chk_transaction_amount_positive CHECK (amount > 0),
    CONSTRAINT fk_transaction_category
        FOREIGN KEY (category_id) REFERENCES categories(id)
        ON DELETE SET NULL,
    CONSTRAINT fk_transaction_payment_method
        FOREIGN KEY (payment_method_id) REFERENCES payment_methods(id)
        ON DELETE SET NULL,
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_transaction_type (type),
    INDEX idx_transaction_category (category_id),
    INDEX idx_transaction_payment_method (payment_method_id)
);

INSERT INTO categories (name, type) VALUES
    ('Lương', 'INCOME'),
    ('Thưởng', 'INCOME'),
    ('Đầu tư', 'INCOME'),
    ('Ăn uống', 'EXPENSE'),
    ('Di chuyển', 'EXPENSE'),
    ('Mua sắm', 'EXPENSE');

INSERT INTO payment_methods (name) VALUES
    ('Tiền mặt'),
    ('Chuyển khoản'),
    ('Thẻ ngân hàng'),
    ('Ví điện tử');

INSERT INTO transactions (
    amount, type, description, transaction_date, category_id, payment_method_id, note
) VALUES
    (15000000, 'INCOME', 'Nhận lương tháng', CURRENT_DATE, 1, NULL, 'Lương tháng hiện tại'),
    (65000, 'EXPENSE', 'Ăn trưa', CURRENT_DATE, 4, 1, 'Cơm trưa');
