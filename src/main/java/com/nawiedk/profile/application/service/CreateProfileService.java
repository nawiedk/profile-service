package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.usecase.CreateProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.out.persistence.dao.ProfileEntity;
import com.nawiedk.profile.out.persistence.dao.ProfileRepository;
import org.springframework.stereotype.Service;

import static com.nawiedk.profile.application.mapper.ProfileMapper.toDomain;
import static com.nawiedk.profile.application.mapper.ProfileMapper.toEntity;

@Service
public class CreateProfileService implements CreateProfileUseCase {

    private final ProfileRepository repository;

    public CreateProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile create(Profile profile) {
        ProfileEntity saved = repository.save(toEntity(profile));
        return toDomain(saved);
    }
}
