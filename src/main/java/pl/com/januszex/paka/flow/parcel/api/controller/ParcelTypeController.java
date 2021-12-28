package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelTypeChangeActivatedRequest;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelTypeRequest;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelTypeAdminResponse;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelTypeResponse;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelTypeServicePort;
import pl.com.januszex.paka.flow.parcel.model.ParcelType;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/parcel-type")
@RequiredArgsConstructor
public class ParcelTypeController {

    private final ParcelTypeServicePort parcelTypeService;

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ParcelTypeAdminResponse> add(@RequestBody @Valid ParcelTypeRequest request) {
        ParcelType parcelType = parcelTypeService.add(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(parcelType.getId()).toUri();
        return ResponseEntity.created(location).body(ParcelTypeAdminResponse.of(parcelType));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody @Valid ParcelTypeRequest request) {
        parcelTypeService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Collection<ParcelTypeAdminResponse>> getAll() {
        return new ResponseEntity<>(parcelTypeService.getAll().stream()
                .map(ParcelTypeAdminResponse::of)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path = "/active")
    public ResponseEntity<Collection<ParcelTypeResponse>> getAllActive() {
        return new ResponseEntity<>(parcelTypeService.getAllActive().stream()
                .map(ParcelTypeResponse::of)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ParcelTypeResponse> getById(@PathVariable long id) {
        return new ResponseEntity<>(ParcelTypeResponse.of(parcelTypeService.getById(id)),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        parcelTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}/state")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<Void> changeActiveChange(@PathVariable long id,
                                                     @RequestBody @Valid ParcelTypeChangeActivatedRequest request) {
        parcelTypeService.changeActiveState(id, request);
        return ResponseEntity.noContent().build();
    }
}
