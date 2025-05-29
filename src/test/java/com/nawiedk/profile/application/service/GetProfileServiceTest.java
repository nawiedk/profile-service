package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.GetProfileCommand;
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
class GetProfileServiceTest {

    @Autowired
    private ProfileRepositoryPort repositoryPort;

    @Autowired
    private GetProfileService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProfileRepositoryPort profileRepositoryPort() {
            return mock(ProfileRepositoryPort.class);
        }
    }

    @Test
    void getProfileByIdReturnsProfileWhenFound() {
        Long profileId = 1L;
        GetProfileCommand command = new GetProfileCommand(profileId);
        Profile expectedProfile = Profile.profileWithId(
                new ProfileId(profileId),
                "Test User",
                "Bio",
                "Location",
                "Role",
                Arrays.asList("Skill1", "Skill2"),
                "image.jpg",
                ProfileStatus.ACTIVE
        );

        when(repositoryPort.findById(profileId)).thenReturn(Optional.of(expectedProfile));

        Profile result = service.findProfileById(command);

        assertNotNull(result);
        assertEquals(expectedProfile.getId(), result.getId());
        assertEquals(expectedProfile.getName(), result.getName());
        verify(repositoryPort, times(1)).findById(profileId);
    }

    @Test
    void getProfileByIdThrowsExceptionWhenNotFound() {
        Long profileId = 99L;
        GetProfileCommand command = new GetProfileCommand(profileId);

        when(repositoryPort.findById(profileId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.findProfileById(command));
        assertEquals("Profile not found", exception.getMessage());
        verify(repositoryPort, times(1)).findById(profileId);
    }

    @Test
    void getAllProfilesReturnsListOfProfiles() {
        List<Profile> expectedProfiles = Arrays.asList(
                Profile.profileWithId(new ProfileId(1L), "User A", "Bio A", "Loc A", "Role A", List.of("S1"), "imgA.jpg", ProfileStatus.ACTIVE),
                Profile.profileWithId(new ProfileId(2L), "User B", "Bio B", "Loc B", "Role B", List.of("S2"), "imgB.jpg", ProfileStatus.INACTIVE)
        );

        when(repositoryPort.findAll()).thenReturn(expectedProfiles);

        List<Profile> result = service.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedProfiles.get(0).getName(), result.get(0).getName());
        assertEquals(expectedProfiles.get(1).getName(), result.get(1).getName());
        verify(repositoryPort, times(1)).findAll();
    }

    @Test
    void getAllProfilesReturnsEmptyListWhenNoProfilesExist() {
        List<Profile> expectedProfiles = List.of();

        when(repositoryPort.findAll()).thenReturn(expectedProfiles);

        List<Profile> result = service.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repositoryPort, times(1)).findAll();
    }
}