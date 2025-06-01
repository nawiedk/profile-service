package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private CreateProfileService createProfileService;

    @Captor
    private ArgumentCaptor<Profile> profileCaptor;

    private CreateProfileCommand validCommand;
    private Profile savedProfile;
    private Set<Skill> validSkills;

    @BeforeEach
    void setUp() {
        validSkills = Set.of(
                Skill.skillWithId(new Skill.SkillId(1L), "Java", "Programming", 8)
        );

        validCommand = new CreateProfileCommand(
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                ProfileStatusEnum.ACTIVE,
                "http://example.com/image.jpg",
                validSkills
        );

        savedProfile = Profile.profileWithId(
                new Profile.ProfileId(1L),
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                "http://example.com/image.jpg",
                ProfileStatusEnum.ACTIVE,
                validSkills
        );
    }

    @Test
    void create_WithValidCommand_ShouldCreateProfile() {
        when(profileRepositoryPort.save(any(Profile.class))).thenReturn(savedProfile);

        Profile createdProfile = createProfileService.create(validCommand);

        verify(profileRepositoryPort).save(profileCaptor.capture());
        Profile capturedProfile = profileCaptor.getValue();

        assertThat(capturedProfile.getProfileId()).isEmpty();
        assertThat(capturedProfile.getName()).isEqualTo(validCommand.name());
        assertThat(capturedProfile.getBio()).isEqualTo(validCommand.bio());
        assertThat(capturedProfile.getLocation()).isEqualTo(validCommand.location());
        assertThat(capturedProfile.getRole()).isEqualTo(validCommand.role());
        assertThat(capturedProfile.getImageUrl()).isEqualTo(validCommand.imageUrl());
        assertThat(capturedProfile.getProfileStatusEnum()).isEqualTo(validCommand.profileStatusEnum());
        assertThat(capturedProfile.getSkills()).isEqualTo(validCommand.skills());

        assertThat(createdProfile.getProfileId()).isPresent();
        assertThat(createdProfile.getProfileId().get().id()).isEqualTo(1L);
    }

    @Test
    void create_ShouldPreserveSkillsWithTheirIds() {
        when(profileRepositoryPort.save(any(Profile.class))).thenReturn(savedProfile);

        createProfileService.create(validCommand);

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
