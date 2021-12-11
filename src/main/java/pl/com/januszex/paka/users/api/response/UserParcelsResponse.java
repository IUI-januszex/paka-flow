package pl.com.januszex.paka.users.api.response;

import lombok.Builder;
import lombok.Value;
import pl.com.januszex.paka.flow.parcel.api.response.ParcelBriefView;

import java.util.Collection;

@Builder
@Value
public class UserParcelsResponse {
    Collection<ParcelBriefView> sentParcels;

    Collection<ParcelBriefView> observedParcels;
}
