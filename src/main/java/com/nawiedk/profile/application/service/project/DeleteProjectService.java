package com.nawiedk.profile.application.service.project;

import com.nawiedk.profile.application.port.in.command.project.DeleteProjectCommand;
import com.nawiedk.profile.application.port.in.usecase.project.DeleteProjectUseCase;
import com.nawiedk.profile.application.port.out.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteProjectService implements DeleteProjectUseCase {

    private final ProjectRepositoryPort repositoryPort;

    @Override
    public void deleteByProjectId(DeleteProjectCommand command) {
        if (!repositoryPort.existsByProjectId(command.projectId())) {
            throw new RuntimeException("Project not found");
        }
        repositoryPort.deleteByProjectId(command.projectId());
    }
}
