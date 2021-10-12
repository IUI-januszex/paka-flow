package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelTypeNotFoundException;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelTypeRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelTypeRequest;
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
    @Transactional
    public ParcelType add(ParcelTypeRequest request) {
        ParcelType model = new ParcelType();
        convertRequestToModel(request, model);
        return parcelTypeRepository.add(model);
    }

    @Override
    public ParcelType update(long id, ParcelTypeRequest request) {
        ParcelType model = getById(id);
        convertRequestToModel(request, model);
        return parcelTypeRepository.update(model);
    }

    @Override
    public ParcelType getById(long id) {
        return parcelTypeRepository.getById(id).orElseThrow(() -> new ParcelTypeNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        parcelTypeRepository.delete(getById(id));
    }

    private void convertRequestToModel(ParcelTypeRequest request, ParcelType model) {
        model.setName(request.getName());
        model.setDescription(request.getDescription());
        model.setPrice(request.getPrice());
    }
}
