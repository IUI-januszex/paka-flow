package pl.com.januszex.paka.flow.state.model;

import pl.com.januszex.paka.flow.state.api.repose.ParcelStateResponse;

class Returned extends ParcelState {
    @Override
    public ParcelStateType getType() {
        return ParcelStateType.RETURNED;
    }

    @Override
    public ParcelStateResponse toResponse() {
        return ParcelStateResponse.builder().type(getType()).build();
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return false;
    }
}
