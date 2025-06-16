package com.nawiedk.profile.application.service.project;

import com.nawiedk.profile.application.port.in.command.project.CreateProjectCommand;
import com.nawiedk.profile.application.port.in.usecase.project.CreateProjectUseCase;
import com.nawiedk.profile.application.port.out.ProjectRepositoryPort;
import com.nawiedk.profile.domain.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectRepositoryPort repositoryPort;

    @Override
    public Project create(CreateProjectCommand command) {
        Project project = Project.projectWithoutId(
                command.name(),
                command.previewText(),
                command.description(),
                command.technologies(),
                command.githubUrl(),
                command.projectStatus()
        );
        return repositoryPort.save(project);
    }
}
