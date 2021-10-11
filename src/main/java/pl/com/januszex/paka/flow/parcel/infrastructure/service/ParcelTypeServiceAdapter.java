package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelTypeNotFoundException;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelTypeRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.service.ParcelTypeServicePort;
import pl.com.januszex.paka.flow.parcel.domain.ParcelType;

import java.util.Collection;

@Service
@RequiredArgsConstructor
class ParcelTypeServiceAdapter implements ParcelTypeServicePort {

    private final ParcelTypeRepositoryPort parcelTypeRepository;

    @Override
    public Collection<ParcelType> getAll() {
        return parcelTypeRepository.getAll();
    }

    @Override
    public ParcelType getById(long id) {
        return parcelTypeRepository.getById(id).orElseThrow(() -> new ParcelTypeNotFoundException(id));
    }

    @Override
    public void deleteById(long id) {
        parcelTypeRepository.delete(getById(id));
    }
}
