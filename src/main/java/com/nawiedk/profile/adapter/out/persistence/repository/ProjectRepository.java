package com.nawiedk.profile.adapter.out.persistence.repository;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProjectJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectJpaEntity, Long> {
}
