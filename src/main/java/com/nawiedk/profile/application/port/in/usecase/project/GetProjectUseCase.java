package com.nawiedk.profile.application.port.in.usecase.project;

import com.nawiedk.profile.application.port.in.command.project.GetProjectCommand;
import com.nawiedk.profile.domain.model.Project;

import java.util.List;

public interface GetProjectUseCase {
    List<Project> findAll();

    Project findProjectByProjectId(GetProjectCommand command);
}
