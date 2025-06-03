package com.nawiedk.profile.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @Min(0)
    @Max(10)
    private int proficiencyLevel;
}
