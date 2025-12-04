package com.camp.camping_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "landmark")
public class Landmark extends BaseEntity{
    @Column(name = "title", columnDefinition = "character varying(100)")
    private String title;

    @Column(name = "description", columnDefinition = "character varying(1000)")
    private String description;

    @Column(name = "lat", columnDefinition = "numeric(9,6)")
    private BigDecimal lat;

    @Column(name = "lng", columnDefinition = "numeric(9,6)")
    private BigDecimal lng;

    @Column(name = "category", columnDefinition = "character varying(50)")
    private String category;

    @Column(name = "price")
    private Long price;

    @Column(name = "public_id", columnDefinition = "character varying(255)")
    private String publicId;

    @Column(name = "secure_url", columnDefinition = "character varying(255)")
    private String secureUrl;

    @Column(name = "profile_id")
    private String profileId;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "clerk_id", insertable = false, updatable = false)
    private Profile profile;

    @OneToMany(mappedBy = "landmark", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Booking> bookings;

    @OneToMany(mappedBy = "landmark", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Favorite> favorites;
}
