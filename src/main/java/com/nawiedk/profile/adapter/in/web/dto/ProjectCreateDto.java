package com.nawiedk.profile.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class ProjectCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    @Size(min = 50, max = 1000)
    private String description;

    private Set<@Valid SkillUpdateDto> technologies;

    private Set<@NotBlank String> urls;
}
