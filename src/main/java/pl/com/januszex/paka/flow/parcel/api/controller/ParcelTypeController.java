package pl.com.januszex.paka.flow.parcel.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelTypeResponse;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelTypeServicePort;

import java.util.Collection;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/parcel-type")
@RequiredArgsConstructor
public class ParcelTypeController {

    private final ParcelTypeServicePort parcelTypeService;

    @GetMapping
    public Collection<ParcelTypeResponse> getAll() {
        return parcelTypeService.getAll().stream().map(ParcelTypeResponse::of).collect(Collectors.toList());
    }

    @GetMapping(path = {"/{id}"})
    public ParcelTypeResponse getById(@PathVariable long id) {
        return ParcelTypeResponse.of(parcelTypeService.getById(id));
    }
}
