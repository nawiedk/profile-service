package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.CreateProfileUseCase;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProfileService implements CreateProfileUseCase {

    private final ProfileRepositoryPort repositoryPort;

    @Override
    public Profile create(CreateProfileCommand command) {
        Profile profile = Profile.profileWithoutId(
                command.name(),
                command.bio(),
                command.location(),
                command.role(),
                command.skills(),
                command.imageUrl(),
                command.profileStatus()
        );
        return repositoryPort.save(profile);
    }
}
