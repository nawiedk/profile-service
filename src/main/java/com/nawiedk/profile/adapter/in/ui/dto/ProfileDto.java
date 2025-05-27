package com.nawiedk.profile.adapter.in.ui.dto;

import jakarta.validation.constraints.*;
import java.util.List;

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
    public List<@NotBlank String> skills;

    @NotBlank
    public String imageUrl;

    public boolean profileStatus;
}