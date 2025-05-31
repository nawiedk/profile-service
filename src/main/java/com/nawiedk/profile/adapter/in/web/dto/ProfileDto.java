package com.nawiedk.profile.adapter.in.web.dto;

import com.nawiedk.profile.domain.ProfileStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class ProfileDto {

    @NotNull
    private Long profileId;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 1000)
    private String bio;

    @NotBlank
    private String location;

    @NotBlank
    private String role;

    @NotBlank
    private String imageUrl;

    private ProfileStatus profileStatus;

    @NotEmpty
    private Set<@Valid SkillDto> skills;
}