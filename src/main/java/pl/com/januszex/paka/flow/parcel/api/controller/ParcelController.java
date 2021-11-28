package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.januszex.paka.flow.parcel.api.request.RegisterParcelRequest;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/parcel")
public class ParcelController {

    private final ParcelServicePort parcelService;

    @PostMapping
    public ResponseEntity<Object> registerParcel(@RequestBody @Valid RegisterParcelRequest request) {
        Parcel parcel = parcelService.registerParcel("1", request); //TODO mock! change it!
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(parcel.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

}
