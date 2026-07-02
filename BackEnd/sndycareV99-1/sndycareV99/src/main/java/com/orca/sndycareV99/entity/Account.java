package com.orca.sndycareV99.entity;


import com.orca.sndycareV99.interfaces.BelongsToResidence;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account implements BelongsToResidence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "residence_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_accounts_residence"))
    private Residence residence;

    @Column(name = "account_number", nullable = false, length = 100)
    private String accountNumber;

    @Column(name = "account_name", nullable = false, length = 255)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    public enum AccountType {
        ASSET,
        LIABILITY,
        EQUITY,
        REVENUE,
        EXPENSE
    }

    @Override
    public Residence getResidence() {
        return this.residence;
    }
}
