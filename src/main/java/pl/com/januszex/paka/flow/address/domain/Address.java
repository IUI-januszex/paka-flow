package pl.com.januszex.paka.flow.address.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String postalCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String city;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String street;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String buildingNumber;

    @Column(columnDefinition = "TEXT")
    private String flatNumber;
}
