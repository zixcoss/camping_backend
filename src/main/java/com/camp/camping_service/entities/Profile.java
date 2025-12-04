package com.camp.camping_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile")
public class Profile extends BaseEntity{

    @Column(name = "clerk_id", columnDefinition = "character varying(255)", nullable = false, unique = true)
    private String clerkId;

    @Column(name = "first_name" ,columnDefinition = "character varying(50)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "character varying(50)")
    private String lastName;

    @Column(name = "email", columnDefinition = "character varying(50)")
    private String email;

    @Column(name = "profile_image", columnDefinition = "character varying(255)")
    private String profileImage;

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Landmark> landmark;

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Booking> bookings;

    @OneToMany(mappedBy = "profile", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Favorite> favorites;
}
