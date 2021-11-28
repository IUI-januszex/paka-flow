package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelCourierArrivalServicePort;

@Service
class ParcelCourierArrivalServiceAdapter implements ParcelCourierArrivalServicePort {
    @Override
    public int getDaysToArriveToSender() {
        return 1;
    }
}
