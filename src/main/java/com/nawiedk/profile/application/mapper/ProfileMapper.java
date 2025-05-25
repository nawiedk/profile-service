package com.nawiedk.profile.application.mapper;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.out.persistence.dao.ProfileEntity;

public class ProfileMapper {

    public static Profile toDomain(ProfileEntity entity) {
        return new Profile(
                entity.getId(),
                entity.getName(),
                entity.getBio(),
                entity.getLocation(),
                entity.getRole(),
                entity.getSkills(),
                entity.getImageUrl(),
                entity.getProfileStatus()
        );
    }

    public static ProfileEntity toEntity(Profile profile) {
        return new ProfileEntity(
                profile.getId(),
                profile.getName(),
                profile.getBio(),
                profile.getLocation(),
                profile.getRole(),
                profile.getSkills(),
                profile.getImageUrl(),
                profile.getProfileStatus()
        );
    }
}