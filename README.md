# Registration and Login System with Roles

Система реєстрації та автентифікації користувачів з підтримкою ролей.

## Структура бази даних

### Таблиця users
```sql
CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255),
    phone VARCHAR(255),
    name VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);
```

### Таблиця roles
```sql
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);
```

### Таблиця user_roles (Many-to-Many)
```sql
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
```

## SQL скрипти

Проєкт містить SQL скрипти для MySQL:

- `schema-mysql.sql` - створення таблиць для MySQL
- `data-mysql.sql` - ініціалізація даних (ролі USER та ADMIN)

## Запуск проєкту

### MySQL

```bash
# Запустити MySQL
docker-compose up -d mysql

# Запустити додаток
mvn spring-boot:run
```

## Налаштування БД

SQL скрипти автоматично виконуються при старті додатку через `spring.sql.init.mode=always`.

### Створення таблиць вручну (опціонально)

**MySQL:**
```bash
docker exec -i java-mysql-1 mysql -uroot -proot users_db < src/main/resources/schema-mysql.sql
docker exec -i java-mysql-1 mysql -uroot -proot users_db < src/main/resources/data-mysql.sql
```

## Entities

### User
- `id` - унікальний ідентифікатор
- `name` - ім'я користувача
- `email` - email (унікальний)
- `phone` - телефон
- `password` - хешований пароль
- `roles` - набір ролей (Many-to-Many)

### Role
- `id` - унікальний ідентифікатор
- `name` - назва ролі (унікальна)
- `users` - набір користувачів з цією роллю

## Репозиторії

- `UserRepository` - CRUD операції для користувачів
- `RoleRepository` - CRUD операції для ролей

## Примітки

- Таблиці створюються автоматично через Hibernate (`ddl-auto: update`)
- SQL скрипти виконуються для ініціалізації базових ролей (USER, ADMIN)
- Зв'язок Many-to-Many між User та Role через таблицю user_roles
- Каскадне видалення при видаленні користувача або ролі
