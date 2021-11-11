package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.dao.WarehouseDao;

@Service
public class ParcelStateManagerFactory {

    private final WarehouseDao warehouseDao;
    private final ParcelStateServicePort parcelStateService;
    private final ParcelServicePort parcelService;

    public ParcelStateManagerFactory(WarehouseDao warehouseDao,
                                     @Lazy ParcelStateServicePort parcelStateService,
                                     @Lazy ParcelServicePort parcelService) {
        this.warehouseDao = warehouseDao;
        this.parcelStateService = parcelStateService;
        this.parcelService = parcelService;
    }

    public ParcelStateManager getInstance(ParcelStateType parcelStateType) {
        switch (parcelStateType) {
            case AT_SENDER:
                return new ParcelAtSenderManager(warehouseDao);
            case ASSIGNED_TO_COURIER:
                return new ParcelAssignedToCourierManager(parcelStateService);
            case AT_WAREHOUSE:
                return new ParcelAtWarehouseManager(warehouseDao);
            case DELIVERED:
                return new ParcelDeliveredManager(parcelService);
            case RETURNED:
                return new ParcelReturnedManager(parcelService);
            case AT_COURIER:
                return new ParcelAtCourierManager(parcelStateService);
        }
        throw new IllegalStateException("Unsupported parcel state");
    }
}
