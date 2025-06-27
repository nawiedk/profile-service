package com.nawiedk.profile.adapter.in.web.mapper.response;

import java.util.List;

public record ProfileResponse(Long profileId,
                              String name,
                              String[] bio,
                              String location,
                              String role,
                              String imageUrl,
                              String githubUrl,
                              String linkedInUrl,
                              String profileStatus,
                              List<SkillResponse> skills,
                              List<ProjectResponse> projects,
                              Long userId) {
}