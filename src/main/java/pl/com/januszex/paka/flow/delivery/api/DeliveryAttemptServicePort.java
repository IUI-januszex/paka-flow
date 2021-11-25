package pl.com.januszex.paka.flow.delivery.api;

public interface DeliveryAttemptServicePort {

    void addDeliveryAttempt(long parcelId, String courierId);

}
