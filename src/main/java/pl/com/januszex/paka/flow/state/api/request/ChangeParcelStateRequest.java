package pl.com.januszex.paka.flow.state.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeParcelStateRequest {

    private ParcelStateType nextState;

    private String courierId;

    private WarehouseType warehouseType;

    private Long warehouseId;

    private Long parcelId;
}
