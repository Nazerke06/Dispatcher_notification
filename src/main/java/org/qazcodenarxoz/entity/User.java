package org.qazcodenarxoz.entity;

import javax.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    @Column(columnDefinition = "char(36)")
    private UUID id;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String phone;
    private String country;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // getters / setters
}
