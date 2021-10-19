package pl.com.januszex.paka.flow.state.api.request;

import lombok.Data;
import pl.com.januszex.paka.flow.state.domain.ParcelStateType;

@Data
public class ChangeParcelStateRequest {

    private ParcelStateType nextState;

    private Long courierId;

    private Long magazineId;
}
