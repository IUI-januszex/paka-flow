package pl.com.januszex.paka.flow.state.model;

class Returned extends ParcelState {
    @Override
    public ParcelStateType getType() {
        return ParcelStateType.RETURNED;
    }

    @Override
    protected boolean isNextStateValid(ParcelStateType nextState) {
        return false;
    }
}
