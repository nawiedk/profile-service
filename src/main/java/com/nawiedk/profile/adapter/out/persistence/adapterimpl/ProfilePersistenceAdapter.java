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

    /*
    @Override
    public List<Profile> findByRole(String role) {
        return profileRepository.findByRole(role)
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public List<Profile> findByLocation(String location) {
        return profileRepository.findByLocation(location)
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public List<Profile> findBySkill(String skillKeyword) {
        return profileRepository.findBySkillKeyword(skillKeyword)
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public List<Profile> findByTeamId(Long teamId) {
        return profileRepository.findByTeamId(teamId)
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public List<Profile> findByManagerId(Long managerId) {
        return profileRepository.findByManagerId(managerId)
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    public List<Profile> findByProfileStatus(ProfileStatus status) {
        return profileRepository.findByProfileStatus(status)
                .stream()
                .map(profileMapper::mapToDomainEntity)
                .toList();
    }

    @Override
    @Transactional
    public Profile updateProfileStatus(Long id, ProfileStatus newStatus) {
        return profileRepository.findById(id)
                .map(entity -> {
                    entity.setProfileStatus(newStatus);
                    return profileMapper.mapToDomainEntity(profileRepository.save(entity));
                })
                .orElseThrow(() -> new IllegalArgumentException("Profile not found with id: " + id));
    }

    @Override
    public Optional<Profile> findByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .map(profileMapper::mapToDomainEntity);
    }

    @Override
    public Optional<Profile> findByExternalSystemId(String externalSystemId) {
        return profileRepository.findByExternalSystemId(externalSystemId)
                .map(profileMapper::mapToDomainEntity);
    }
    */
}
