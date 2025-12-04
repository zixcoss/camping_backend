package com.camp.camping_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "favorite")
public class Favorite extends BaseEntity{

    @Column(name = "profile_id")
    private String profileId;

    @Column(name = "landmark_id")
    private String landmarkId;

    @ManyToOne
    @JoinColumn(name = "profile_id", referencedColumnName = "clerk_id", updatable = false, insertable = false)
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "landmark_id", referencedColumnName = "id", updatable = false, insertable = false)
    private Landmark landmark;
}
