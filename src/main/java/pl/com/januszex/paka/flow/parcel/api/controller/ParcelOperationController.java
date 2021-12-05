package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.januszex.paka.flow.parcel.api.request.AssignParcelToCourierRequest;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.security.CurrentUser;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/parcel/{id}")
public class ParcelOperationController {

    private final ParcelServicePort parcelService;

    @PostMapping

    public ResponseEntity<Object> assignToCourier(@PathVariable("id") long id,
                                                  @RequestBody @Valid AssignParcelToCourierRequest request,
                                                  CurrentUser currentUser) {
        parcelService.assignParcelToCourier(id, request.getCourierId(), currentUser.getPrincipal());
        return ResponseEntity.noContent().build();
    }

}
