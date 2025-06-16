package com.nawiedk.profile.domain.model;

import com.nawiedk.profile.domain.util.ProjectStatus;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public class Project {

    @Valid
    private final ProjectId projectId;

    @Getter
    @NotBlank
    @Size(min = 2, max = 100)
    private final String name;

    @Getter
    @Size(max = 255)
    private final String previewText;

    @Getter
    @Size(max = 1000)
    private final String description;

    @Getter
    private final Set<@Valid Skill> technologies;

    @Getter
    private final String githubUrl;

    @Getter
    @NotNull
    private final ProjectStatus projectStatus;

    public Project(ProjectId projectId,
                   String name,
                   String previewText,
                   String description,
                   Set<Skill> technologies,
                   String githubUrl,
                   ProjectStatus projectStatus
    ) {
        this.projectId = projectId;
        this.name = name;
        this.previewText = previewText;
        this.description = description;
        this.technologies = technologies;
        this.githubUrl = githubUrl;
        this.projectStatus = projectStatus;
        validate(this);
    }


    public record ProjectId(@Valid Long projectId) {
        public ProjectId(@Valid Long projectId) {
            if (projectId == null)
                throw new ConstraintViolationException("Profile ID cannot be null", new HashSet<>());
            this.projectId = projectId;
            validate(this);
        }
    }


    public static Project projectWithId(ProjectId projectId, String name, String previewText, String description, Set<Skill> technologies, String githubUrl, ProjectStatus projectStatus) {
        return new Project(projectId, name, previewText, description, technologies, githubUrl, projectStatus);
    }

    public static Project projectWithoutId(String name, String previewText, String description, Set<Skill> technologies, String githubUrl, ProjectStatus projectStatus) {
        return new Project(null, name, description, previewText, technologies, githubUrl, projectStatus);
    }

    public Optional<ProjectId> getProjectId() {
        return Optional.ofNullable(this.projectId);
    }

    public String getProjectStatusAsString() {
        return this.projectStatus.toString();
    }
}

