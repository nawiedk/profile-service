package com.nawiedk.profile.application.service.profile;

import com.nawiedk.profile.application.port.in.command.profile.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.profile.CreateProfileUseCase;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
                command.imageUrl(),
                command.githubUrl(),
                command.linkedInUrl(),
                command.profileStatus(),
                new ArrayList<>(),
                new ArrayList<>()
        );
        return repositoryPort.save(profile);
    }
}
