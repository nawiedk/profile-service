package com.nawiedk.profile.adapter.in.web.mapper.response;

import com.nawiedk.profile.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfileResponseMapper {

    private final SkillResponseMapper skillResponseMapper;

    public ProfileResponse mapToResponse(Profile entity) {
        return new ProfileResponse(
                entity.getProfileId().map(Profile.ProfileId::id).orElse(null),
                entity.getName(),
                entity.getBio(),
                entity.getLocation(),
                entity.getRole(),
                entity.getImageUrl(),
                entity.getProfileStatusAsString(),
                entity.getSkills().stream().map(skillResponseMapper::mapToResponse).collect(Collectors.toSet())
        );
    }
}
