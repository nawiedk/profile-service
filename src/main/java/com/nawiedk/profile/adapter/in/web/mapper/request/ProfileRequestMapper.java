package com.nawiedk.profile.adapter.in.web.mapper.request;

import com.nawiedk.profile.adapter.in.web.dto.ProfileCreateDto;
import com.nawiedk.profile.adapter.in.web.dto.ProfileUpdateDto;
import com.nawiedk.profile.application.port.in.command.profile.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.command.profile.UpdateProfileCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfileRequestMapper {

    public CreateProfileCommand generateCreateProfileCommand(ProfileCreateDto dto) {
        return new CreateProfileCommand(
                dto.getName(),
                dto.getBio(),
                dto.getLocation(),
                dto.getRole(),
                dto.getImageUrl(),
                dto.getGithubUrl(),
                dto.getLinkedInUrl(),
                dto.getProfileStatus()
        );
    }

    public UpdateProfileCommand generateUpdateProfileCommand(ProfileUpdateDto dto) {
        return new UpdateProfileCommand(
                dto.getProfileId(),
                dto.getName(),
                dto.getBio(),
                dto.getLocation(),
                dto.getRole(),
                dto.getImageUrl(),
                dto.getGithubUrl(),
                dto.getLinkedInUrl(),
                dto.getProfileStatus()
        );
    }
}
