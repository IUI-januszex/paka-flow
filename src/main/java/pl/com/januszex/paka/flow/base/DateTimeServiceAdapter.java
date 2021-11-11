package pl.com.januszex.paka.flow.base;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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
}
