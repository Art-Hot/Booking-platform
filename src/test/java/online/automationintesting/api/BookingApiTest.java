package online.automationintesting.api;

import io.restassured.response.Response;
import online.automationintesting.models.Booking;
import online.automationintesting.models.ContactMessage;
import online.automationintesting.models.Room;
import online.automationintesting.utils.DateUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("API тесты: бронирование, комнаты, сообщения")
public class BookingApiTest extends BaseApiTest {

    private final BookingClient bookingClient = new BookingClient();

    @Test
    @DisplayName("Получение токена авторизации")
    void shouldGetAuthToken() {
        String token = bookingClient.getAuthToken();

        assertThat(token)
                .as("Токен не должен быть пустым")
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    @DisplayName("Получение списка всех комнат")
    void shouldGetAllRooms() {
        List<Room> rooms = bookingClient.getAllRooms();

        assertThat(rooms)
                .as("Список комнат не должен быть пустым")
                .isNotEmpty();

        rooms.forEach(room -> {
            assertThat(room.getRoomId()).as("ID комнаты должен быть положительным").isPositive();
            assertThat(room.getRoomName()).as("Номер комнаты должен быть положительным").isNotEmpty();
            assertThat(room.getType()).as("Тип комнаты не должен быть пустым").isNotBlank();
        });
    }

    @Test
    @DisplayName("Успешное создание бронирования")
    void shouldCreateBooking() {
        String token = bookingClient.getAuthToken();
        List<Room> rooms = bookingClient.getAllRooms();

        assertThat(rooms).isNotEmpty();
        Integer roomId = rooms.get(0).getRoomId();

        Booking booking = Booking.builder()
                .roomid(roomId)
                .firstname("John")
                .lastname("Doe")
                .depositpaid(true)
                .bookingdates(Booking.BookingDates.builder()
                        .checkin("2025-06-01")
                        .checkout("2025-06-05")
                        .build())
                .totalprice(250)
                .build();

        Response response = bookingClient.createBookingWithToken(booking, token);

        assertThat(response.getStatusCode())
                .as("Статус создания бронирования должен быть 200")
                .isIn(200, 201);

        Integer bookingId = response.jsonPath().getInt("bookingid");
        assertThat(bookingId)
                .as("ID бронирования должен быть положительным")
                .isPositive();

        Response getResponse = bookingClient.getBookingById(bookingId, token);
        assertThat(getResponse.getStatusCode()).isEqualTo(200);
        assertThat(getResponse.jsonPath().getString("firstname")).isEqualTo("John");
        assertThat(getResponse.jsonPath().getString("lastname")).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Создание бронирования без обязательного поля lastname")
    void shouldFailCreateBookingWithoutLastName() {
        String token = bookingClient.getAuthToken();

        Booking booking = Booking.builder()
                .roomid(1)
                .firstname("John")
                // lastname отсутствует
                .depositpaid(true)
                .bookingdates(Booking.BookingDates.builder()
                        .checkin("2025-06-01")
                        .checkout("2025-06-05")
                        .build())
                .totalprice(250)
                .build();

        Response response = bookingClient.createBookingWithToken(booking, token);

        assertThat(response.getStatusCode())
                .as("Ожидается статус 400 при отсутствии обязательного поля")
                .isEqualTo(400);
    }

    @Test
    @DisplayName("Отправка контактного сообщения")
    void shouldSendContactDiscription() {
        ContactMessage message = ContactMessage.builder()
                .name("Тестовый Пользователь")
                .email("test@example.com")
                .phone("79990001122")
                .subject("Тест API")
                .message("Это тестовое сообщение, отправленное через API.")
                .build();

        Response response = bookingClient.sendContactMessage(message);

        assertThat(response.getStatusCode())
                .as("Сообщение должно успешно отправляться")
                .isIn(200, 201);
    }

    @Test
    @DisplayName("Обновление и удаление бронирования")
    void shouldUpdateAndDeleteBooking() {
        String token = bookingClient.getAuthToken();
        List<Room> rooms = bookingClient.getAllRooms();

        if (rooms.isEmpty()) {
            throw new IllegalStateException("Нет доступных для тестирования комнат");
        }

        Integer roomId = rooms.get(0).getRoomId();
        String[] dates = DateUtils.uniqueBookingDates();

        Booking booking = Booking.builder()
                .roomid(roomId)
                .firstname("Alice")
                .lastname("Smith")
                .depositpaid(true)
                .bookingdates(Booking.BookingDates.builder()
                        .checkin(dates[0])
                        .checkout(dates[1])
                        .build())
                .totalprice(250)
                .build();

        Response createResponse = bookingClient.createBookingWithToken(booking, token);

        assertThat(createResponse.getStatusCode())
                .as("Статус создания должен быть успешным")
                .isIn(200, 201);
        assertThat(createResponse.asString())
                .as("Тело ответа на создание не должно быть пустым")
                .isNotBlank();
        Integer bookingId = createResponse.jsonPath().getInt("bookingid");
        assertThat(bookingId)
                .as("ID брони должен существовать и быть положительным")
                .isNotNull()
                .isPositive();

        Response getResponse = bookingClient.getBookingById(bookingId, token);

        assertThat(bookingId).as("ID брони должен существовать").isNotNull();

        String[] updateDates = DateUtils.uniqueBookingDates();

        Booking updatedBooking = Booking.builder()
                .roomid(roomId)
                .firstname("Alice")
                .lastname("Johnson")
                .depositpaid(false)
                .bookingdates(Booking.BookingDates.builder()
                        .checkin(updateDates[0])
                        .checkout(updateDates[1])
                        .build())
                .totalprice(200)
                .build();

        Response updateResponse = bookingClient.updateBooking(bookingId, updatedBooking, token);

        assertThat(updateResponse.getStatusCode())
                .as("Обновление должно проходить успешно")
                .isIn(200, 201);

        getResponse = bookingClient.getBookingById(bookingId, token);

        assertThat(getResponse.getStatusCode()).isEqualTo(200);
        assertThat(getResponse.asString())
                .as("ответ GET после обновления не должен быть пустым")
                .isNotBlank();

        assertThat(getResponse.jsonPath().getString("lastname")).isEqualTo("Johnson");
        assertThat(getResponse.jsonPath().getBoolean("depositpaid")).isFalse();

        Response deleteResponse = bookingClient.deleteBooking(bookingId, token);
        assertThat(deleteResponse.getStatusCode())
                .as("Удаление должно проходить успешно")
                .isIn(200, 202, 204);

        Response afterDelete = bookingClient.getBookingById(bookingId, token);
        assertThat(afterDelete.getStatusCode())
                .as("Удалённое бронирование должно возвращать 404")
                .isEqualTo(404);
    }

    @Test
    @DisplayName("Валидация: создание бронирования с датой выезда раньше заезда")
    void shouldFailBookingWithInvalidDates() {
        String token = bookingClient.getAuthToken();
        List<Room> rooms = bookingClient.getAllRooms();
        Integer roomId = rooms.get(0).getRoomId();

        Booking booking = Booking.builder()
                .roomid(roomId)
                .firstname("Bob")
                .lastname("Brown")
                .depositpaid(true)
                .bookingdates(Booking.BookingDates.builder()
                        .checkin("2025-06-10")
                        .checkout("2025-06-05") // раньше заезда
                        .build())
                .totalprice(100)
                .build();

        Response response = bookingClient.createBookingWithToken(booking, token);

        assertThat(response.getStatusCode())
                .as("Ожидается ошибка при неверных датах")
                .isEqualTo(409);
    }
}