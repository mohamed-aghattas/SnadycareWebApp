package com.orca.sndycareV99.entity;


import com.orca.sndycareV99.interfaces.BelongsToResidence;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "properties",
        indexes = @Index(name = "idx_properties_code", columnList = "property_code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property implements BelongsToResidence {

    @Override
    public Residence getResidence() {
        return null;
    }

    public enum PropertyType {APARTMENT, SHOP, OFFICE, GARAGE}

    public enum PropertyStatus {AVAILABLE, OCCUPIED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_properties_building"))
    private Building building;

    @Column(name = "property_code", unique = true, nullable = false, length = 50)
    private String propertyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false, length = 20)
    private PropertyType propertyType;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "coownership_fee", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal coownershipFee = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private PropertyStatus status = PropertyStatus.AVAILABLE;
}
