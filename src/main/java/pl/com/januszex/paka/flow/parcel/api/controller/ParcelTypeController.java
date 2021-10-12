package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelTypeRequest;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelTypeResponse;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelTypeServicePort;
import pl.com.januszex.paka.flow.parcel.domain.ParcelType;

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
    public ResponseEntity<ParcelType> add(@RequestBody @Valid ParcelTypeRequest request) {
        ParcelType parcelType = parcelTypeService.add(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(parcelType.getId()).toUri();
        return ResponseEntity.created(location).body(parcelType);
    }

    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Object> update(@PathVariable long id, @RequestBody @Valid ParcelTypeRequest request) {
        parcelTypeService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Collection<ParcelTypeResponse>> getAll() {
        return new ResponseEntity<>(parcelTypeService.getAll().stream()
                .map(ParcelTypeResponse::of)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<ParcelTypeResponse> getById(@PathVariable long id) {
        return new ResponseEntity<>(ParcelTypeResponse.of(parcelTypeService.getById(id)),
                HttpStatus.OK);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Object> delete(@PathVariable long id) {
        parcelTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
