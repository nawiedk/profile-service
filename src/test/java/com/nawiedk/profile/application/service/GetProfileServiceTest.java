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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private GetProfileService getProfileService;

    private Profile testProfile;
    private final Long existingProfileId = 1L;

    @BeforeEach
    void setUp() {
        Set<Skill> skills = Set.of(
                Skill.skillWithId(new Skill.SkillId(1L), "Java", "Programming", 8)
        );

        testProfile = Profile.profileWithId(
                new Profile.ProfileId(existingProfileId),
                "John Doe",
                "Test Bio",
                "Test Location",
                "Developer",
                "http://example.com/image.jpg",
                ProfileStatus.ACTIVE,
                skills
        );
    }

    @Test
    void findProfileById_WhenProfileExists_ShouldReturnProfile() {
        when(profileRepositoryPort.findById(existingProfileId))
                .thenReturn(Optional.of(testProfile));

        Profile foundProfile = getProfileService.findProfileById(new GetProfileCommand(existingProfileId));

        assertThat(foundProfile).isNotNull();
        assertThat(foundProfile.getProfileId()).isPresent();
        assertThat(foundProfile.getProfileId().get().id()).isEqualTo(existingProfileId);
        verify(profileRepositoryPort).findById(existingProfileId);
    }

    @Test
    void findProfileById_WhenProfileDoesNotExist_ShouldThrowException() {
        when(profileRepositoryPort.findById(existingProfileId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> getProfileService.findProfileById(new GetProfileCommand(existingProfileId)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Profile not found");

        verify(profileRepositoryPort).findById(existingProfileId);
    }

    @Test
    void findAll_ShouldReturnAllProfiles() {
        List<Profile> profiles = List.of(testProfile);
        when(profileRepositoryPort.findAll()).thenReturn(profiles);

        List<Profile> foundProfiles = getProfileService.findAll();

        assertThat(foundProfiles).hasSize(1);
        assertThat(foundProfiles.get(0).getProfileId().get().id()).isEqualTo(existingProfileId);
        verify(profileRepositoryPort).findAll();
    }

    @Test
    void findAll_WhenNoProfiles_ShouldReturnEmptyList() {
        when(profileRepositoryPort.findAll()).thenReturn(List.of());

        List<Profile> foundProfiles = getProfileService.findAll();

        assertThat(foundProfiles).isEmpty();
        verify(profileRepositoryPort).findAll();
    }
}
