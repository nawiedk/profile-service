package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.usecase.GetProfileUseCase;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.out.persistence.dao.ProfileEntity;
import com.nawiedk.profile.out.persistence.dao.ProfileRepository;
import org.springframework.stereotype.Service;

import static com.nawiedk.profile.application.mapper.ProfileMapper.toDomain;

@Service
public class GetProfileService implements GetProfileUseCase {

    private final ProfileRepository repository;

    public GetProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile getProfileById(Long id) {
        ProfileEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return toDomain(entity);
    }
}
