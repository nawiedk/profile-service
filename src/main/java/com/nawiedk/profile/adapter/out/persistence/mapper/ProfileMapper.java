package com.nawiedk.profile.adapter.out.persistence.mapper;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileEntity;

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
                entity.isProfileStatus()
        );
    }

    public static ProfileEntity toEntity(Profile profile) {
        return new ProfileEntity(
                profile.getId(),
                profile.getName(),
                profile.getBio(),
                profile.getLocation(),
                profile.getRole(),
                profile.getImageUrl(),
                profile.getProfileStatus(),
                profile.getSkills()
        );
    }

    public static void updateEntity(ProfileEntity entity, Profile updated) {
        entity.setName(updated.getName());
        entity.setBio(updated.getBio());
        entity.setLocation(updated.getLocation());
        entity.setRole(updated.getRole());
        entity.setSkills(updated.getSkills());
        entity.setImageUrl(updated.getImageUrl());
        entity.setProfileStatus(updated.getProfileStatus());
    }

}