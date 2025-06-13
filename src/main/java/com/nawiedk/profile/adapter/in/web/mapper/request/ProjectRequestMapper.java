package com.nawiedk.profile.adapter.in.web.mapper.request;

import com.nawiedk.profile.adapter.in.web.dto.ProjectCreateDto;
import com.nawiedk.profile.adapter.in.web.dto.ProjectUpdateDto;
import com.nawiedk.profile.application.port.in.command.project.CreateProjectCommand;
import com.nawiedk.profile.application.port.in.command.project.UpdateProjectCommand;
import com.nawiedk.profile.domain.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectRequestMapper {

    private final SkillRequestMapper skillRequestMapper;

    public CreateProjectCommand generateCreateProjectCommand(ProjectCreateDto dto) {
        return new CreateProjectCommand(
                dto.getName(),
                dto.getPreviewText(),
                dto.getDescription(),
                dto.getTechnologies().stream().map(skillRequestMapper::mapToDomainEntity).collect(Collectors.toSet()),
                dto.getUrls()
        );
    }

    public UpdateProjectCommand generateUpdateProjectCommand(ProjectUpdateDto dto) {
        return new UpdateProjectCommand(
                dto.getProjectId(),
                dto.getName(),
                dto.getPreviewText(),
                dto.getDescription(),
                dto.getTechnologies().stream().map(skillRequestMapper::mapToDomainEntity).collect(Collectors.toSet()),
                dto.getUrls()
        );
    }

    public Project mapToDomainEntity(ProjectUpdateDto dto) {
        return Project.projectWithId(
                new Project.ProjectId(dto.getProjectId()),
                dto.getName(),
                dto.getPreviewText(),
                dto.getDescription(),
                dto.getTechnologies().stream().map(skillRequestMapper::mapToDomainEntity).collect(Collectors.toSet()),
                dto.getUrls()
        );
    }
}
