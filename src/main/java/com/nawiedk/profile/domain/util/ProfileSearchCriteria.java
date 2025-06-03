package com.nawiedk.profile.domain.util;

public record ProfileSearchCriteria(
        String name,
        String role,
        String location,
        String skill,
        ProfileStatus status) {
}
