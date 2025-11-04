package com.custard.account_service.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "profiles", indexes = {
        @Index(name = "idx_profile_user_id", columnList = "user_id")
})
@Getter
@Setter
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String profilePicture;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = true)
    private String lastName;
    private Date dob;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressEntity> addresses = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;

    public void addAddress(AddressEntity addressEntity) {
        if (addressEntity != null) {
            this.addresses.add(addressEntity);
            addressEntity.setProfile(this);
        }
    }

    public void removeAddress(AddressEntity addressEntity) {
        this.addresses.remove(addressEntity);
        addressEntity.setProfile(null);
    }

}
