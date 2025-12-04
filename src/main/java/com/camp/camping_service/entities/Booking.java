package com.camp.camping_service.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Instant;

@Entity
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking extends BaseEntity{
    @Column(name = "total_nights")
    private Integer totalNights;

    @Column(name = "check_in")
    private Instant checkIn;

    @Column(name = "check_out")
    private Instant checkOut;

    @Column(name = "total")
    private Long total;

    @Builder.Default
    @Column(name = "payment_status")
    private Boolean paymentStatus = false;

    @Column(name = "profile_id")
    private String profileId;

    @Column(name = "landmark_id")
    private String landmarkId;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "clerk_id", insertable = false, updatable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "landmark_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Landmark landmark;
}
