package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.januszex.paka.flow.delivery.api.DeliveryAttemptResponse;
import pl.com.januszex.paka.flow.parcel.api.request.RegisterParcelRequest;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.security.CurrentUser;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/parcel")
public class ParcelController {

    private final ParcelServicePort parcelService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ClientInd', 'ClientBiz')")
    public ResponseEntity<Object> registerParcel(@RequestBody @Valid RegisterParcelRequest request,
                                                 CurrentUser currentUser) {
        Parcel parcel = parcelService.registerParcel(currentUser.getPrincipal(), request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(parcel.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
    
    @GetMapping(path = "/{id}/delivery-attempt")
    public ResponseEntity<Collection<DeliveryAttemptResponse>> getDeliveryAttempts(@PathVariable("id") long id){
        return ResponseEntity.ok(
                parcelService.getParcelDeliveryAttempts(id).stream().map(DeliveryAttemptResponse::of).collect(Collectors.toList())
        );
    }
}
