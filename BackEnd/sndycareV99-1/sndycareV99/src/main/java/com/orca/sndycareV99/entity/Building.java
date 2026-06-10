package com.orca.sndycareV99.entity;

import com.orca.sndycareV99.interfaces.BelongsToResidence;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Building  implements BelongsToResidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residence_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_buildings_residence"))
    private Residence residence;

    @Column(nullable = false, length = 255)
    private String name;

    private Integer floors;
}