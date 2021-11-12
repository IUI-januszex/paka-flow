package pl.com.januszex.paka.flow.parcel.model;

import lombok.Data;
import pl.com.januszex.paka.flow.address.model.Address;
import pl.com.januszex.paka.flow.delivery.model.DeliveryAttempt;
import pl.com.januszex.paka.flow.state.model.ParcelState;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
    @JoinColumn(nullable = false, name = "PARCEL_TYPE_ID")
    private ParcelType parcelType;

    @Column(nullable = false)
    private BigDecimal parcelPrice;

    @Column(nullable = false)
    private BigDecimal parcelFee;

    @Column(nullable = false, name = "IS_FEE_PAID")
    private boolean feePaid;

    @Column(nullable = false, name = "IS_PAID")
    private boolean paid;

    @Column(nullable = false, name = "IS_DATE_MOVED")
    private boolean dateMoved;

    @Column(nullable = false, name = "IS_MARKED_TO_RETURN")
    private boolean markedToReturn = false;

    @Column(nullable = false)
    private String sendingUserId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String senderDetails;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String senderEmailAddress;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String senderPhoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String receiverDetails;

    @Column(columnDefinition = "TEXT")
    private String receiverEmailAddress;

    @Column(columnDefinition = "TEXT")
    private String receiverPhoneNumber;

    @ElementCollection
    //@Column(name = "OBSERVER_ID")
    public Set<String> observerIds;

    @JoinColumn(nullable = false, name = "DELIVERY_ADDRESS_ID")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private Address deliveryAddress;

    @JoinColumn(nullable = false, name = "SENDER_ADDRESS_ID")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private Address senderAddress;

    @OneToMany(fetch = LAZY, mappedBy = "parcel", cascade = ALL)
    private List<ParcelState> states;

    @OneToMany(fetch = EAGER, mappedBy = "parcel", cascade = ALL)
    private List<DeliveryAttempt> deliveryAttempts;
}