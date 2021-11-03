package pl.com.januszex.paka.flow.parcel.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Data
@Entity
@Table(name = "PARCEL_TYPES")
public class ParcelType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, name = "IS_ACTIVE")
    private boolean active;

    @OneToMany(mappedBy = "parcelType", fetch = LAZY)
    private Set<Parcel> parcels;
}
