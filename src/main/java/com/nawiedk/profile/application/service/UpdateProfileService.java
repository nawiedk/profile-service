package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.usecase.UpdateProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.out.persistence.dao.ProfileEntity;
import com.nawiedk.profile.out.persistence.dao.ProfileRepository;
import org.springframework.stereotype.Service;

import static com.nawiedk.profile.application.mapper.ProfileMapper.toDomain;
import static com.nawiedk.profile.application.mapper.ProfileMapper.updateEntity;

@Service
public class UpdateProfileService implements UpdateProfileUseCase {

    private final ProfileRepository repository;

    public UpdateProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile update(Long id, Profile updatedProfile) {
        ProfileEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        updateEntity(entity, updatedProfile);

        ProfileEntity saved = repository.save(entity);
        return toDomain(saved);
    }
}
