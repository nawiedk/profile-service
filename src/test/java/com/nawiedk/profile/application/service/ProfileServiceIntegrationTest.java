package com.nawiedk.profile.application.service;

import com.nawiedk.profile.adapter.out.persistence.jpaentity.SkillJpaEntity;
import com.nawiedk.profile.adapter.out.persistence.repository.SkillRepository;
import com.nawiedk.profile.application.port.in.command.profile.CreateProfileCommand;
import com.nawiedk.profile.application.port.in.command.profile.DeleteProfileCommand;
import com.nawiedk.profile.application.port.in.command.profile.GetProfileCommand;
import com.nawiedk.profile.application.port.in.command.profile.UpdateProfileCommand;
import com.nawiedk.profile.application.port.in.usecase.profile.CreateProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.profile.DeleteProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.profile.GetProfileUseCase;
import com.nawiedk.profile.application.port.in.usecase.profile.UpdateProfileUseCase;
import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.Skill;
import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProfileServiceIntegrationTest {

    @Autowired
    private CreateProfileUseCase createProfileUseCase;

    @Autowired
    private GetProfileUseCase getProfileUseCase;

    @Autowired
    private UpdateProfileUseCase updateProfileUseCase;

    @Autowired
    private DeleteProfileUseCase deleteProfileUseCase;

    @Autowired
    private SkillRepository skillRepository;

    private SkillJpaEntity javaSkill;
    private SkillJpaEntity springSkill;
    private SkillJpaEntity uiuxSkill;

    @BeforeEach
    void setUp() {
        skillRepository.deleteAll();

        javaSkill = skillRepository.save(new SkillJpaEntity(null, "Java", "Programming", 8));
        springSkill = skillRepository.save(new SkillJpaEntity(null, "Spring", "Framework", 7));
        uiuxSkill = skillRepository.save(new SkillJpaEntity(null, "UI/UX", "Design", 9));
    }

    @Test
    void shouldPerformFullProfileLifecycle() {
        // Create a profile
        Set<Skill> initialSkills = Set.of(
                Skill.skillWithId(new Skill.SkillId(javaSkill.getSkillId()), "Java", "Programming", 8)
        );

        CreateProfileCommand createCommand = new CreateProfileCommand(
                "John Doe",
                "Initial Bio",
                "Initial Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                initialSkills
        );

        Profile createdProfile = createProfileUseCase.create(createCommand);
        assertThat(createdProfile.getProfileId()).isPresent();
        Long profileId = createdProfile.getProfileId().get().profileId();

        // Retrieve the created profile
        Profile retrievedProfile = getProfileUseCase.findProfileByProfileId(new GetProfileCommand(profileId));
        assertThat(retrievedProfile.getName()).isEqualTo("John Doe");
        assertThat(retrievedProfile.getProfileStatus()).isEqualTo(ProfileStatus.ACTIVE);

        // Update the profile
        Set<Skill> updatedSkills = Set.of(
                Skill.skillWithId(new Skill.SkillId(javaSkill.getSkillId()), "Java", "Programming", 9),
                Skill.skillWithId(new Skill.SkillId(springSkill.getSkillId()), "Spring", "Framework", 7)
        );

        UpdateProfileCommand updateCommand = new UpdateProfileCommand(
                profileId,
                "John Doe Updated",
                "Updated Bio",
                "Updated Location",
                "Senior Developer",
                ProfileStatus.ON_LEAVE,
                "http://example.com/new-image.jpg",
                updatedSkills
        );

        Profile updatedProfile = updateProfileUseCase.update(updateCommand);
        assertThat(updatedProfile.getName()).isEqualTo("John Doe Updated");
        assertThat(updatedProfile.getProfileStatus()).isEqualTo(ProfileStatus.ON_LEAVE);
        assertThat(updatedProfile.getSkills()).hasSize(2);

        // Verify the update through get
        Profile retrievedUpdatedProfile = getProfileUseCase.findProfileByProfileId(new GetProfileCommand(profileId));
        assertThat(retrievedUpdatedProfile.getRole()).isEqualTo("Senior Developer");
        assertThat(retrievedUpdatedProfile.getSkills()).hasSize(2);

        // Delete the profile
        deleteProfileUseCase.deleteByProfileId(new DeleteProfileCommand(profileId));

        // Verify deletion
        assertThatThrownBy(() -> getProfileUseCase.findProfileByProfileId(new GetProfileCommand(profileId)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Profile not found");
    }

    @Test
    void shouldHandleMultipleProfiles() {
        // Create first profile
        CreateProfileCommand firstCommand = new CreateProfileCommand(
                "First User",
                "First Bio",
                "First Location",
                "Developer",
                ProfileStatus.ACTIVE,
                "http://example.com/first.jpg",
                Set.of(Skill.skillWithId(new Skill.SkillId(javaSkill.getSkillId()), "Java", "Programming", 8))
        );
        createProfileUseCase.create(firstCommand);

        // Create second profile
        CreateProfileCommand secondCommand = new CreateProfileCommand(
                "Second User",
                "Second Bio",
                "Second Location",
                "Designer",
                ProfileStatus.ACTIVE,
                "http://example.com/second.jpg",
                Set.of(Skill.skillWithId(new Skill.SkillId(uiuxSkill.getSkillId()), "UI/UX", "Design", 9))
        );
        createProfileUseCase.create(secondCommand);

        // Retrieve all profiles
        List<Profile> allProfiles = getProfileUseCase.findAll();
        assertThat(allProfiles).hasSize(2);
        assertThat(allProfiles).extracting(Profile::getName)
                .containsExactlyInAnyOrder("First User", "Second User");
    }

    @Test
    @Disabled
    void shouldValidateProfileConstraints() {
        CreateProfileCommand invalidCommand = new CreateProfileCommand(
                "J", // Invalid: name too short (min length is 2)
                "Bio",
                "Location",
                "Role",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                Set.of(Skill.skillWithId(new Skill.SkillId(javaSkill.getSkillId()), "Java", "Programming", 8))
        );

        assertThatThrownBy(() -> createProfileUseCase.create(invalidCommand))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("name: size must be between 2 and 100");
    }

    @Test
    @Disabled
    void shouldValidateSkillConstraints() {
        // First create a valid profile
        CreateProfileCommand validCommand = new CreateProfileCommand(
                "Valid Name",
                "Bio",
                "Location",
                "Role",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                Set.of(Skill.skillWithId(new Skill.SkillId(javaSkill.getSkillId()), "Java", "Programming", 8))
        );
        Profile validProfile = createProfileUseCase.create(validCommand);

        // Then try to update it with invalid skill data
        UpdateProfileCommand invalidCommand = new UpdateProfileCommand(
                validProfile.getProfileId().get().profileId(),
                "Valid Name",
                "Bio",
                "Location",
                "Role",
                ProfileStatus.ACTIVE,
                "http://example.com/image.jpg",
                Set.of(Skill.skillWithId(new Skill.SkillId(javaSkill.getSkillId()), "Java", "Programming", 11))
        );

        assertThatThrownBy(() -> updateProfileUseCase.update(invalidCommand))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("proficiencyLevel: must be less than or equal to 10");
    }
} 