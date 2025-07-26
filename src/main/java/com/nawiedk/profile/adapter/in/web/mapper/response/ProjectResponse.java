package com.nawiedk.profile.adapter.in.web.mapper.response;

import java.util.List;

public record ProjectResponse(
        String name,
        String previewText,
        String[] description,
        List<SkillResponse> technologies,
        String githubUrl,
        String projectStatus) {
}