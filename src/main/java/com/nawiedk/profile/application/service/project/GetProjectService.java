package com.nawiedk.profile.application.service.project;

import com.nawiedk.profile.application.port.in.command.project.GetProjectCommand;
import com.nawiedk.profile.application.port.in.usecase.project.GetProjectUseCase;
import com.nawiedk.profile.application.port.out.ProjectRepositoryPort;
import com.nawiedk.profile.domain.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GetProjectService implements GetProjectUseCase {

    private final ProjectRepositoryPort repositoryPort;

    @Override
    public List<Project> findAll() {
        return repositoryPort.findAll();
    }

    @Override
    public Project findProjectByProjectId(GetProjectCommand command) {
        return repositoryPort.findProjectByProjectId(command.projectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
