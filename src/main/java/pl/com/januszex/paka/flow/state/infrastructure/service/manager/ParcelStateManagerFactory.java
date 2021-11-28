package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.base.DateTimeServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelCourierArrivalServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.notification.api.NotificationServicePort;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;

@Service
public class ParcelStateManagerFactory {

    private final WarehouseDao warehouseDao;
    private final ParcelStateServicePort parcelStateService;
    private final ParcelServicePort parcelService;
    private final DateTimeServicePort dateTimeService;
    private final NotificationServicePort notificationService;
    private final ParcelCourierArrivalServicePort parcelCourierArrivalService;
    private final DateTimeServicePort dateTimeServicePort;

    public ParcelStateManagerFactory(WarehouseDao warehouseDao,
                                     @Lazy ParcelStateServicePort parcelStateService,
                                     @Lazy ParcelServicePort parcelService,
                                     DateTimeServicePort dateTimeService,
                                     NotificationServicePort notificationService,
                                     ParcelCourierArrivalServicePort parcelCourierArrivalService,
                                     DateTimeServicePort dateTimeServicePort) {
        this.warehouseDao = warehouseDao;
        this.parcelStateService = parcelStateService;
        this.parcelService = parcelService;
        this.dateTimeService = dateTimeService;
        this.notificationService = notificationService;
        this.parcelCourierArrivalService = parcelCourierArrivalService;
        this.dateTimeServicePort = dateTimeServicePort;
    }

    public ParcelStateManager getInstance(ParcelStateType parcelStateType) {
        switch (parcelStateType) {
            case AT_SENDER:
                return new ParcelAtSenderManager(warehouseDao,
                        notificationService,
                        parcelCourierArrivalService,
                        dateTimeServicePort);
            case ASSIGNED_TO_COURIER:
                return new ParcelAssignedToCourierManager(parcelStateService);
            case AT_WAREHOUSE:
                return new ParcelAtWarehouseManager(warehouseDao, dateTimeService);
            case DELIVERED:
                return new ParcelDeliveredManager(parcelService);
            case RETURNED:
                return new ParcelReturnedManager(parcelService);
            case AT_COURIER:
                return new ParcelAtCourierManager(parcelStateService, warehouseDao);
        }
        throw new IllegalStateException("Unsupported parcel state");
    }
}
