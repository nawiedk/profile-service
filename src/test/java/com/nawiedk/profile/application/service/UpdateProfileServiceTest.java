package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.domain.ProfileStatus;
import com.nawiedk.profile.domain.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private UpdateProfileService updateProfileService;

    private Long existingProfileId;
    private Profile mockExistingProfile;
    private Set<Skill> validSkills;

    @BeforeEach
    void setUp() {
        existingProfileId = 1L;

        Skill validSkill = new Skill(null, "Java", "Programming", 5);
        validSkills = new HashSet<>();
        validSkills.add(validSkill);

        mockExistingProfile = new Profile(
                new Profile.ProfileId(existingProfileId),
                "Old Name",
                "Old Bio",
                "Old Location",
                "Old Role",
                "http://oldimage.url",
                ProfileStatus.ACTIVE,
                validSkills
        );
    }

    @Test
    void update_givenValidCommandAndExistingProfile_constructsProfileFromCommandAndSaves() {
        UpdateProfileCommand command = new UpdateProfileCommand(
                existingProfileId,
                "New Name",
                "New Bio",
                "New Location",
                "New Role",
                ProfileStatus.ON_LEAVE,
                "http://newimage.url",
                validSkills
        );

        when(profileRepositoryPort.findById(existingProfileId)).thenReturn(Optional.of(mockExistingProfile));
        when(profileRepositoryPort.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Profile resultProfile = updateProfileService.update(command);

        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepositoryPort).save(profileCaptor.capture());
        Profile capturedProfile = profileCaptor.getValue();

        assertNotNull(resultProfile);
        assertTrue(capturedProfile.getProfileId().isPresent(), "ProfileId should be present");
        assertEquals(command.id(), capturedProfile.getProfileId().get().id(), "Profile ID should match command ID");
        assertEquals(command.name(), capturedProfile.getName());
        assertEquals(command.bio(), capturedProfile.getBio());
        assertEquals(command.location(), capturedProfile.getLocation());
        assertEquals(command.role(), capturedProfile.getRole());
        assertEquals(command.profileStatus(), capturedProfile.getProfileStatus());
        assertEquals(command.imageUrl(), capturedProfile.getImageUrl());
        assertEquals(command.skills(), capturedProfile.getSkills());

        assertEquals(capturedProfile, resultProfile);

        verify(profileRepositoryPort, times(1)).findById(existingProfileId);
        verify(profileRepositoryPort, times(1)).save(any(Profile.class));
    }

    @Test
    void update_givenNonExistingProfileId_throwsRuntimeException() {
        Long nonExistentId = 2L;
        UpdateProfileCommand command = new UpdateProfileCommand(
                nonExistentId, "New Name", "New Bio", "New Location", "New Role",
                ProfileStatus.ACTIVE, "http://newimage.url", validSkills
        );

        when(profileRepositoryPort.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> updateProfileService.update(command));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepositoryPort, times(1)).findById(nonExistentId);
        verify(profileRepositoryPort, never()).save(any(Profile.class));
    }

    @Test
    void update_givenNullCommand_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> updateProfileService.update(null));
        verifyNoInteractions(profileRepositoryPort);
    }

    @Test
    void update_commandWithNullProfileStatus_throwsIllegalArgumentExceptionFromProfileDomain() {
        UpdateProfileCommand commandWithNullStatus = new UpdateProfileCommand(
                existingProfileId,
                "New Name",
                "New Bio",
                "New Location",
                "New Role",
                null,
                "http://newimage.url",
                validSkills
        );

        when(profileRepositoryPort.findById(existingProfileId)).thenReturn(Optional.of(mockExistingProfile));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> updateProfileService.update(commandWithNullStatus));

        assertTrue(exception.getMessage().contains("profileStatus: must not be null") || exception.getMessage().contains("status: must not be null"));
        verify(profileRepositoryPort, times(1)).findById(existingProfileId);
        verify(profileRepositoryPort, never()).save(any(Profile.class));
    }

    @Test
    void updateProfileCommand_constructor_throwsIllegalArgumentException_whenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new UpdateProfileCommand(null, "Name", "Bio", "Location", "Role", ProfileStatus.ACTIVE, "http://image.url", validSkills));
        assertTrue(exception.getMessage().contains("id: must not be null"));
    }

    @Test
    void updateProfileCommand_constructor_throwsIllegalArgumentException_whenNameIsBlank() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new UpdateProfileCommand(existingProfileId, "", "Bio", "Location", "Role", ProfileStatus.ACTIVE, "http://image.url", validSkills));
        assertTrue(exception.getMessage().contains("name: must not be blank"));
        assertTrue(exception.getMessage().contains("name: size must be between 2 and 100"));
    }

    @Test
    void updateProfileCommand_constructor_throwsIllegalArgumentException_whenSkillsAreEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new UpdateProfileCommand(existingProfileId, "Name", "Bio", "Location", "Role", ProfileStatus.ACTIVE, "http://image.url", new HashSet<>()));
        assertTrue(exception.getMessage().contains("skills: must not be empty"));
    }

    @Test
    void updateProfileCommand_constructor_throwsIllegalArgumentException_whenSkillInSetIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Skill(null, "", "Category", 3));
        assertTrue(exception.getMessage().contains("name: must not be blank"));
    }
}
