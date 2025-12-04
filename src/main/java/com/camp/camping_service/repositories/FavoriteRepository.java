package com.camp.camping_service.repositories;

import com.camp.camping_service.entities.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, String> {
    void deleteAllByProfileIdAndLandmarkId(String profileId, String landmarkId);
    List<Favorite> findByProfileId(String clerkId);
}
