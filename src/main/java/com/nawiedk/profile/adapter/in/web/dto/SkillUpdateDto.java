package com.nawiedk.profile.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SkillUpdateDto {

    @NotNull
    private Long skillId;

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @Min(0)
    @Max(10)
    private int proficiencyLevel;
}
