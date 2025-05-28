package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.usecase.UpdateProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import static com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper.mapToDomainEntity;
import static com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper.updateEntity;

@Service
public class UpdateProfileService implements UpdateProfileUseCase {

    private final ProfileRepository repository;

    public UpdateProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile update(Long id, Profile updatedProfile) {
        ProfileJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        updateEntity(entity, updatedProfile);

        ProfileJpaEntity saved = repository.save(entity);
        return mapToDomainEntity(saved);
    }
}
