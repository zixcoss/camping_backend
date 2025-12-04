package com.camp.camping_service.repositories;

import com.camp.camping_service.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,String> {
    Optional<Profile> findByClerkId(String clerkId);
}
