package com.nawiedk.profile.adapter.in.web.mapper.response;

import java.util.Set;

public record ProjectResponse(
        Long projectId,
        String name,
        String description,
        Set<SkillResponse> technologies,
        Set<String> urls) {
}