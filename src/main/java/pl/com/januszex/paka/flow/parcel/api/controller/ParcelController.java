package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.januszex.paka.flow.delivery.api.DeliveryAttemptResponse;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelRequest;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelBriefView;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelDetailView;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelViewCreator;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.security.CurrentUser;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/parcel")
public class ParcelController {

    private final ParcelServicePort parcelService;
    private final ParcelViewCreator parcelViewCreator;

    @PostMapping
    @PreAuthorize("hasAnyRole('ClientInd', 'ClientBiz')")
    public ResponseEntity<ParcelBriefView> registerParcel(@RequestBody @Valid ParcelRequest request,
                                                          CurrentUser currentUser) {
        Parcel parcel = parcelService.registerParcel(currentUser.getPrincipal(), request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(parcel.getId()).toUri();
        return ResponseEntity.created(location).body(parcelViewCreator.map(parcel));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ClientInd', 'ClientBiz')")
    public ResponseEntity<Void> deleteParcel(@PathVariable("id") long id) {
        parcelService.deleteParcel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ParcelBriefView> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(parcelViewCreator.map(parcelService.getById(id)));
    }

    @GetMapping(path = "/{id}/detail")
    @PreAuthorize("hasAnyRole('Admin', 'Courier', 'Logistician')")
    public ResponseEntity<ParcelDetailView> getByIdWithDetails(@PathVariable("id") long id) {
        return ResponseEntity.ok(parcelViewCreator.mapWithDetails(parcelService.getById(id)));
    }

    @GetMapping(path = "/{id}/states")
    public ResponseEntity<Collection<ParcelStateResponse>> getStates(@PathVariable("id") long id) {
        return ResponseEntity.ok(parcelService.getById(id)
                .getStates()
                .stream()
                .map(ParcelState::toResponse)
                .sorted(Comparator.comparing(ParcelStateResponse::getChangeTime).reversed())
                .collect(Collectors.toList()));
    }

    @GetMapping(path = "/{id}/delivery-attempt")
    public ResponseEntity<Collection<DeliveryAttemptResponse>> getDeliveryAttempts(@PathVariable("id") long id) {
        return ResponseEntity.ok(
                parcelService.getParcelDeliveryAttempts(id).stream().map(DeliveryAttemptResponse::of).collect(Collectors.toList())
        );
    }
}
