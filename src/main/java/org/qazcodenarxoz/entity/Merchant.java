package org.qazcodenarxoz.module4_1.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String mcc;
    private String country;
    private String city;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // M:N merchant_products
    @ManyToMany
    @JoinTable(
            name = "merchant_products",
            joinColumns = @JoinColumn(name = "merchant_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    // M:N agent_merchants
    @ManyToMany(mappedBy = "merchants")
    private Set<Agent> agents = new HashSet<>();

    // getters / setters
}