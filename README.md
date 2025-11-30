# User and Post Management System

REST API для управління користувачами та їхніми постами з використанням Spring Boot, Spring Data JPA та підтримкою різних БД.

## Технології

- Spring Boot 3.2.0
- Spring Data JPA
- PostgreSQL / MySQL / H2
- Java 17

## Структура БД

### Таблиця `users`
- `id` - BIGSERIAL/BIGINT PRIMARY KEY
- `name` - VARCHAR(255) NOT NULL
- `email` - VARCHAR(255) NOT NULL UNIQUE

### Таблиця `posts`
- `id` - BIGSERIAL/BIGINT PRIMARY KEY
- `title` - VARCHAR(255) NOT NULL
- `content` - TEXT NOT NULL
- `user_id` - BIGINT FOREIGN KEY REFERENCES users(id)

## Запуск

### PostgreSQL (за замовчуванням)

```bash
# 1. Запустити PostgreSQL
docker-compose up -d postgres

# 2. Запустити додаток
mvn spring-boot:run
```

### MySQL

```bash
# 1. Запустити MySQL
docker-compose up -d mysql

# 2. Запустити додаток з профілем MySQL
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

### H2 (in-memory, для тестування)

```bash
# Запустити додаток з профілем H2
mvn spring-boot:run -Dspring-boot.run.profiles=h2

# H2 Console доступна за адресою:
# http://localhost:8080/h2-console
# JDBC URL: jdbc:h2:mem:usersdb
# Username: sa
# Password: (порожній)
```

## API Endpoints

### Створення користувача

```http
POST http://localhost:8080/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@gmail.com"
}
```

### Створення користувача з постами

```http
POST http://localhost:8080/users/with-posts
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane@gmail.com",
  "posts": [
    {
      "title": "Перший пост",
      "content": "Контент першого поста"
    },
    {
      "title": "Другий пост",
      "content": "Контент другого поста"
    }
  ]
}
```

### Отримання користувача по імені

```http
GET http://localhost:8080/users/by-name?name=John Doe
```

### Отримання користувачів по email domain

```http
GET http://localhost:8080/users/by-email-domain?domain=@gmail.com
```

### Отримання всіх постів користувача

```http
GET http://localhost:8080/users/{userId}/posts
```

### Отримання всіх користувачів

```http
GET http://localhost:8080/users
```

### Отримання користувача по id

```http
GET http://localhost:8080/users/{id}
```

### Тест rollback транзакції

```http
POST http://localhost:8080/users/test-rollback
Content-Type: application/json

{
  "name": "Test User",
  "email": "test@example.com"
}
```

## Приклади використання

### Створення користувача

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@gmail.com"
  }'
```

### Створення користувача з постами

```bash
curl -X POST http://localhost:8080/users/with-posts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@gmail.com",
    "posts": [
      {"title": "Post 1", "content": "Content 1"},
      {"title": "Post 2", "content": "Content 2"}
    ]
  }'
```

### Отримання користувачів по імені

```bash
curl "http://localhost:8080/users/by-name?name=John%20Doe"
```

### Отримання користувачів по email domain

```bash
curl "http://localhost:8080/users/by-email-domain?domain=@gmail.com"
```

### Отримання постів користувача

```bash
curl http://localhost:8080/users/1/posts
```

### Тест rollback

```bash
curl -X POST http://localhost:8080/users/test-rollback \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com"
  }'
```

## Особливості реалізації

### Іменовані методи в репозиторії

- `findByName(String name)` - знаходить користувачів за ім'ям
- `findByEmailEndingWith(String domain)` - знаходить користувачів по домену email

### Відносини між сутностями

- **User** має One-to-Many відношення до **Post**
- Кожен пост належить одному користувачу
- При видаленні користувача видаляються всі його пости (orphanRemoval = true)

### Транзакції

- Всі операції з БД виконуються в транзакціях
- При помилці відбувається автоматичний rollback
- Метод `test-rollback` демонструє роботу rollback

### Підтримка різних БД

- **PostgreSQL** - за замовчуванням
- **MySQL** - через профіль `mysql`
- **H2** - через профіль `h2` (in-memory для тестування)

## Налаштування БД

Параметри підключення знаходяться в `src/main/resources/application.yml`:

- **PostgreSQL**: `jdbc:postgresql://localhost:5432/users_db`
- **MySQL**: `jdbc:mysql://localhost:3306/users_db`
- **H2**: `jdbc:h2:mem:usersdb`

Для зміни БД використовуйте Spring profiles:
- `mvn spring-boot:run -Dspring-boot.run.profiles=mysql`
- `mvn spring-boot:run -Dspring-boot.run.profiles=h2`
