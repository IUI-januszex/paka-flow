package pl.com.januszex.paka.flow.state.infrastructure.service.manager;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.domain.ParcelStateType;
import pl.com.januszex.paka.warehouse.api.dao.WarehouseDao;

@Service
public class ParcelStateManagerFactory {

    private final WarehouseDao warehouseDao;
    private final ParcelStateServicePort parcelStateService;

    public ParcelStateManagerFactory(WarehouseDao warehouseDao,
                                     @Lazy ParcelStateServicePort parcelStateService) {
        this.warehouseDao = warehouseDao;
        this.parcelStateService = parcelStateService;
    }

    public ParcelStateManager getInstance(ParcelStateType parcelStateType) {
        switch (parcelStateType) {
            case ASSIGNED_TO_WAREHOUSE:
                return new ParcelAssignedToWarehouseManager(warehouseDao, parcelStateService);
            case AT_SENDER:
                return new ParcelAtSenderManager(warehouseDao);
            case ASSIGNED_TO_COURIER:
                return new ParcelAssignedToCourierManager(parcelStateService);
            case AT_WAREHOUSE:
                return new ParcelAtWarehouseManager(warehouseDao);
            case DELIVERED:
                return new ParcelDeliveredManager();
            case RETURNED:
                return new ParcelReturnedManager();
            case AT_COURIER:
                return new ParcelAtCourierManager(parcelStateService);
        }
        throw new IllegalStateException("Unsupported parcel state");
    }
}
