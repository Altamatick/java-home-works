-- SQL скрипт для ініціалізації даних (MySQL)
-- Додавання базових ролей

-- Додавання ролей (якщо їх ще немає)
INSERT IGNORE INTO roles (name) VALUES ('USER');
INSERT IGNORE INTO roles (name) VALUES ('ADMIN');
