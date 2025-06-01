package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.ProfileStatusEnum;
import com.nawiedk.profile.domain.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private UpdateProfileService updateProfileService;

    @Captor
    private ArgumentCaptor<Profile> profileCaptor;

    private final Long existingProfileId = 1L;
    private UpdateProfileCommand validCommand;
    private Profile existingProfile;
    private Profile updatedProfile;
    private Set<Skill> validSkills;

    @BeforeEach
    void setUp() {
        validSkills = Set.of(
                Skill.skillWithId(new Skill.SkillId(1L), "Java", "Programming", 8)
        );

        existingProfile = Profile.profileWithId(
                new Profile.ProfileId(existingProfileId),
                "Old Name",
                "Old Bio",
                "Old Location",
                "Old Role",
                "http://example.com/old-image.jpg",
                ProfileStatusEnum.ACTIVE,
                validSkills
        );

        validCommand = new UpdateProfileCommand(
                existingProfileId,
                "New Name",
                "New Bio",
                "New Location",
                "New Role",
                ProfileStatusEnum.ON_LEAVE,
                "http://example.com/new-image.jpg",
                validSkills
        );

        updatedProfile = Profile.profileWithId(
                new Profile.ProfileId(existingProfileId),
                validCommand.name(),
                validCommand.bio(),
                validCommand.location(),
                validCommand.role(),
                validCommand.imageUrl(),
                validCommand.profileStatusEnum(),
                validCommand.skills()
        );
    }

    @Test
    void update_WithValidCommand_ShouldUpdateProfile() {
        when(profileRepositoryPort.findById(existingProfileId)).thenReturn(Optional.of(existingProfile));
        when(profileRepositoryPort.save(any(Profile.class))).thenReturn(updatedProfile);

        Profile result = updateProfileService.update(validCommand);

        verify(profileRepositoryPort).save(profileCaptor.capture());
        Profile capturedProfile = profileCaptor.getValue();

        assertThat(capturedProfile.getProfileId()).isPresent();
        assertThat(capturedProfile.getProfileId().get().id()).isEqualTo(existingProfileId);
        assertThat(capturedProfile.getName()).isEqualTo(validCommand.name());
        assertThat(capturedProfile.getBio()).isEqualTo(validCommand.bio());
        assertThat(capturedProfile.getLocation()).isEqualTo(validCommand.location());
        assertThat(capturedProfile.getRole()).isEqualTo(validCommand.role());
        assertThat(capturedProfile.getImageUrl()).isEqualTo(validCommand.imageUrl());
        assertThat(capturedProfile.getProfileStatusEnum()).isEqualTo(validCommand.profileStatusEnum());
        assertThat(capturedProfile.getSkills()).isEqualTo(validCommand.skills());

        assertThat(result).isEqualTo(updatedProfile);
    }

    @Test
    void update_WhenProfileDoesNotExist_ShouldThrowException() {
        when(profileRepositoryPort.findById(existingProfileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateProfileService.update(validCommand))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Profile not found");

        verify(profileRepositoryPort).findById(existingProfileId);
        verify(profileRepositoryPort, never()).save(any());
    }

    @Test
    void update_ShouldPreserveSkillsWithTheirIds() {
        when(profileRepositoryPort.findById(existingProfileId)).thenReturn(Optional.of(existingProfile));
        when(profileRepositoryPort.save(any(Profile.class))).thenReturn(updatedProfile);

        updateProfileService.update(validCommand);

        verify(profileRepositoryPort).save(profileCaptor.capture());
        Profile capturedProfile = profileCaptor.getValue();

        Skill capturedSkill = capturedProfile.getSkills().iterator().next();
        assertThat(capturedSkill.getSkillId()).isPresent();
        assertThat(capturedSkill.getSkillId().get().id()).isEqualTo(1L);
        assertThat(capturedSkill.getName()).isEqualTo("Java");
        assertThat(capturedSkill.getCategory()).isEqualTo("Programming");
        assertThat(capturedSkill.getProficiencyLevel()).isEqualTo(8);
    }
}
