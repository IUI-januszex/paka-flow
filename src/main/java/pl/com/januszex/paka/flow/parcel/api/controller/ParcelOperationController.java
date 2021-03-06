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
    public ResponseEntity<Void> assignToCourier(@PathVariable("id") long id,
                                                @RequestBody @Valid AssignParcelToCourierRequest request,
                                                CurrentUser currentUser) {
        parcelService.assignParcelToCourier(id, request.getCourierId(), currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "pick-up")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Void> pickUpParcel(@PathVariable("id") long id,
                                             CurrentUser currentUser) {
        parcelService.pickupParcel(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "return-to-warehouse")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Void> returnToWarehouse(@PathVariable("id") long id,
                                                  CurrentUser currentUser) {
        parcelService.returnToWarehouse(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "deliver-to-warehouse")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Void> deliverToWarehouse(@PathVariable("id") long id,
                                                   @RequestBody @Valid DeliverToWarehouseRequest request) {
        parcelService.deliverParcelAtWarehouse(id, request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "deliver-to-client")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Void> deliverToClient(@PathVariable("id") long id,
                                                CurrentUser currentUser) {
        parcelService.deliverParcelToClient(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "move-date")
    @PreAuthorize("hasAnyRole('ClientInd', 'ClientBiz')")
    public ResponseEntity<Void> moveDate(@PathVariable("id") long id,
                                         @RequestBody @Valid MoveCourierArrivalDateRequest request) {
        parcelService.moveCourierArrivalDate(id, request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "pay")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Void> payParcel(@PathVariable("id") long id,
                                          @RequestBody @Valid ParcelPaidRequest request) {
        parcelService.setParcelPaid(id, request.isPaid());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "pay-fee")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Void> payParcelFee(@PathVariable("id") long id,
                                             @RequestBody @Valid ParcelPaidRequest request) {
        parcelService.setParcelFeePaid(id, request.isPaid());
        return ResponseEntity.noContent().build();
    }


    @PostMapping(path = "delivery-attempt")
    @PreAuthorize("hasRole('Courier')")
    public ResponseEntity<Void> addDeliveryAttempt(@PathVariable("id") long id,
                                                   CurrentUser currentUser) {
        parcelService.addDeliveryAttempt(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "observe")
    @PreAuthorize("hasAnyRole('ClientInd', 'ClientBiz')")
    public ResponseEntity<Void> addParcelToObserve(@PathVariable("id") long id,
                                                   CurrentUser currentUser) {
        parcelService.addParcelToObserved(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "observe")
    @PreAuthorize("hasAnyRole('ClientInd', 'ClientBiz')")
    public ResponseEntity<Void> deleteParcelToObserve(@PathVariable("id") long id,
                                                      CurrentUser currentUser) {
        parcelService.removeParcelFromObserved(id, currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
