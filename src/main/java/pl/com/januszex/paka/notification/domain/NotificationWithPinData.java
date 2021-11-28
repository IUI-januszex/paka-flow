package pl.com.januszex.paka.notification.domain;

import lombok.Getter;
import lombok.ToString;
import pl.com.januszex.paka.flow.parcel.model.Parcel;

import java.time.LocalDate;

@Getter
@ToString(callSuper = true)
public class NotificationWithPinData extends NotificationData {

    private final String pin;

    private final LocalDate arrivalDate;

    private NotificationWithPinData(String emailAddress,
                                    long parcelId,
                                    NotificationType notificationType,
                                    String sender,
                                    String receiver,
                                    String pin,
                                    LocalDate arrivalDate) {
        super(emailAddress, parcelId, notificationType, sender, receiver);
        this.pin = pin;
        this.arrivalDate = arrivalDate;
    }

    public static NotificationWithPinData getParcelRegisteredSender(Parcel parcel) {
        return getNotification(parcel, NotificationType.PARCEL_REGISTERED_SENDER);
    }

    public static NotificationWithPinData getCourierWillArrive(Parcel parcel) {
        return getNotification(parcel, NotificationType.COURIER_WILL_ARRIVE);
    }

    private static NotificationWithPinData getNotification(Parcel parcel, NotificationType courierWillArrive) {
        return new NotificationWithPinData(
                parcel.getSenderEmailAddress(),
                parcel.getId(),
                courierWillArrive,
                parcel.getSenderDetails(),
                parcel.getReceiverDetails(),
                new String(parcel.getPin()),
                parcel.getExpectedCourierArrivalDate()
        );
    }
}
