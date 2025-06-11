package com.nawiedk.profile.adapter.out.persistence.adapterimpl;

import com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.util.ProfileSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements ProfileRepositoryPort {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public Profile save(Profile profile) {
        return profileMapper.mapToDomainEntity(
                profileRepository.save(profileMapper.mapToJpaEntity(profile))
        );
    }

    @Override
    public Optional<Profile> findProfileByProfileId(Long profileId) {
        return profileRepository.findById(profileId)
                .map(profileMapper::mapToDomainEntity);
    }

    @Override
    public void deleteByProfileId(Long profileId) {
        profileRepository.deleteById(profileId);
    }

    @Override
    public boolean existsByProfileId(Long profileId) {
        return profileRepository.existsById(profileId);
    }

    @Override
    public List<Profile> findAll() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public List<Profile> findBySearchCriteria(
            ProfileSearchCriteria searchCriteria
    ) {
        return profileRepository.findBySearchCriteria(searchCriteria.name(),
                        searchCriteria.role(),
                        searchCriteria.location(),
                        searchCriteria.skill(),
                        searchCriteria.status())
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }
}
