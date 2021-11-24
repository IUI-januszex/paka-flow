package pl.com.januszex.paka.flow.base;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface DateTimeServicePort {
    LocalDateTime getNow();

    boolean isToday(LocalDateTime localDateTime);

    LocalDate addWorkdays(LocalDate date, int workdays);
}
