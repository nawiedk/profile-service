package com.nawiedk.profile.application.port.out;

import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.util.ProfileSearchCriteria;

import java.util.List;
import java.util.Optional;

public interface ProfileRepositoryPort {
    Profile save(Profile profile);

    Optional<Profile> findProfileByProfileId(Long profileId);

    void deleteByProfileId(Long profileId);

    boolean existsByProfileId(Long profileId);

    List<Profile> findAllProfiles();

    List<Profile> findBySearchCriteria(ProfileSearchCriteria searchCriteria);
}