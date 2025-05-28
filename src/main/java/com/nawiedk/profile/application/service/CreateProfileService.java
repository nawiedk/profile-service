package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.usecase.CreateProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import static com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper.mapToDomainEntity;
import static com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper.mapToJpaEntity;

@Service
public class CreateProfileService implements CreateProfileUseCase {

    private final ProfileRepository repository;

    public CreateProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile create(Profile profile) {
        ProfileJpaEntity saved = repository.save(mapToJpaEntity(profile));
        return mapToDomainEntity(saved);
    }
}
