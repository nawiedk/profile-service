package com.nawiedk.profile.adapter.out.persistence.repository;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {
}