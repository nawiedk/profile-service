package com.nawiedk.profile.adapter.in.web.mapper.request;

import com.nawiedk.profile.adapter.in.web.dto.ProfileCreateDto;
import com.nawiedk.profile.adapter.in.web.dto.ProfileUpdateDto;
import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfileRequestMapper {

    private final SkillRequestMapper skillRequestMapper;

    public CreateProfileCommand generateCreateProfileCommand(ProfileCreateDto dto) {
        return new CreateProfileCommand(
                dto.getName(),
                dto.getBio(),
                dto.getLocation(),
                dto.getRole(),
                dto.getProfileStatus(),
                dto.getImageUrl(),
                dto.getSkills().stream().map(skillRequestMapper::mapToDomainEntity).collect(Collectors.toSet())
        );
    }

    public UpdateProfileCommand generateUpdateProfileCommand(ProfileUpdateDto dto) {
        return new UpdateProfileCommand(
                dto.getProfileId(),
                dto.getName(),
                dto.getBio(),
                dto.getLocation(),
                dto.getRole(),
                dto.getProfileStatus(),
                dto.getImageUrl(),
                dto.getSkills().stream().map(skillRequestMapper::mapToDomainEntity).collect(Collectors.toSet())
        );
    }
}
