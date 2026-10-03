package online.automationintesting.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public final class DateUtils {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateUtils() {}

    public static String[] uniqueBookingDates() {
        // сдвиг от 30 до 365 дней в будущее + случайный день запуска
        int startOffset = ThreadLocalRandom.current().nextInt(30, 300)
                + LocalDate.now().getDayOfYear() % 30; // небольшой разброс по дню запуска
        LocalDate checkin  = LocalDate.now().plusDays(startOffset);
        LocalDate checkout = checkin.plusDays(ThreadLocalRandom.current().nextInt(2, 5));
        return new String[]{ checkin.format(ISO), checkout.format(ISO) };
    }

    public static String[] invalidDates() {
        LocalDate checkin = LocalDate.now().plusDays(60);
        return new String[]{ checkin.format(ISO), checkin.minusDays(3).format(ISO) };
    }
}