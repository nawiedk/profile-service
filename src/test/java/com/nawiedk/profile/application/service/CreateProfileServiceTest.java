package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.domain.Profile.ProfileId;
import com.nawiedk.profile.domain.ProfileStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProfileServiceTest {

    @Mock
    ProfileRepositoryPort repositoryPort;

    @InjectMocks
    CreateProfileService service;

    CreateProfileCommand command;

    @BeforeEach
    void setup() {
        command = new CreateProfileCommand(
                "john_doe",
                "Full Stack Developer",
                "Berlin",
                "Senior Developer",
                List.of("JavaScript", "React", "Node.js", "MongoDB"),
                "john_doe.png",
                ProfileStatus.ACTIVE
        );
    }

    @Test
    void shouldCreateProfileSuccessfully() {
        Profile savedProfile = Profile.profileWithId(
                new ProfileId(1L),
                command.name(),
                command.bio(),
                command.location(),
                command.role(),
                command.skills(),
                command.imageUrl(),
                command.profileStatus()
        );

        when(repositoryPort.save(any(Profile.class))).thenReturn(savedProfile);

        Profile result = service.create(command);

        assertNotNull(result);
        assertEquals(savedProfile.getId(), result.getId());
        assertEquals(command.name(), result.getName());
        assertEquals(command.profileStatus(), result.getProfileStatus());

        verify(repositoryPort, times(1)).save(any(Profile.class));
    }

    @Test
    void shouldPassCorrectProfileToRepository() {
        when(repositoryPort.save(any(Profile.class))).thenReturn(mock(Profile.class));

        service.create(command);

        verify(repositoryPort).save(argThat(profile ->
                profile.getName().equals(command.name()) &&
                        profile.getBio().equals(command.bio()) &&
                        profile.getLocation().equals(command.location()) &&
                        profile.getRole().equals(command.role()) &&
                        profile.getSkills().equals(command.skills()) &&
                        profile.getImageUrl().equals(command.imageUrl()) &&
                        profile.getProfileStatus() == command.profileStatus()
        ));
    }
}
