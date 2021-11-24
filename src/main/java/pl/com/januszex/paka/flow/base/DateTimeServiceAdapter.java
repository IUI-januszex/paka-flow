package pl.com.januszex.paka.flow.base;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
class DateTimeServiceAdapter implements DateTimeServicePort {

    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

    @Override
    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    @Override
    public boolean isToday(LocalDateTime localDateTime) {
        return fmt.format(localDateTime).equals(fmt.format(getNow()));
    }

    @Override
    public LocalDate addWorkdays(LocalDate date, int workdays) {
        if (workdays < 1) {
            return date;
        }

        LocalDate result = date;
        int addedDays = 0;
        while (addedDays < workdays) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++addedDays;
            }
        }

        return result;
    }
}
