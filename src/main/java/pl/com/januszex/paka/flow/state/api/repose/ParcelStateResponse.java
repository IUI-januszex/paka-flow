package pl.com.januszex.paka.flow.state.api.repose;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ParcelStateResponse {
    ParcelStateType type;
    Long warehouseId;
    WarehouseType warehouseType;
    String courierId;
}
