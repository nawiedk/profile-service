package com.nawiedk.profile.application.service.profile;

import com.nawiedk.profile.application.port.in.command.profile.UpdateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.profile.UpdateProfileUseCase;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.Profile.ProfileId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileService implements UpdateProfileUseCase {

    private final ProfileRepositoryPort repositoryPort;

    @Override
    public Profile update(UpdateProfileCommand command) {
        repositoryPort.findProfileByProfileId(command.profileId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Profile updatedProfileDomain = Profile.profileWithId(
                new ProfileId(command.profileId()),
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
        return repositoryPort.save(updatedProfileDomain);
    }
}
