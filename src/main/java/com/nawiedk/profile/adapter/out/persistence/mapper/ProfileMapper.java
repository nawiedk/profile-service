package com.nawiedk.profile.adapter.out.persistence.mapper;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import org.springframework.stereotype.Component;
import com.nawiedk.profile.domain.Profile.ProfileId;

@Component
public class ProfileMapper {

    public Profile mapToDomainEntity(ProfileJpaEntity entity) {
        return Profile.profileWithId(
                new ProfileId(entity.getProfileId()),
                entity.getName(),
                entity.getBio(),
                entity.getLocation(),
                entity.getRole(),
                entity.getSkills(),
                entity.getImageUrl(),
                entity.getProfileStatus()
        );
    }

    public ProfileJpaEntity mapToJpaEntity(Profile profile) {
        ProfileJpaEntity entity = new ProfileJpaEntity();
        entity.setProfileId(profile.getId().map(ProfileId::id).orElse(null));
        entity.setName(profile.getName());
        entity.setBio(profile.getBio());
        entity.setLocation(profile.getLocation());
        entity.setRole(profile.getRole());
        entity.setImageUrl(profile.getImageUrl());
        entity.setProfileStatus(profile.getProfileStatus());
        entity.setSkills(profile.getSkills());
        return entity;
    }

    /*
    public static void updateEntity(ProfileJpaEntity entity, Profile updated) {
        entity.setName(updated.getName());
        entity.setBio(updated.getBio());
        entity.setLocation(updated.getLocation());
        entity.setRole(updated.getRole());
        entity.setSkills(updated.getSkills());
        entity.setImageUrl(updated.getImageUrl());
        entity.setProfileStatus(updated.getProfileStatus());
    }
    */
}