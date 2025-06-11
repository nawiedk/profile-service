package com.nawiedk.profile.adapter.in.web.mapper.request;

import com.nawiedk.profile.adapter.in.web.dto.ProfileCreateDto;
import com.nawiedk.profile.adapter.in.web.dto.ProfileUpdateDto;
import com.nawiedk.profile.application.port.in.command.profile.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.command.profile.UpdateProfileCommand;
import com.nawiedk.profile.domain.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

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
                dto.getImageUrl()
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
                dto.getImageUrl()
        );
    }

    public Profile mapToDomainEntity(ProfileUpdateDto dto) {
        return Profile.profileWithId(
                new Profile.ProfileId(dto.getProfileId()),
                dto.getName(),
                dto.getBio(),
                dto.getLocation(),
                dto.getRole(),
                dto.getImageUrl(),
                dto.getProfileStatus(),
                new HashSet<>(),
                new HashSet<>()
        );
    }
}
