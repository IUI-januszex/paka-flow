package pl.com.januszex.paka.flow.state.infrastructure.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.base.DateTimeServicePort;
import pl.com.januszex.paka.flow.parcel.api.exception.ParcelNotFound;
import pl.com.januszex.paka.flow.parcel.domain.Operation;
import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.exception.ParcelStateNotFound;
import pl.com.januszex.paka.flow.state.api.repository.ParcelStateRepositoryPort;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.api.service.ParcelStateServicePort;
import pl.com.januszex.paka.flow.state.infrastructure.service.manager.ParcelStateManager;
import pl.com.januszex.paka.flow.state.infrastructure.service.manager.ParcelStateManagerFactory;
import pl.com.januszex.paka.flow.state.model.ParcelState;
import pl.com.januszex.paka.flow.state.model.ParcelStateFactory;
import pl.com.januszex.paka.flow.state.model.ParcelStateType;
import pl.com.januszex.paka.warehouse.domain.WarehouseType;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
class ParcelStateServiceAdapter implements ParcelStateServicePort {

    private final ParcelStateRepositoryPort parcelStateRepository;
    private final ParcelStateManagerFactory parcelStateManagerFactory;
    private final DateTimeServicePort dateTimeService;
    private final Map<ParcelStateType, ParcelStateManager> managers = new EnumMap<>(ParcelStateType.class);

    private final EntityManager entityManager;

    @Override
    public ParcelState getCurrentParcelState(long parcelId) {
        return parcelStateRepository.getCurrentParcelState(parcelId).orElseThrow(() -> new ParcelNotFound(parcelId));
    }

    @Override
    public ParcelState getById(long id) {
        entityManager.detach(parcelStateRepository.getById(id).orElseThrow(() -> new ParcelStateNotFound(id)));
        return parcelStateRepository.getById(id).orElseThrow(() -> new ParcelStateNotFound(id));
    }

    @Override
    @Transactional
    public ParcelState changeParcelState(ChangeParcelStateRequest request) {
        ParcelState currentState = getCurrentParcelState(request.getParcelId());
        validateChangeStateData(request);
        currentState.setCurrent(false);
        ParcelState newParcelState = parcelStateRepository.add(ParcelStateFactory.newInstance(request,
                currentState,
                currentState.getParcel(),
                dateTimeService.getNow()));
        doPostChangeOperations(newParcelState);
        return newParcelState;
    }

    @Override
    public ParcelState getInitState(long localWarehouseId, Parcel parcel) {
        ChangeParcelStateRequest parcelStateRequest = new ChangeParcelStateRequest();
        parcelStateRequest.setNextState(ParcelStateType.AT_SENDER);
        parcelStateRequest.setWarehouseId(localWarehouseId);
        parcelStateRequest.setWarehouseType(WarehouseType.LOCAL);
        getManager(ParcelStateType.AT_SENDER).validateChangeStateData(parcelStateRequest);
        return ParcelStateFactory.newInstance(parcelStateRequest,
                null,
                parcel,
                dateTimeService.getNow());
    }

    @Override
    @Transactional
    public void deleteLastParcelState(long parcelId) {
        ParcelState parcelState = getCurrentParcelState(parcelId);
        ParcelState previousParcelSate = parcelState.getPreviousState();
        parcelState.setPreviousState(null);
        previousParcelSate.setCurrent(true);
        parcelStateRepository.delete(parcelState);
    }

    @Override
    public void validateChangeStateData(ChangeParcelStateRequest request) {
        getManager(request.getNextState()).validateChangeStateData(request);
    }

    @Override
    public void doPostChangeOperations(ParcelState newParcelState) {
        getManager(newParcelState.getType()).doPostChangeOperations(newParcelState);
    }

    @Override
    public AddressDto getSourceAddress(ParcelState parcelState) {
        return getManager(parcelState.getType()).getSourceAddress(parcelState);
    }

    @Override
    public AddressDto getDestinationAddress(ParcelState parcelState) {
        return getManager(parcelState.getType()).getDestinationAddress(parcelState);
    }

    @Override
    public Operation getNextOperation(ParcelState parcelState) {
        return getManager(parcelState.getType()).getNextOperation(parcelState);
    }

    @PostConstruct
    private void initManagers() {
        for (ParcelStateType value : ParcelStateType.values()) {
            managers.put(value, parcelStateManagerFactory.getInstance(value));
        }
    }

    private ParcelStateManager getManager(ParcelStateType parcelStateType) {
        return managers.get(parcelStateType);
    }
}
