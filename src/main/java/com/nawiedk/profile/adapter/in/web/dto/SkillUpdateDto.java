package com.nawiedk.profile.adapter.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillUpdateDto {

    //    @NotNull
    private Long skillId;

    @NotBlank
    private String name;

    @Min(0)
    @Max(10)
    private int proficiencyLevel;
}
