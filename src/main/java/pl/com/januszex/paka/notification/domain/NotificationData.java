package pl.com.januszex.paka.notification.domain;

import lombok.Getter;
import lombok.ToString;
import pl.com.januszex.paka.flow.parcel.model.Parcel;

@Getter
@ToString
public class NotificationData {

    private final String emailAddress;

    private final long parcelId;

    private final NotificationType notificationType;

    private final String sender;

    private final String receiver;


    protected NotificationData(String emailAddress,
                               long parcelId,
                               NotificationType notificationType,
                               String sender,
                               String receiver) {
        this.emailAddress = emailAddress;
        this.parcelId = parcelId;
        this.notificationType = notificationType;
        this.sender = sender;
        this.receiver = receiver;
    }

    public static NotificationData getRegisterNotificationForReceiver(Parcel parcel) {
        return new NotificationData(parcel.getReceiverEmailAddress(),
                parcel.getId(),
                NotificationType.PARCEL_REGISTERED_RECEIVER,
                parcel.getSenderDetails(),
                parcel.getReceiverDetails());
    }

    public static NotificationData getDeliveredNotificationForSender(Parcel parcel) {
        return new NotificationData(parcel.getSenderDetails(),
                parcel.getId(),
                NotificationType.PARCEL_DELIVERED,
                parcel.getSenderDetails(),
                parcel.getReceiverDetails());
    }

    public static NotificationData getCourierWillArriveTodayNotification(Parcel parcel) {
        return new NotificationData(parcel.getSenderDetails(),
                parcel.getId(),
                NotificationType.PARCEL_DELIVERED,
                parcel.getSenderDetails(),
                parcel.getReceiverDetails());
    }

    public static NotificationData getDeliveredNotificationForReceiver(Parcel parcel) {
        return new NotificationData(parcel.getReceiverEmailAddress(),
                parcel.getId(),
                NotificationType.PARCEL_DELIVERED,
                parcel.getSenderDetails(),
                parcel.getReceiverDetails());
    }

    public static NotificationData getReturnNotification(Parcel parcel) {
        return new NotificationData(parcel.getSenderEmailAddress(),
                parcel.getId(),
                NotificationType.PARCEL_WILL_RETURN,
                parcel.getSenderDetails(),
                parcel.getReceiverDetails());
    }
}
