# Автотесты для сервиса Restful Booker Platform

Фреймворк для автоматизированного тестирования веб-приложения бронирования отеля -Restful Booker Platform Demo. Проект построен на паттерне проектирования POM и реализует в себе гибридный подход: UI-тесты на Selenide и API-тесты на Rest Assured в одном проекте.

## Стек технологий

Java 17, Maven, JUnit 5, Selenide 7.0.4, Rest Assured 5.3.2, AssertJ, Lombok, Jackson, Owner.

## Структура проекта
````
├── +---src
├── |   +---main
├── |   |   +---java
├── |   |   |   \---online
├── |   |   |       \---automationintesting
├── |   |   |           +---api                        
├── |   |   |           |       BookingClient.java      # http- клиент
├── |   |   |           |       Specs.java              # спецификация запросов
├── |   |   |           +---config
├── |   |   |           |       ProjectConfig.java      # интерфейс централизованного управления настройками тестов
├── |   |   |           |
├── |   |   |           +---models
├── |   |   |           |       Booking.java            # модель бронирования
├── |   |   |           |       BookingAuth.java        # модель авторизации
├── |   |   |           |       ContactMessage.java     # модель отправки сообщения в службу тех поддержки
├── |   |   |           |       Room.java               # модель структуры данных номера
├── |   |   |           |
├── |   |   |           \---pages
├── |   |   |                   AdminPage.java          # страница администратора
├── |   |   |                   MainPage.java           # главная страница
├── |   |   |
├── |   |   \---resources
├── |   |           config.properties                   # конфигурация базовых параметров окружения
├── |   |
├── |   \---test
├── |       +---java
├── |       |   |   Example.java
├── |       |   |
├── |       |   \---online
├── |       |       \---automationintesting
├── |       |           +---api
├── |       |           |       BaseApiTest.java         # общая настройка библиотеки
├── |       |           |       BookingApiTest.java      # класс api тестов
├── |       |           |
├── |       |           +---ui
├── |       |           |       BaseUITest.java          # настройка браузера перед запуском
├── |       |           |       ContactFormTest.java     # класс отправки контактной форны
├── |       |           |
├── |       |           \---utils
├── |       |                   DateUtils.java           # генерация уникальных дат для тестовых данных
├── |       |
├── |       \---resources
├── |               logback.xml                          # настройка отображения лггов консоли


## Архитектурные решения

**Изоляция тестов.** В BaseUITest после каждого теста автоматически очищаются cookies и localStorage. Это гарантирует чистое состояние браузера перед следующим тестом.

**Разделение методов входа.** В AdminPage два отдельных метода: login() для позитивных сценариев и loginWithInvalidCredentials() для негативных. Это исключает ложные ошибки в логах при успешном входе.

**Умная навигация.** Метод goToAdminPage() в MainPage автоматически обрабатывает активную сессию: если пользователь уже авторизован, выполняется Logout перед возвращением объекта AdminPage.

**API Service Layer.** Все HTTP-запросы инкапсулированы в BookingClient. Тесты работают с бизнес-объектами, а не сырыми JSON. Спецификации запросов и ответов вынесены в отдельный класс Specs.

## Запуск

mvn clean test — все тесты;
mvn test -Dtest="ContactFormTest" — только UI;
mvn test -Dtest="BookingApiTest" — только API;
mvn test -Dtest="BookingApiTest#shouldCreateBooking" — конкретный метод.

## Конфигурация

Настройки хранятся в src/test/resources/config.properties:

baseUrl — URL приложения;
apiBaseUrl — базовый URL API;
adminUsername / adminPassword — учетные данные админа;
browser — chrome browserSize — 1920x1080 timeout — 10000 headless — true.

## Отчеты и отладка

Скриншоты и HTML-страница сохраняются автоматически при падении теста в build/reports/tests/. API-логи выводятся в консоль только при ошибках валидации. UI-логи содержат селекторы и значения взаимодействий.
