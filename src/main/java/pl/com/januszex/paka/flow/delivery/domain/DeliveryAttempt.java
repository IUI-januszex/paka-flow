package pl.com.januszex.paka.flow.delivery.domain;

import lombok.Data;
import pl.com.januszex.paka.flow.parcel.domain.Parcel;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.EAGER;

@Entity
@Data
public class DeliveryAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private Long courierId;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(nullable = false, name = "parcelId")
    private Parcel parcel;
}
