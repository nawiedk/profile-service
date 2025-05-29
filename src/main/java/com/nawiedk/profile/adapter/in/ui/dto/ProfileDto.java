package com.nawiedk.profile.adapter.in.ui.dto;

import com.nawiedk.profile.domain.ProfileStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProfileDto {

    @NotNull
    public Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    public String name;

    @Size(max = 1000)
    public String bio;

    @NotBlank
    public String location;

    @NotBlank
    public String role;

    @NotEmpty
    public Set<@NotBlank String> skills;

    @NotBlank
    public String imageUrl;

    public ProfileStatus profileStatus;
}