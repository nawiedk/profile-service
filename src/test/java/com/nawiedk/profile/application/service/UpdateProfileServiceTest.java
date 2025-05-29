package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.domain.Profile.ProfileId;
import com.nawiedk.profile.domain.ProfileStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UpdateProfileServiceTest {

    @Autowired
    private ProfileRepositoryPort repositoryPort;

    @Autowired
    private UpdateProfileService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProfileRepositoryPort profileRepositoryPort() {
            return mock(ProfileRepositoryPort.class);
        }
    }

    @Test
    void updateProfileSuccessfullyWhenExists() {
        Long profileId = 1L;
        Profile existingProfile = Profile.profileWithId(
                new ProfileId(profileId),
                "Old Name",
                "Old Bio",
                "Old Location",
                "Old Role",
                List.of("Old Skill"),
                "old_image.jpg",
                ProfileStatus.ACTIVE
        );

        UpdateProfileCommand command = new UpdateProfileCommand(
                profileId,
                "New Name",
                "New Bio",
                "New Location",
                "New Role",
                Arrays.asList("New Skill", "Another Skill"),
                "new_image.jpg",
                ProfileStatus.ACTIVE
        );

        Profile updatedProfile = Profile.profileWithId(
                new ProfileId(profileId),
                "New Name",
                "New Bio",
                "New Location",
                "New Role",
                Arrays.asList("New Skill", "Another Skill"),
                "new_image.jpg",
                ProfileStatus.ACTIVE
        );

        when(repositoryPort.findById(profileId)).thenReturn(Optional.of(existingProfile));
        when(repositoryPort.save(any(Profile.class))).thenReturn(updatedProfile);

        Profile result = service.update(command);

        assertNotNull(result);
        assertEquals(profileId, result.getId().map(ProfileId::id).orElse(null));
        assertEquals("New Name", result.getName());
        assertEquals("New Bio", result.getBio());
        assertEquals(ProfileStatus.ACTIVE, result.getProfileStatus());
        assertTrue(result.getSkills().contains("New Skill"));

        verify(repositoryPort, times(1)).findById(profileId);
        verify(repositoryPort, times(1)).save(any(Profile.class));
    }

    @Test
    void updateProfileThrowsExceptionWhenProfileNotFound() {
        Long profileId = 99L;
        UpdateProfileCommand command = new UpdateProfileCommand(
                profileId,
                "Name",
                "Bio",
                "Location",
                "Role",
                List.of("Skill"),
                "image.jpg",
                ProfileStatus.ACTIVE
        );

        when(repositoryPort.findById(profileId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.update(command));
        assertEquals("Profile not found", exception.getMessage());

        verify(repositoryPort, times(1)).findById(profileId);
        verify(repositoryPort, never()).save(any(Profile.class));
    }
}