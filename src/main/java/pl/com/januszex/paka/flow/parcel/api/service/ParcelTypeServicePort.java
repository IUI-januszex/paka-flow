package pl.com.januszex.paka.flow.parcel.api.service;

import pl.com.januszex.paka.flow.parcel.domain.ParcelType;

import java.util.Collection;

public interface ParcelTypeServicePort {
    Collection<ParcelType> getAll();

    ParcelType getById(long id);

    void deleteById(long id);
}
