package com.nawiedk.profile.application.port.out;

import com.nawiedk.profile.domain.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryPort {
    Project save(Project project);

    Optional<Project> findProjectByProjectId(Long projectId);

    void deleteByProjectId(Long projectId);

    boolean existsByProjectId(Long projectId);

    List<Project> findAll();
}
