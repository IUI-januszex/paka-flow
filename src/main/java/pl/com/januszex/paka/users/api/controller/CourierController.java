package pl.com.januszex.paka.users.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelViewCreator;
import pl.com.januszex.paka.users.api.response.CourierParcelsResponse;

import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/courier/{id}")
@RequiredArgsConstructor
public class CourierController {

    private final ParcelServicePort parcelService;
    private final ParcelViewCreator parcelViewCreator;

    @GetMapping(path = "/parcels")
    public ResponseEntity<CourierParcelsResponse> getCouriersParcels(@PathVariable("id") String id) {
        return ResponseEntity.ok()
                .body(CourierParcelsResponse.of(parcelService.getCouriersParcels(id).stream()
                        .map(parcelViewCreator::mapWithDetails)
                        .collect(Collectors.toList())));
    }

}
