# Order Management System

REST API для управління замовленнями з використанням Spring Boot.

## Структура проєкту

- `Product` - клас товару (id, name, cost)
- `Order` - клас замовлення (id, creationDate, totalCost, products)
- `OrderRepository` - репозиторій для зберігання замовлень в пам'яті
- `PingController` - контролер для перевірки роботи додатку
- `OrderController` - REST контролер для роботи з замовленнями

## Запуск

```bash
mvn spring-boot:run
```

Додаток буде доступний за адресою: http://localhost:8080

## API Endpoints

### 1. Ping (перевірка роботи)

```http
GET http://localhost:8080/ping
```

**Відповідь:**
```
OK
```

### 2. Отримання всіх замовлень

```http
GET http://localhost:8080/orders
```

**Відповідь:**
```json
[
  {
    "id": 1,
    "creationDate": "2025-11-16T10:30:00",
    "totalCost": 25800.0,
    "products": [
      {
        "id": 1,
        "name": "Ноутбук",
        "cost": 25000.0
      },
      {
        "id": 2,
        "name": "Миша",
        "cost": 800.0
      }
    ]
  }
]
```

### 3. Отримання конкретного замовлення

```http
GET http://localhost:8080/orders/{id}
```

**Приклад:**
```http
GET http://localhost:8080/orders/1
```

**Відповідь:**
```json
{
  "id": 1,
  "creationDate": "2025-11-16T10:30:00",
  "totalCost": 25800.0,
  "products": [
    {
      "id": 1,
      "name": "Ноутбук",
      "cost": 25000.0
    }
  ]
}
```

### 4. Додавання нового замовлення

```http
POST http://localhost:8080/orders
Content-Type: application/json

{
  "products": [
    {
      "id": 5,
      "name": "Клавіатура",
      "cost": 2000.0
    },
    {
      "id": 6,
      "name": "Миша",
      "cost": 800.0
    }
  ]
}
```

**Відповідь:**
```json
{
  "id": 3,
  "creationDate": "2025-11-18T16:55:00",
  "totalCost": 2800.0,
  "products": [
    {
      "id": 5,
      "name": "Клавіатура",
      "cost": 2000.0
    },
    {
      "id": 6,
      "name": "Миша",
      "cost": 800.0
    }
  ]
}
```

## Тестування через curl

```bash
# Перевірка роботи
curl http://localhost:8080/ping

# Отримання всіх замовлень
curl http://localhost:8080/orders

# Отримання замовлення по id
curl http://localhost:8080/orders/1

# Додавання нового замовлення
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "products": [
      {"id": 5, "name": "Клавіатура", "cost": 2000.0},
      {"id": 6, "name": "Миша", "cost": 800.0}
    ]
  }'
```

## Особливості

- Замовлення зберігаються в пам'яті (без БД)
- При додаванні замовлення автоматично:
  - Генерується id
  - Встановлюється дата створення
  - Обчислюється загальна вартість (якщо не вказана)
- Репозиторій ініціалізується з двома тестовими замовленнями
