package pl.com.januszex.paka.flow.parcel.api.service;

import pl.com.januszex.paka.flow.parcel.api.request.ParcelTypeChangeActivatedRequest;
import pl.com.januszex.paka.flow.parcel.api.request.ParcelTypeRequest;
import pl.com.januszex.paka.flow.parcel.domain.ParcelType;

import java.util.Collection;

public interface ParcelTypeServicePort {
    Collection<ParcelType> getAll();

    Collection<ParcelType> getAllActive();

    ParcelType add(ParcelTypeRequest request);

    ParcelType update(long id, ParcelTypeRequest request);

    ParcelType getById(long id);

    void deleteById(long id);

    void changeActiveState(long id, ParcelTypeChangeActivatedRequest request);

    int getAssignedParcelCount(long id);
}
