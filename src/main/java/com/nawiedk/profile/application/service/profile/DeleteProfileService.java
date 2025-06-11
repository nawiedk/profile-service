package com.nawiedk.profile.application.service.profile;

import com.nawiedk.profile.application.port.in.command.profile.DeleteProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.profile.DeleteProfileUseCase;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteProfileService implements DeleteProfileUseCase {

    private final ProfileRepositoryPort repositoryPort;

    @Override
    public void deleteByProfileId(DeleteProfileCommand command) {
        if (!repositoryPort.existsByProfileId(command.profileId())) {
            throw new RuntimeException("Profile not found");
        }
        repositoryPort.deleteByProfileId(command.profileId());
    }
}