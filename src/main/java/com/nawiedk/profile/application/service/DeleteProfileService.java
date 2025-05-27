package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.usecase.DeleteProfileUseCase;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteProfileService implements DeleteProfileUseCase {

    private final ProfileRepository repository;

    public DeleteProfileService(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Profile not found");
        }
        repository.deleteById(id);
    }
}
