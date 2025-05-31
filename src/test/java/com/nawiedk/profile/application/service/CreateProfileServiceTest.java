package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
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

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private CreateProfileService createProfileService;

    private Skill domainSkillJava;
    private Skill domainSkillReact;
    private Profile savedProfile;

    @BeforeEach
    void setUp() {
        domainSkillJava = Skill.skillWithoutId("Java", "Backend", 8);
        domainSkillReact = Skill.skillWithId(new Skill.SkillId(10L), "React", "Frontend", 7);

        savedProfile = Profile.profileWithId(
                new Profile.ProfileId(1L),
                "John Doe",
                "Experienced Developer",
                "Berlin",
                "Software Engineer",
                "http://example.com/image.png",
                ProfileStatus.ACTIVE,
                Set.of(domainSkillJava, domainSkillReact)
        );
    }

    @Test
    void shouldCreateProfileSuccessfully() {
        CreateProfileCommand validCommand = new CreateProfileCommand(
                "John Doe",
                "Experienced Developer",
                "Berlin",
                "Software Engineer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.png",
                Set.of(domainSkillJava, domainSkillReact)
        );
        when(profileRepositoryPort.save(any(Profile.class))).thenReturn(savedProfile);

        Profile result = createProfileService.create(validCommand);

        assertNotNull(result);
        assertEquals(savedProfile.getProfileId(), result.getProfileId());
        assertEquals(validCommand.name(), result.getName());
        assertEquals(validCommand.skills().size(), result.getSkills().size());

        ArgumentCaptor<Profile> profileCaptor = ArgumentCaptor.forClass(Profile.class);
        verify(profileRepositoryPort, times(1)).save(profileCaptor.capture());

        Profile capturedProfile = profileCaptor.getValue();
        assertTrue(capturedProfile.getProfileId().isEmpty());
        assertEquals(validCommand.name(), capturedProfile.getName());
        assertEquals(validCommand.bio(), capturedProfile.getBio());
        assertEquals(validCommand.location(), capturedProfile.getLocation());
        assertEquals(validCommand.role(), capturedProfile.getRole());
        assertEquals(validCommand.profileStatus(), capturedProfile.getProfileStatus());
        assertEquals(validCommand.imageUrl(), capturedProfile.getImageUrl());
        assertSame(validCommand.skills(), capturedProfile.getSkills());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenCommandIsNullAndPassedToService() {
        assertThrows(NullPointerException.class, () -> createProfileService.create(null));
        verifyNoInteractions(profileRepositoryPort);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingCommandWithNullName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new CreateProfileCommand(
                null,
                "Bio",
                "Location",
                "Role",
                ProfileStatus.ACTIVE,
                "img.url",
                Set.of(domainSkillJava)
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("name: must not be blank"));
        verifyNoInteractions(profileRepositoryPort);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingCommandWithEmptySkillsSet() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new CreateProfileCommand(
                "John Doe",
                "Bio",
                "Location",
                "Role",
                ProfileStatus.ACTIVE,
                "img.url",
                Collections.emptySet()
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("skills: must not be empty"));
        verifyNoInteractions(profileRepositoryPort);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSkillInCommandIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new CreateProfileCommand(
                "Valid Name",
                "Valid Bio",
                "Valid Location",
                "Valid Role",
                ProfileStatus.ACTIVE,
                "valid.url",
                Set.of(Skill.skillWithoutId("", "Category", 5))
        ));
        assertTrue(exception.getMessage().toLowerCase().contains("name: must not be blank"),
                "Exception message should confirm 'name' validation failed for the Skill. Message was: " + exception.getMessage());
        assertFalse(exception.getMessage().toLowerCase().contains("skills["),
                "Exception message should come directly from Skill validation, not be prefixed with 'skills[]'. Message was: " + exception.getMessage());
        verifyNoInteractions(profileRepositoryPort);
    }
}