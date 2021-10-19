package pl.com.januszex.paka.flow.state.domain;

class Delivered extends ParcelState {
    @Override
    public ParcelStateType getType() {
        return ParcelStateType.DELIVERED;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return false;
    }
}
