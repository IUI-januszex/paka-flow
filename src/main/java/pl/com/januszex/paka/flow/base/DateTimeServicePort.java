package pl.com.januszex.paka.flow.base;

import java.time.LocalDateTime;

public interface DateTimeServicePort {
    LocalDateTime getNow();

    boolean isToday(LocalDateTime localDateTime);
}
