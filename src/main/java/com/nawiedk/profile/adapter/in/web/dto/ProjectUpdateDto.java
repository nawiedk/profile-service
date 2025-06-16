package com.nawiedk.profile.adapter.in.web.dto;

import com.nawiedk.profile.domain.util.ProjectStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class ProjectUpdateDto {

    @NotNull
    private Long projectId;

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 50)
    private String previewText;

    @NotBlank
    @Size(min = 50, max = 1000)
    private String description;

    private Set<@Valid SkillUpdateDto> technologies;

    private String githubUrl;

    @NotEmpty
    private ProjectStatus projectStatus;
}
