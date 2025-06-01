package com.nawiedk.profile.adapter.in.web.mapper.response;

import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileResponseMapperTest {

    @Mock
    private SkillResponseMapper skillResponseMapper;

    @Mock
    private Profile profile;

    private ProfileResponseMapper mapper;

    private Skill javaDomainSkill;
    private SkillResponse javaSkillResponse;

    @BeforeEach
    void setUp() {
        mapper = new ProfileResponseMapper(skillResponseMapper);

        javaDomainSkill = Skill.skillWithId(
                new Skill.SkillId(1L),
                "Java",
                "Programming",
                8
        );

        javaSkillResponse = new SkillResponse(1L, "Java", "Programming", 8);
    }

    @Test
    void mapToResponse_whenProfileHasId_shouldMapAllFields() {
        when(profile.getProfileId()).thenReturn(Optional.of(new Profile.ProfileId(1L)));
        when(profile.getName()).thenReturn("John Doe");
        when(profile.getBio()).thenReturn("Test Bio");
        when(profile.getLocation()).thenReturn("Test Location");
        when(profile.getRole()).thenReturn("Developer");
        when(profile.getImageUrl()).thenReturn("http://example.com/image.jpg");
        when(profile.getProfileStatusAsString()).thenReturn("ACTIVE");
        when(profile.getSkills()).thenReturn(Set.of(javaDomainSkill));
        when(skillResponseMapper.mapToResponse(javaDomainSkill)).thenReturn(javaSkillResponse);

        ProfileResponse response = mapper.mapToResponse(profile);

        assertThat(response.profileId()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.bio()).isEqualTo("Test Bio");
        assertThat(response.location()).isEqualTo("Test Location");
        assertThat(response.role()).isEqualTo("Developer");
        assertThat(response.imageUrl()).isEqualTo("http://example.com/image.jpg");
        assertThat(response.profileStatus()).isEqualTo("ACTIVE");
        assertThat(response.skills()).hasSize(1);
        assertThat(response.skills().iterator().next()).isEqualTo(javaSkillResponse);

        verify(profile).getProfileId();
        verify(profile).getName();
        verify(profile).getBio();
        verify(profile).getLocation();
        verify(profile).getRole();
        verify(profile).getImageUrl();
        verify(profile).getProfileStatusAsString();
        verify(profile).getSkills();
        verify(skillResponseMapper).mapToResponse(javaDomainSkill);
    }

    @Test
    void mapToResponse_whenProfileHasNoId_shouldMapWithNullId() {
        when(profile.getProfileId()).thenReturn(Optional.empty());
        when(profile.getName()).thenReturn("Jane Doe");
        when(profile.getBio()).thenReturn("Test Bio");
        when(profile.getLocation()).thenReturn("Test Location");
        when(profile.getRole()).thenReturn("Engineer");
        when(profile.getImageUrl()).thenReturn("http://example.com/image.jpg");
        when(profile.getProfileStatusAsString()).thenReturn("ACTIVE");
        when(profile.getSkills()).thenReturn(Set.of(javaDomainSkill));
        when(skillResponseMapper.mapToResponse(javaDomainSkill)).thenReturn(javaSkillResponse);

        ProfileResponse response = mapper.mapToResponse(profile);

        assertThat(response.profileId()).isNull();
        assertThat(response.name()).isEqualTo("Jane Doe");
        assertThat(response.skills()).hasSize(1);

        verify(profile).getProfileId();
        verify(profile).getName();
        verify(profile).getBio();
        verify(profile).getLocation();
        verify(profile).getRole();
        verify(profile).getImageUrl();
        verify(profile).getProfileStatusAsString();
        verify(profile).getSkills();
        verify(skillResponseMapper).mapToResponse(javaDomainSkill);
    }

    @Test
    void mapToResponse_whenProfileHasNoSkills_shouldMapEmptySkillSet() {
        when(profile.getProfileId()).thenReturn(Optional.of(new Profile.ProfileId(1L)));
        when(profile.getName()).thenReturn("John Doe");
        when(profile.getBio()).thenReturn("Test Bio");
        when(profile.getLocation()).thenReturn("Test Location");
        when(profile.getRole()).thenReturn("Developer");
        when(profile.getImageUrl()).thenReturn("http://example.com/image.jpg");
        when(profile.getProfileStatusAsString()).thenReturn("ACTIVE");
        when(profile.getSkills()).thenReturn(new HashSet<>());

        ProfileResponse response = mapper.mapToResponse(profile);

        assertThat(response.skills()).isEmpty();

        verify(profile).getProfileId();
        verify(profile).getName();
        verify(profile).getBio();
        verify(profile).getLocation();
        verify(profile).getRole();
        verify(profile).getImageUrl();
        verify(profile).getProfileStatusAsString();
        verify(profile).getSkills();
        verifyNoInteractions(skillResponseMapper);
    }

    @Test
    void mapToResponse_whenProfileHasMultipleSkills_shouldMapAllSkills() {
        Skill springDomainSkill = Skill.skillWithId(
                new Skill.SkillId(2L),
                "Spring",
                "Framework",
                7
        );

        SkillResponse springSkillResponse = new SkillResponse(2L, "Spring", "Framework", 7);

        when(profile.getProfileId()).thenReturn(Optional.of(new Profile.ProfileId(1L)));
        when(profile.getName()).thenReturn("John Doe");
        when(profile.getBio()).thenReturn("Test Bio");
        when(profile.getLocation()).thenReturn("Test Location");
        when(profile.getRole()).thenReturn("Developer");
        when(profile.getImageUrl()).thenReturn("http://example.com/image.jpg");
        when(profile.getProfileStatusAsString()).thenReturn("ACTIVE");
        when(profile.getSkills()).thenReturn(Set.of(javaDomainSkill, springDomainSkill));
        when(skillResponseMapper.mapToResponse(javaDomainSkill)).thenReturn(javaSkillResponse);
        when(skillResponseMapper.mapToResponse(springDomainSkill)).thenReturn(springSkillResponse);

        ProfileResponse response = mapper.mapToResponse(profile);

        assertThat(response.skills()).hasSize(2);
        assertThat(response.skills()).containsExactlyInAnyOrder(javaSkillResponse, springSkillResponse);

        verify(profile).getProfileId();
        verify(profile).getName();
        verify(profile).getBio();
        verify(profile).getLocation();
        verify(profile).getRole();
        verify(profile).getImageUrl();
        verify(profile).getProfileStatusAsString();
        verify(profile).getSkills();
        verify(skillResponseMapper).mapToResponse(javaDomainSkill);
        verify(skillResponseMapper).mapToResponse(springDomainSkill);
    }

    @Test
    void mapToResponse_whenProfileStatusIsOnLeave_shouldMapStatusCorrectly() {
        when(profile.getProfileId()).thenReturn(Optional.of(new Profile.ProfileId(1L)));
        when(profile.getName()).thenReturn("John Doe");
        when(profile.getBio()).thenReturn("Test Bio");
        when(profile.getLocation()).thenReturn("Test Location");
        when(profile.getRole()).thenReturn("Developer");
        when(profile.getImageUrl()).thenReturn("http://example.com/image.jpg");
        when(profile.getProfileStatusAsString()).thenReturn("ON_LEAVE");
        when(profile.getSkills()).thenReturn(Set.of(javaDomainSkill));

        ProfileResponse response = mapper.mapToResponse(profile);

        assertThat(response.profileStatus()).isEqualTo("ON_LEAVE");

        verify(profile).getProfileId();
        verify(profile).getName();
        verify(profile).getBio();
        verify(profile).getLocation();
        verify(profile).getRole();
        verify(profile).getImageUrl();
        verify(profile).getProfileStatusAsString();
        verify(profile).getSkills();
        verify(skillResponseMapper).mapToResponse(javaDomainSkill);
    }

    @Test
    void mapToResponse_whenProfileStatusIsInactive_shouldMapStatusCorrectly() {
        when(profile.getProfileId()).thenReturn(Optional.of(new Profile.ProfileId(1L)));
        when(profile.getName()).thenReturn("John Doe");
        when(profile.getBio()).thenReturn("Test Bio");
        when(profile.getLocation()).thenReturn("Test Location");
        when(profile.getRole()).thenReturn("Developer");
        when(profile.getImageUrl()).thenReturn("http://example.com/image.jpg");
        when(profile.getProfileStatusAsString()).thenReturn("INACTIVE");
        when(profile.getSkills()).thenReturn(Set.of(javaDomainSkill));

        ProfileResponse response = mapper.mapToResponse(profile);

        assertThat(response.profileStatus()).isEqualTo("INACTIVE");

        verify(profile).getProfileId();
        verify(profile).getName();
        verify(profile).getBio();
        verify(profile).getLocation();
        verify(profile).getRole();
        verify(profile).getImageUrl();
        verify(profile).getProfileStatusAsString();
        verify(profile).getSkills();
        verify(skillResponseMapper).mapToResponse(javaDomainSkill);
    }
} 