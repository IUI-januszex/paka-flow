package pl.com.januszex.paka.flow.parcel.api.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class MoveCourierArrivalDateRequest {

    @NotNull(message = "Provide new date")
    private LocalDate newDate;

    @NotNull(message = "Provide parcel pin")
    private char[] pin;
}
