package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.usecase.GetProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import static com.nawiedk.profile.adapter.out.persistence.mapper.ProfileMapper.mapToDomainEntity;

@Service
public class GetProfileService implements GetProfileUseCase {

    private final ProfileRepository repository;

    public GetProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile getProfileById(Long id) {
        ProfileJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToDomainEntity(entity);
    }
}
