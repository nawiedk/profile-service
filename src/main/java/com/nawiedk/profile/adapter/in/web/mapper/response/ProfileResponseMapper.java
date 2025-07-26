package com.nawiedk.profile.adapter.in.web.mapper.response;

import com.nawiedk.profile.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfileResponseMapper {

    private final SkillResponseMapper skillResponseMapper;
    private final ProjectResponseMapper projectResponseMapper;

    public ProfileResponse mapToResponse(Profile entity) {
        return new ProfileResponse(
                entity.getName(),
                entity.getBio().isEmpty() ? new String[0] : entity.getBio().split("(?:\r\n|\n){2,}"),
                entity.getLocation(),
                entity.getRole(),
                entity.getImageUrl(),
                entity.getGithubUrl(),
                entity.getLinkedInUrl(),
                entity.getProfileStatusAsString(),
                entity.getSkills().stream().map(skillResponseMapper::mapToResponse).collect(Collectors.toList()),
                entity.getProjects().stream().map(projectResponseMapper::mapToResponse).collect(Collectors.toList())
        );
    }
}
