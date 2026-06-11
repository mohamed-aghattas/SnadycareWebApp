package com.orca.sndycareV99.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "residences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Residence  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255,unique = true)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String city;

    @Column(name = "total_units")
    @Builder.Default
    private Integer totalUnits = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by",
            foreignKey = @ForeignKey(name = "fk_residences_creator"))
    private User createdBy;

    @OneToMany(mappedBy = "residence" , cascade = CascadeType.ALL , orphanRemoval = true)
    @Builder.Default
    List<Account> accounts = new ArrayList<>();


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    public void addAcount(Account account){
        accounts.add(account);
        account.setResidence(this);
    }

    public void removeAcount(Account account){
        accounts.remove(account);
        account.setResidence(null);
    }
}