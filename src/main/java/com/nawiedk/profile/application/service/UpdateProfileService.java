package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.UpdateProfileUseCase;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.Profile.ProfileId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProfileService implements UpdateProfileUseCase {

    private final ProfileRepositoryPort repositoryPort;

    @Override
    public Profile update(UpdateProfileCommand command) {
        repositoryPort.findById(command.id())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Profile updatedProfileDomain = Profile.profileWithId(
                new ProfileId(command.id()),
                command.name(),
                command.bio(),
                command.location(),
                command.role(),
                command.imageUrl(), command.profileStatusEnum(), command.skills()
        );

        return repositoryPort.save(updatedProfileDomain);
    }
}
