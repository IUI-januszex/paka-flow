package pl.com.januszex.paka.flow.state.domain;

import lombok.Data;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;
import pl.com.januszex.paka.flow.state.api.exception.IllegalNextState;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public abstract class ParcelState {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private LocalDateTime changeTime;

    @Column(nullable = false)
    private boolean current;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(nullable = false, name = "parcelId")
    private Parcel parcel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "previousStateId")
    private ParcelState previousState;

    public void checkNextState(ParcelStateType nextState) {
        if (!isNextStateValid(nextState)) {
            throw new IllegalNextState(getType(), nextState);
        }
    }

    public abstract ParcelStateType getType();

    protected abstract boolean isNextStateValid(ParcelStateType nextState);

}
