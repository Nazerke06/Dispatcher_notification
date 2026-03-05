package org.qazcodenarxoz.entity;

import javax.persistence.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "merchants")
public class Merchant {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(columnDefinition = "char(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "mcc")
    private String mcc;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false)
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
