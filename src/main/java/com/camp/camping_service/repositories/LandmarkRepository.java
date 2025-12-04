package com.camp.camping_service.repositories;

import com.camp.camping_service.dto.select.SelectLandmarkListRecord;
import com.camp.camping_service.entities.Landmark;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandmarkRepository extends JpaRepository<Landmark, String>, JpaSpecificationExecutor<Landmark> {

    @Query("SELECT " +
            "new com.camp.camping_service.dto.select.SelectLandmarkListRecord(l.id, l.title, l.description, l.price, l.lat, l.lng, l.secureUrl, f.id) "+
            "FROM Landmark l "+
            "LEFT JOIN l.favorites f ON f.profileId = :clerkId "+
            "WHERE (:category IS NULL OR l.category = :category) "+
            "AND l.title LIKE %:title% "+
            "ORDER BY l.title")
    List<SelectLandmarkListRecord> findAllWithFavorite(
            @Param("clerkId") String clerkId,
            @Param("category") String category,
            @Param("title") String title
    );

    Long countByProfileId(String clerkId);
    List<Landmark> findByProfileId(String clerkId);
}
