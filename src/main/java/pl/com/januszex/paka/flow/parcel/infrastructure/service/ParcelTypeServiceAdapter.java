package pl.com.januszex.paka.flow.parcel.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelTypeNotFoundException;
import pl.com.januszex.paka.flow.parcel.api.exception.UpdateOrDeleteParcelTypeNotPossibleException;
import pl.com.januszex.paka.flow.parcel.api.repository.ParcelTypeRepositoryPort;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelTypeChangeActivatedRequest;
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
    public Collection<ParcelType> getAllActive() {
        return parcelTypeRepository.getActive();
    }

    @Override
    @Transactional
    public ParcelType add(ParcelTypeRequest request) {
        ParcelType model = new ParcelType();
        convertRequestToModel(request, model);
        model.setActive(true);
        return parcelTypeRepository.add(model);
    }

    @Override
    public ParcelType update(long id, ParcelTypeRequest request) {
        ParcelType model = getById(id);
        checkParcelType(model);
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
        ParcelType model = getById(id);
        checkParcelType(model);
        parcelTypeRepository.delete(model);
    }

    @Override
    public void changeActiveState(long id, ParcelTypeChangeActivatedRequest request) {
        ParcelType model = getById(id);
        model.setActive(request.isActive());
        parcelTypeRepository.update(model);
    }

    @Override
    public int getAssignedParcelCount(long id) {
        return getById(id).getParcels().size();
    }

    private void convertRequestToModel(ParcelTypeRequest request, ParcelType model) {
        model.setName(request.getName());
        model.setDescription(request.getDescription());
        model.setPrice(request.getPrice());
    }

    private void checkParcelType(ParcelType model) {
        if (!model.getParcels().isEmpty()) {
            throw new UpdateOrDeleteParcelTypeNotPossibleException(model.getId());
        }
    }
}
