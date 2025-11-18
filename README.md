# Customer Management System

Проєкт для управління клієнтами з використанням Spring Boot, JdbcTemplate та PostgreSQL.

## Структура проєкту

- `Customer` - клас сутності клієнта
- `CustomerDao` - DAO клас для роботи з базою даних
- `CustomerRowMapper` - маппер для конвертації записів БД в Java-об'єкти
- `DatabaseConfig` - конфігурація бази даних
- `DatabaseInitializer` - автоматичне створення таблиці при старті

## Запуск

### 1. Запустити PostgreSQL в Docker

```bash
cd docker
docker-compose up -d
```

### 2. Створити таблицю вручну (опціонально)

Якщо автоматична ініціалізація не спрацює, можна створити таблицю вручну:

```sql
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    social_security_number VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### 3. Запустити додаток

```bash
mvn spring-boot:run
```

## Використання CustomerDao

```java
@Autowired
private CustomerDao customerDao;

// Додавання клієнта
Customer customer = new Customer("Іван Петренко", "ivan@example.com", "1234567890");
Customer saved = customerDao.add(customer);

// Пошук по id
Optional<Customer> found = customerDao.findById(1L);

// Оновлення
customer.setFullName("Іван Іванович Петренко");
customerDao.update(customer);

// Видалення
customerDao.deleteById(1L);

// Отримання всіх
List<Customer> allCustomers = customerDao.findAll();
```

## Налаштування

Параметри підключення до БД знаходяться в `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/my_database
spring.datasource.username=myuser
spring.datasource.password=mypassword
```
