package pl.com.januszex.paka.flow.parcel.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name = "PARCEL")
public class Parcel {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, insertable = false)
    private Long id;

    @Column(nullable = false)
    private char[] pin;

    private LocalDate expectedCourierArrivalDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "parcelTypeId")
    private ParcelType parcelType;

    @Column(nullable = false)
    private BigDecimal parcelFee;

    @Column(nullable = false, name = "isPaid")
    private boolean paid;

    @Column(nullable = false, name = "isDateMoved")
    private boolean dateMoved;

    @Column(nullable = false, name = "isMarkedToReturn")
    private boolean markedToReturn = false;

}