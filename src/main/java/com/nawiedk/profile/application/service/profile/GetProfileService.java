package com.nawiedk.profile.application.service.profile;

import com.nawiedk.profile.application.port.in.command.profile.GetProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.profile.GetProfileUseCase;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GetProfileService implements GetProfileUseCase {

    private final ProfileRepositoryPort repositoryPort;

    @Override
    public List<Profile> getAllProfiles() {
        return repositoryPort.findAllProfiles();
    }

    @Override
    public Profile getProfileByProfileId(GetProfileCommand command) {
        return repositoryPort.findProfileByProfileId(command.profileId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }
}