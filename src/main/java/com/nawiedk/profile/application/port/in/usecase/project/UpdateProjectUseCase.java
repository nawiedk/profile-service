package com.nawiedk.profile.application.port.in.usecase.project;

import com.nawiedk.profile.application.port.in.command.project.UpdateProjectCommand;
import com.nawiedk.profile.domain.model.Project;

public interface UpdateProjectUseCase {
    Project update(UpdateProjectCommand command);
}
