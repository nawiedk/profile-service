package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.GetProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.GetProfileUseCase;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.Profile;
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
    public Profile findProfileById(GetProfileCommand command) {
        return repositoryPort.findById(command.id())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Override
    public List<Profile> findAll() {
        return repositoryPort.findAll();
    }
}