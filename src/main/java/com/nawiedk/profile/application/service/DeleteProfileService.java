package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.DeleteProfileUseCase;
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
    public void deleteById(DeleteProfileCommand command) {
        if (!repositoryPort.existsById(command.id())) {
            throw new RuntimeException("Profile not found");
        }
        repositoryPort.deleteById(command.id());
    }
}