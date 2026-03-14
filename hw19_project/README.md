# Система бронирования столиков в ресторане

Учебный проект на **Java + Spring Boot + PostgreSQL**: регистрация/вход пользователя и бронирование столиков на фиксированный интервал **3 часа** с защитой от конкурентных бронирований на уровне БД.

## Возможности

- Регистрация и вход пользователя
- Создание брони: **указывается только время начала**, окончание рассчитывается как `start + 3h`
- Подбор столика на стороне сервиса:
    - фильтр по вместимости (`capacity >= persons`)
    - фильтр по занятости на интервале `[start, end)` (пересечения запрещены)
    - выбор **подходящей вместимости**
- Просмотр списка «мои брони»
- Отмена брони (status = `CANCELLED`), после отмены слот освобождается
- Валидации:
    - `startTime` в будущем
    - часы работы: **12:00–24:00** (последнее начало = **21:00**)
    - максимальное количество гостей: `8`

## Технологии

- Java, Spring Boot (Web, Data JPA, Security, Validation)
- PostgreSQL
- Flyway миграции
- Docker для локального запуска Postgres
- Тестирование: JUnit 5, AssertJ, Mockito, Testcontainers (Postgres)

## Модель данных

Таблицы:

- `users(id, login, password, created_at)`
- `tables(id, capacity)`
- `reservations(id, user_id, guest_name, guest_phone, table_id, persons, status, start_time, end_time, time_slot)`

## Запуск (Docker + локально)

### 1) Запустить PostgreSQL в Docker

В проекте есть команда (см. `docker/runDb.src`):

```bash
docker run --rm --name pg-docker \
  -e POSTGRES_PASSWORD=pwd \
  -e POSTGRES_USER=usr \
  -e POSTGRES_DB=demoDB \
  -p 5430:5432 \
  postgres:12
```

Проверка подключения (опционально):

```bash
psql "postgresql://usr:pwd@localhost:5430/demoDB"
```

### 2) Запустить приложение локально

Приложение поднимется на: `http://localhost:8080`

### 3) Зайти в веб-интерфейс

Тестовые пользователи (создаются в `SeedDataRunner` в профилях `dev,test`):

- `user1 / pwd123`
- `user2 / pwd321`

## API

### Auth

- `POST /api/auth/register` — регистрация
- `POST /api/auth/login` — вход (создаёт сессию)
- `GET /api/auth/profile` — текущий пользователь
- `POST /logout` — выход

### Reservations

- `POST /api/reservation` — создать бронь
- `GET /api/reservation` — список броней текущего пользователя
- `POST /api/reservation/{id}/cancel` — отменить бронь

Пример запроса создания брони:

```http
POST /api/reservation
Content-Type: application/json

{
  "guestName": "Anna",
  "guestPhone": "+79990000000",
  "persons": 4,
  "startTime": "2026-03-02T12:00"
}
```

## Тесты

- unit-тесты сервисов/контроллеров
- интеграционные тесты на Testcontainers (Postgres)
- сценарии конкурентных бронирований (несколько потоков одновременно)