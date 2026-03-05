package org.qazcodenarxoz.entity;


import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "agents")
public class Agent {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(columnDefinition = "char(36)")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // APP, POS, WEB, PARTNER

    private String country;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // M:N agent_merchants
    @ManyToMany
    @JoinTable(
            name = "agent_merchants",
            joinColumns = @JoinColumn(name = "agent_id"),
            inverseJoinColumns = @JoinColumn(name = "merchant_id")
    )
    private Set<Merchant> merchants = new HashSet<>();

    // getters / setters
}
