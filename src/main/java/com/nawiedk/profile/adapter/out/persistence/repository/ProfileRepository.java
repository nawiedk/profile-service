package com.nawiedk.profile.adapter.out.persistence.repository;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.domain.model.ProfileStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileJpaEntity, Long> {
    List<ProfileJpaEntity> findByRole(String role);

    List<ProfileJpaEntity> findByLocation(String location);

    List<ProfileJpaEntity> findByProfileStatus(ProfileStatusEnum status);

    Optional<ProfileJpaEntity> findByUserId(Long userId);

    Optional<ProfileJpaEntity> findByExternalSystemId(String externalSystemId);

    List<ProfileJpaEntity> findByTeamId(Long teamId);

    List<ProfileJpaEntity> findByManagerId(Long managerId);

    @Query("SELECT DISTINCT p FROM ProfileJpaEntity p LEFT JOIN p.skills s " +
            "WHERE (:nameKeyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :nameKeyword, '%'))) " +
            "AND (:role IS NULL OR p.role = :role) " +
            "AND (:location IS NULL OR p.location = :location) " +
            "AND (:skillKeyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :skillKeyword, '%'))) " +
            "AND (:status IS NULL OR p.profileStatus = :status)")
    List<ProfileJpaEntity> findBySearchCriteria(
            @Param("nameKeyword") String nameKeyword,
            @Param("role") String role,
            @Param("location") String location,
            @Param("skillKeyword") String skillKeyword,
            @Param("status") ProfileStatusEnum status
    );

    @Query("SELECT DISTINCT p FROM ProfileJpaEntity p LEFT JOIN p.skills s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :skillKeyword, '%'))")
    List<ProfileJpaEntity> findBySkillKeyword(@Param("skillKeyword") String skillKeyword);
}