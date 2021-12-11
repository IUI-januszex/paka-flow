package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.januszex.paka.flow.parcel.api.request.AssignParcelToCourierRequest;
import pl.com.januszex.paka.flow.parcel.api.request.DeliverToWarehouseRequest;
import pl.com.januszex.paka.flow.parcel.api.request.MoveCourierArrivalDateRequest;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelPaidRequest;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.security.CurrentUser;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/parcel/{id}")
public class ParcelOperationController {

    private final ParcelServicePort parcelService;

    @PostMapping(path = "/assign")
    @PreAuthorize("hasRole('Logistician')")
    public ResponseEntity<Object> assignToCourier(@PathVariable("id") long id,
                                                  @RequestBody @Valid AssignParcelToCourierRequest request,
                                                  CurrentUser currentUser) {
        parcelService.assignParcelToCourier(id, request.getCourierId(), currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "pick-up")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Object> pickUpParcel(@PathVariable("id") long id,
                                               CurrentUser currentUser) {
        parcelService.pickupParcel(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "deliver-to-warehouse")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Object> pickUpParcel(@PathVariable("id") long id,
                                               @RequestBody @Valid DeliverToWarehouseRequest request) {
        parcelService.deliverParcelAtWarehouse(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "deliver-to-client")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Object> deliverToClient(@PathVariable("id") long id,
                                                  CurrentUser currentUser) {
        parcelService.deliverParcelToClient(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "move-date")
    public ResponseEntity<Object> moveDate(@PathVariable("id") long id,
                                           @RequestBody @Valid MoveCourierArrivalDateRequest request) {
        parcelService.moveCourierArrivalDate(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "pay")
    public ResponseEntity<Object> payParcel(@PathVariable("id") long id,
                                            @RequestBody @Valid ParcelPaidRequest request) {
        parcelService.setParcelPaid(id, request.isPaid());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "pay-fee")
    public ResponseEntity<Object> payParcelFee(@PathVariable("id") long id,
                                               @RequestBody @Valid ParcelPaidRequest request) {
        parcelService.setParcelFeePaid(id, request.isPaid());
        return ResponseEntity.noContent().build();
    }


    @PostMapping(path = "delivery-attempt")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Object> addDeliveryAttempt(@PathVariable("id") long id,
                                                     CurrentUser currentUser) {
        parcelService.addDeliveryAttempt(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
