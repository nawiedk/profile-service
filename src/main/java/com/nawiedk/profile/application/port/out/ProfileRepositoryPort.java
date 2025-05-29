package com.nawiedk.profile.application.port.out;

import com.nawiedk.profile.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

public interface ProfileRepositoryPort {
    Profile save(Profile profile);
    Optional<Profile> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Profile> findAll();

    /*
    List<Profile> findBySearchCriteria(
            String nameKeyword,
            String role,
            String location,
            String skillKeyword,
            ProfileStatus status
    );
    List<Profile> findByRole(String role);
    List<Profile> findByLocation(String location);
    List<Profile> findBySkillKeyword(String skillKeyword);
    List<Profile> findByTeamId(Long teamId);
    List<Profile> findByManagerId(Long managerId);
    List<Profile> findByProfileStatus(ProfileStatus status);
    Profile updateProfileStatus(Long id, ProfileStatus newStatus);
    Optional<Profile> findByUserId(Long userId);
    Optional<Profile> findByExternalSystemId(String externalSystemId);
     */
}