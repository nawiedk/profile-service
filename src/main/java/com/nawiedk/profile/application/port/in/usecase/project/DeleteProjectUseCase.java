package com.nawiedk.profile.application.port.in.usecase.project;

import com.nawiedk.profile.application.port.in.command.project.DeleteProjectCommand;

public interface DeleteProjectUseCase {
    void deleteByProjectId(DeleteProjectCommand command);
}
