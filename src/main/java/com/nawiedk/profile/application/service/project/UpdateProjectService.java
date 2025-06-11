package com.nawiedk.profile.application.service.project;


import com.nawiedk.profile.application.port.in.command.project.UpdateProjectCommand;
import com.nawiedk.profile.application.port.in.usecase.project.UpdateProjectUseCase;
import com.nawiedk.profile.application.port.out.ProjectRepositoryPort;
import com.nawiedk.profile.domain.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProjectService implements UpdateProjectUseCase {

    private final ProjectRepositoryPort repositoryPort;

    @Override
    public Project update(UpdateProjectCommand command) {
        repositoryPort.findProjectByProjectId(command.projectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Project updatedProjectDomain = Project.projectWithId(
                new Project.ProjectId(command.projectId()),
                command.name(),
                command.description(),
                command.technologies(),
                command.urls()
        );
        return repositoryPort.save(updatedProjectDomain);
    }
}
