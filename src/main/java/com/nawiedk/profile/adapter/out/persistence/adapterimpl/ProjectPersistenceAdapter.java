package com.nawiedk.profile.adapter.out.persistence.adapterimpl;

import com.nawiedk.profile.adapter.out.persistence.mapper.ProjectMapper;
import com.nawiedk.profile.adapter.out.persistence.repository.ProjectRepository;
import com.nawiedk.profile.application.port.out.ProjectRepositoryPort;
import com.nawiedk.profile.domain.model.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectPersistenceAdapter implements ProjectRepositoryPort {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public Project save(Project project) {
        return projectMapper.mapToDomainEntity(
                projectRepository.save(projectMapper.mapToJpaEntity(project))
        );
    }

    @Override
    public Optional<Project> findProjectByProjectId(Long projectId) {
        return projectRepository.findById(projectId)
                .map(projectMapper::mapToDomainEntity);
    }

    public void deleteByProjectId(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    public boolean existsByProjectId(Long projectId) {
        return projectRepository.existsById(projectId);
    }

    public List<Project> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::mapToDomainEntity)
                .toList();
    }
}
