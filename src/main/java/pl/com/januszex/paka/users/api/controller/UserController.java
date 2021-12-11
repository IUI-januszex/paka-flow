package pl.com.januszex.paka.users.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelServicePort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelViewCreator;
import pl.com.januszex.paka.security.CurrentUser;
import pl.com.januszex.paka.users.api.response.UserParcelsResponse;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final ParcelServicePort parcelService;
    private final ParcelViewCreator parcelViewCreator;

    @GetMapping("/me/parcels")
    @PreAuthorize("hasAnyRole('ClientInd', 'ClientBiz')")
    public ResponseEntity<UserParcelsResponse> getUserParels(CurrentUser currentUser) {
        return ResponseEntity.ok(UserParcelsResponse.builder()
                .observedParcels(parcelService.getObservedParcelByUser(currentUser.getPrincipal()).stream()
                        .map(parcelViewCreator::map)
                        .collect(Collectors.toList()))
                .sentParcels(parcelService.getParcelSendByUser(currentUser.getPrincipal()).stream()
                        .map(parcelViewCreator::map)
                        .collect(Collectors.toList()))
                .build());
    }

}
