package com.nawiedk.profile.adapter.out.persistence.mapper;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileJpaEntity;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.Profile.ProfileId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProfileMapper {

    private final SkillMapper skillMapper;
    private final ProjectMapper projectMapper;

    public Profile mapToDomainEntity(ProfileJpaEntity entity) {
        return Profile.profileWithId(
                new ProfileId(entity.getProfileId()),
                entity.getName(),
                entity.getBio(),
                entity.getLocation(),
                entity.getRole(),
                entity.getImageUrl(),
                entity.getGithubUrl(),
                entity.getLinkedInUrl(),
                entity.getProfileStatus(),
                entity.getSkills().stream().map(skillMapper::mapToDomainEntity).collect(Collectors.toList()),
                entity.getProjects().stream().map(projectMapper::mapToDomainEntity).collect(Collectors.toList()),
                new Profile.UserId(entity.getUserId())
        );
    }

    public ProfileJpaEntity mapToJpaEntity(Profile profile) {
        ProfileJpaEntity entity = new ProfileJpaEntity();
        entity.setProfileId(profile.getProfileId().map(ProfileId::profileId).orElse(null));
        entity.setName(profile.getName());
        entity.setBio(profile.getBio());
        entity.setLocation(profile.getLocation());
        entity.setRole(profile.getRole());
        entity.setImageUrl(profile.getImageUrl());
        entity.setGithubUrl(profile.getGithubUrl());
        entity.setLinkedInUrl(profile.getLinkedInUrl());
        entity.setProfileStatus(profile.getProfileStatus());
        entity.setSkills(profile.getSkills().stream().map(skillMapper::mapToJpaEntity).collect(Collectors.toList()));
        entity.setProjects(profile.getProjects().stream().map(projectMapper::mapToJpaEntity).collect(Collectors.toList()));
        entity.setUserId(profile.getUserId().map(Profile.UserId::userId).orElse(null));
        return entity;
    }
}