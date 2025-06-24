package com.nawiedk.profile.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillUpdateDto {

    //    @NotNull
    private Long skillId;

    @NotBlank
    private String name;

    @NotBlank
    private String category;
}
