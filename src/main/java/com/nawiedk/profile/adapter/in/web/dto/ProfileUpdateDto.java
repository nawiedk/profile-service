package com.nawiedk.profile.adapter.in.web.dto;

import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class ProfileUpdateDto {

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

    private Set<@Valid SkillUpdateDto> skills;
}