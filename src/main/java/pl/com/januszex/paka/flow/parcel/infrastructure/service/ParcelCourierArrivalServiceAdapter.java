package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelCourierArrivalServicePort;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
class ParcelCourierArrivalServiceAdapter implements ParcelCourierArrivalServicePort {
    @Override
    public int getDaysToArriveToSender() {
        return 0;
    }

    @Override
    public int getDaysToReceiver() {
        return 0;
    }

    @Override
    public boolean isMoveDateValid(LocalDate newDate, LocalDate oldDate) {
        DayOfWeek dayOfWeek = newDate.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            return false;
        }
        long diff = ChronoUnit.DAYS.between(newDate, oldDate);
        return diff < 4;
    }

}
