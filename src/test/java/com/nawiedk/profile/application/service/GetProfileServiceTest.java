package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.GetProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.domain.ProfileStatus;
import com.nawiedk.profile.domain.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private GetProfileService getProfileService;

    private Profile testProfile;
    private GetProfileCommand getProfileCommandWithValidId;

    @BeforeEach
    void setUp() {
        Profile.ProfileId profileId = new Profile.ProfileId(1L);
        Skill javaSkill = Skill.skillWithoutId("Java", "Backend", 8);
        testProfile = Profile.profileWithId(
                profileId,
                "John Doe",
                "Software Developer",
                "Berlin",
                "Developer",
                "image.url",
                ProfileStatus.ACTIVE,
                Set.of(javaSkill)
        );
        getProfileCommandWithValidId = new GetProfileCommand(1L);
    }

    @Test
    void findProfileByIdShouldReturnProfileWhenFound() {
        when(profileRepositoryPort.findById(1L)).thenReturn(Optional.of(testProfile));

        Profile foundProfile = getProfileService.findProfileById(getProfileCommandWithValidId);

        assertNotNull(foundProfile);
        assertEquals(testProfile.getName(), foundProfile.getName());
        assertEquals(testProfile.getBio(), foundProfile.getBio());
        verify(profileRepositoryPort, times(1)).findById(1L);
    }

    @Test
    void findProfileByIdShouldThrowRuntimeExceptionWhenProfileNotFound() {
        when(profileRepositoryPort.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> getProfileService.findProfileById(getProfileCommandWithValidId));

        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepositoryPort, times(1)).findById(1L);
    }

    @Test
    void findProfileByIdShouldThrowNullPointerExceptionWhenCommandIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> getProfileService.findProfileById(null));
        verifyNoInteractions(profileRepositoryPort);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingCommandWithNullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new GetProfileCommand(null));
        assertTrue(exception.getMessage().contains("Validation failed: id: must not be null"));
        verifyNoInteractions(profileRepositoryPort);
    }

    @Test
    void findAllShouldReturnListOfProfiles() {
        when(profileRepositoryPort.findAll()).thenReturn(List.of(testProfile));

        List<Profile> profiles = getProfileService.findAll();

        assertNotNull(profiles);
        assertEquals(1, profiles.size());
        assertEquals(testProfile.getName(), profiles.getFirst().getName());
        verify(profileRepositoryPort, times(1)).findAll();
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoProfilesExist() {
        when(profileRepositoryPort.findAll()).thenReturn(Collections.emptyList());

        List<Profile> profiles = getProfileService.findAll();

        assertNotNull(profiles);
        assertTrue(profiles.isEmpty());
        verify(profileRepositoryPort, times(1)).findAll();
    }
}