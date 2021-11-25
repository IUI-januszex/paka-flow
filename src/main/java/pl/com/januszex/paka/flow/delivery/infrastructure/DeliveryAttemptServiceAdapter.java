package pl.com.januszex.paka.flow.delivery.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.januszex.paka.flow.base.DateTimeServicePort;
import pl.com.januszex.paka.flow.delivery.api.DeliveryAttemptServicePort;
import pl.com.januszex.paka.flow.delivery.model.DeliveryAttempt;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;

@Service
@RequiredArgsConstructor
class DeliveryAttemptServiceAdapter implements DeliveryAttemptServicePort {

    private final ParcelServicePort parcelService;
    private final DateTimeServicePort dateTimeService;
    private final ParcelRepositoryPort parcelRepository;

    @Override
    @Transactional
    public void addDeliveryAttempt(long parcelId, String courierId) {
        Parcel parcel = parcelService.getById(parcelId);
        DeliveryAttempt deliveryAttempt = new DeliveryAttempt();
        deliveryAttempt.setCourierId(courierId);
        deliveryAttempt.setDateTime(dateTimeService.getNow());
        deliveryAttempt.setParcel(parcel);

        parcel.getDeliveryAttempts().add(deliveryAttempt);

        parcelRepository.update(parcel); //is this necessary?
    }
}
