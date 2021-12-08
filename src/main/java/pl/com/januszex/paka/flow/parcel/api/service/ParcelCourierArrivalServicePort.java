package pl.com.januszex.paka.flow.parcel.api.service;

import java.time.LocalDate;

public interface ParcelCourierArrivalServicePort {

    int getDaysToArriveToSender();

    int getDaysToReceiver();

    boolean isMoveDateValid(LocalDate newDate, LocalDate oldDate);

}
