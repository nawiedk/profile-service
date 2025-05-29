package com.nawiedk.profile.adapter.out.persistence.repository;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<SkillJpaEntity, Long> {
    //todo: write repository methods
}
