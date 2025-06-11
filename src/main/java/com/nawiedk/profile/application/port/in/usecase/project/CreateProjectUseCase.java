package com.nawiedk.profile.application.port.in.usecase.project;

import com.nawiedk.profile.application.port.in.command.project.CreateProjectCommand;
import com.nawiedk.profile.domain.model.Project;

public interface CreateProjectUseCase {
    Project create(CreateProjectCommand command);
}
