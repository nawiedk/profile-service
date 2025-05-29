package com.nawiedk.profile.adapter.out.persistence.repository;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileJpaEntity, Long> {
    //todo: write repository methods
}