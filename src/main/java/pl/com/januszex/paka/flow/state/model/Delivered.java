package pl.com.januszex.paka.flow.state.model;

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
