package pl.com.januszex.paka.flow.state.api.service;

import pl.com.januszex.paka.flow.parcel.model.Parcel;
import pl.com.januszex.paka.flow.state.api.request.ChangeParcelStateRequest;
import pl.com.januszex.paka.flow.state.infrastructure.service.manager.ParcelStateManager;
import pl.com.januszex.paka.flow.state.model.ParcelState;

import java.time.LocalDateTime;

public interface ParcelStateServicePort extends ParcelStateManager {
    ParcelState getCurrentParcelState(long parcelId);

    ParcelState getById(long id);

    ParcelState changeParcelState(ChangeParcelStateRequest request, LocalDateTime now);

    ParcelState getInitState(long localWarehouseId, Parcel parcel, LocalDateTime now);
}
