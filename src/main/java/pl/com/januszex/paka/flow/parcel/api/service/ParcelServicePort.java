package pl.com.januszex.paka.flow.parcel.api.service;

import pl.com.januszex.paka.flow.address.api.response.AddressDto;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;

public interface ParcelServicePort {

    Parcel getById(long id);

    AddressDto getSourceAddress(long parcelId);

    AddressDto getDestinationAddress(long parcelId);
}
