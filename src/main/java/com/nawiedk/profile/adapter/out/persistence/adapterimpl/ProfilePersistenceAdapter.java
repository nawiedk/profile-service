package com.nawiedk.profile.adapter.out.persistence.adapterimpl;

import com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProfilePersistenceAdapter implements ProfileRepositoryPort {

    //todo: finish methods

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @Override
    public Profile save(Profile profile) {
        return profileMapper.mapToDomainEntity(
                profileRepository.save(profileMapper.mapToJpaEntity(profile))
        );
    }

    @Override
    public Optional<Profile> findById(Long id) {
        return profileRepository.findById(id)
                .map(profileMapper::mapToDomainEntity);
    }

    @Override
    public void deleteById(Long id) {
        profileRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return profileRepository.existsById(id);
    }

    @Override
    public List<Profile> findAll() {
        return profileRepository.findAll()
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    /*
    @Override
    public List<Profile> findBySearchCriteria(String nameKeyword, String role, String location, String skillKeyword, ProfileStatus status) {
        return List.of();
    }

    @Override
    public List<Profile> findByRole(String role) {
        return List.of();
    }

    @Override
    public List<Profile> findByLocation(String location) {
        return List.of();
    }

    @Override
    public List<Profile> findBySkillKeyword(String skillKeyword) {
        return List.of();
    }

    @Override
    public List<Profile> findByTeamId(Long teamId) {
        return List.of();
    }

    @Override
    public List<Profile> findByManagerId(Long managerId) {
        return List.of();
    }

    @Override
    public List<Profile> findByProfileStatus(ProfileStatus status) {
        return List.of();
    }

    @Override
    public Profile updateProfileStatus(Long id, ProfileStatus newStatus) {
        return null;
    }

    @Override
    public Optional<Profile> findByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<Profile> findByExternalSystemId(String externalSystemId) {
        return Optional.empty();
    }
     */
}
