package com.nawiedk.profile.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SkillCreateDto {

    @NotBlank
    private String name;

    @NotBlank
    private String category;
}
