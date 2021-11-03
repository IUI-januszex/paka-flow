package pl.com.januszex.paka.flow.parcel.domain;

import lombok.Data;
import pl.com.januszex.paka.flow.address.domain.Address;
import pl.com.januszex.paka.flow.delivery.domain.DeliveryAttempt;
import pl.com.januszex.paka.flow.state.domain.ParcelState;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;
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

    @Column(nullable = false, length = 4)
    private char[] pin;

    private LocalDate expectedCourierArrivalDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(nullable = false, name = "parcelTypeId")
    private ParcelType parcelType;

    @Column(nullable = false)
    private BigDecimal parcelPrice;

    @Column(nullable = false)
    private BigDecimal parcelFee;

    @Column(nullable = false, name = "isFeePaid")
    private boolean feePaid;

    @Column(nullable = false, name = "isPaid")
    private boolean paid;

    @Column(nullable = false, name = "isDateMoved")
    private boolean dateMoved;

    @Column(nullable = false, name = "isMarkedToReturn")
    private boolean markedToReturn = false;

    @Column(nullable = false)
    private String senderId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String receiverDetails;

    @Column(columnDefinition = "TEXT")
    private String receiverEmailAddress;

    @JoinColumn(nullable = false, name = "deliveryAddressId")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private Address deliveryAddress;

    @JoinColumn(nullable = false, name = "senderAddressId")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private Address senderAddress;

    @OneToMany(fetch = LAZY, mappedBy = "parcel", cascade = ALL)
    private List<ParcelState> states;

    @OneToMany(fetch = EAGER, mappedBy = "parcel", cascade = ALL)
    private List<DeliveryAttempt> deliveryAttempts;

}