package pl.com.januszex.paka.warehouse.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelViewCreator;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.warehouse.api.response.WarehouseParcelsResponse;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

    private final ParcelServicePort parcelService;
    private final ParcelViewCreator viewCreator;

    @GetMapping(path = "global/{id}")
    public ResponseEntity<WarehouseParcelsResponse> getParcelsFromGlobal(@PathVariable long id) {
        return prepareParcelResponse(id, WarehouseType.GLOBAL);
    }

    @GetMapping(path = "local/{id}")
    public ResponseEntity<WarehouseParcelsResponse> getParcelsFromLocal(@PathVariable long id) {
        return prepareParcelResponse(id, WarehouseType.LOCAL);
    }

    private ResponseEntity<WarehouseParcelsResponse> prepareParcelResponse(@PathVariable long id, WarehouseType local) {
        Collection<Parcel> parcels = parcelService.getParcelFormWarehouse(id, local);
        return ResponseEntity.ok(WarehouseParcelsResponse.of(parcels.stream()
                .map(viewCreator::mapWithDetails)
                .collect(Collectors.toList())));
    }
}
