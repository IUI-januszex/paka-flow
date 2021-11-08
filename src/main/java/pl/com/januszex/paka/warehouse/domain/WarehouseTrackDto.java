package pl.com.januszex.paka.warehouse.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseTrackDto {

    @JsonProperty("idLocalWarehouseSource")
    private Long sourceWarehouseId;

    @JsonProperty("idGlobalWarehouse1")
    private Long firstGlobalWarehouseId;

    @JsonProperty("idGlobalWarehouse2")
    private Long secondGlobalWarehouseId;

    @JsonProperty("idLocalWarehouseDestination")
    private Long destinationWarehouseId;
}
