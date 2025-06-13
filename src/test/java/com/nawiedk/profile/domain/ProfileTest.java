package com.nawiedk.profile.domain;

import com.nawiedk.profile.domain.model.Profile;
import com.nawiedk.profile.domain.model.Skill;
import com.nawiedk.profile.domain.util.ProfileStatus;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProfileTest {

    private static final String VALID_NAME = "John Doe";
    private static final String VALID_BIO = "Software Engineer with 5 years of experience";
    private static final String VALID_LOCATION = "New York";
    private static final String VALID_ROLE = "Senior Developer";
    private static final String VALID_IMAGE_URL = "https://example.com/image.jpg";
    private static final ProfileStatus VALID_STATUS = ProfileStatus.ACTIVE;
    private static final Profile.ProfileId VALID_PROFILE_ID = new Profile.ProfileId(1L);

    private Set<Skill> createValidSkills() {
        Set<Skill> skills = new HashSet<>();
        skills.add(Skill.skillWithoutId("Java", "Programming", 8));
        skills.add(Skill.skillWithoutId("Spring", "Framework", 7));
        return skills;
    }

    @Nested
    @DisplayName("Profile Creation Tests")
    class ProfileCreationTests {

        @Test
        @DisplayName("Should create valid profile with ID")
        void shouldCreateValidProfileWithId() {
            Set<Skill> skills = createValidSkills();

            Profile profile = Profile.profileWithId(
                    VALID_PROFILE_ID,
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            assertThat(profile).isNotNull();
            assertThat(profile.getProfileId()).isPresent();
            assertThat(profile.getProfileId().get()).isEqualTo(VALID_PROFILE_ID);
            assertThat(profile.getName()).isEqualTo(VALID_NAME);
            assertThat(profile.getBio()).isEqualTo(VALID_BIO);
            assertThat(profile.getLocation()).isEqualTo(VALID_LOCATION);
            assertThat(profile.getRole()).isEqualTo(VALID_ROLE);
            assertThat(profile.getImageUrl()).isEqualTo(VALID_IMAGE_URL);
            assertThat(profile.getProfileStatus()).isEqualTo(VALID_STATUS);
            assertThat(profile.getSkills()).isEqualTo(skills);
        }

        @Test
        @DisplayName("Should create valid profile without ID")
        void shouldCreateValidProfileWithoutId() {
            Set<Skill> skills = createValidSkills();

            Profile profile = Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            assertThat(profile).isNotNull();
            assertThat(profile.getProfileId()).isEmpty();
            assertThat(profile.getName()).isEqualTo(VALID_NAME);
            assertThat(profile.getBio()).isEqualTo(VALID_BIO);
            assertThat(profile.getLocation()).isEqualTo(VALID_LOCATION);
            assertThat(profile.getRole()).isEqualTo(VALID_ROLE);
            assertThat(profile.getImageUrl()).isEqualTo(VALID_IMAGE_URL);
            assertThat(profile.getProfileStatus()).isEqualTo(VALID_STATUS);
            assertThat(profile.getSkills()).isEqualTo(skills);
        }

        @Test
        @DisplayName("Should create profile with empty skills set")
        void shouldCreateProfileWithEmptySkills() {
            Set<Skill> emptySkills = new HashSet<>();

            Profile profile = Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    emptySkills
            );

            assertThat(profile).isNotNull();
            assertThat(profile.getSkills()).isEmpty();
        }

        @Test
        @DisplayName("Should create profile with null bio")
        void shouldCreateProfileWithNullBio() {
            Set<Skill> skills = createValidSkills();

            Profile profile = Profile.profileWithoutId(
                    VALID_NAME,
                    null,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            assertThat(profile).isNotNull();
            assertThat(profile.getBio()).isNull();
        }
    }

    @Nested
    @DisplayName("Profile Validation Tests")
    class ProfileValidationTests {

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowExceptionWhenNameIsNull() {
            Set<Skill> skills = createValidSkills();

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    null,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            Set<Skill> skills = createValidSkills();

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    "   ",
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when name is too short")
        void shouldThrowExceptionWhenNameIsTooShort() {
            Set<Skill> skills = createValidSkills();

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    "A",
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when name is too long")
        void shouldThrowExceptionWhenNameIsTooLong() {
            Set<Skill> skills = createValidSkills();
            String longName = "a".repeat(101);

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    longName,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when bio is too long")
        void shouldThrowExceptionWhenBioIsTooLong() {
            Set<Skill> skills = createValidSkills();
            String longBio = "a".repeat(1001);

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    VALID_NAME,
                    longBio,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when location is null")
        void shouldThrowExceptionWhenLocationIsNull() {
            Set<Skill> skills = createValidSkills();

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    null,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when role is null")
        void shouldThrowExceptionWhenRoleIsNull() {
            Set<Skill> skills = createValidSkills();

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    null,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when imageUrl is null")
        void shouldThrowExceptionWhenImageUrlIsNull() {
            Set<Skill> skills = createValidSkills();

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    null,
                    VALID_STATUS,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }

        @Test
        @DisplayName("Should throw exception when profileStatus is null")
        void shouldThrowExceptionWhenProfileStatusIsNull() {
            Set<Skill> skills = createValidSkills();

            assertThatThrownBy(() -> Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    null,
                    skills
            )).isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("ProfileId Tests")
    class ProfileIdTests {

        @Test
        @DisplayName("Should create valid ProfileId")
        void shouldCreateValidProfileId() {
            Long id = 1L;

            Profile.ProfileId profileId = new Profile.ProfileId(id);

            assertThat(profileId).isNotNull();
            assertThat(profileId.profileId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should throw exception when ProfileId is null")
        void shouldThrowExceptionWhenProfileIdIsNull() {
            assertThatThrownBy(() -> new Profile.ProfileId(null))
                    .isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("Profile Method Tests")
    class ProfileMethodTests {

        @Test
        @DisplayName("Should return profile status as string")
        void shouldReturnProfileStatusAsString() {
            Set<Skill> skills = createValidSkills();
            Profile profile = Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    ProfileStatus.ACTIVE,
                    skills
            );

            String statusString = profile.getProfileStatusAsString();

            assertThat(statusString).isEqualTo("ACTIVE");
        }

        @Test
        @DisplayName("Should return empty optional when profile has no ID")
        void shouldReturnEmptyOptionalWhenProfileHasNoId() {
            Set<Skill> skills = createValidSkills();
            Profile profile = Profile.profileWithoutId(
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            Optional<Profile.ProfileId> profileId = profile.getProfileId();

            assertThat(profileId).isEmpty();
        }

        @Test
        @DisplayName("Should return present optional when profile has ID")
        void shouldReturnPresentOptionalWhenProfileHasId() {
            Set<Skill> skills = createValidSkills();
            Profile profile = Profile.profileWithId(
                    VALID_PROFILE_ID,
                    VALID_NAME,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            Optional<Profile.ProfileId> profileId = profile.getProfileId();

            assertThat(profileId).isPresent();
            assertThat(profileId.get()).isEqualTo(VALID_PROFILE_ID);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should accept minimum valid name length")
        void shouldAcceptMinimumValidNameLength() {
            Set<Skill> skills = createValidSkills();
            String minName = "AB";

            Profile profile = Profile.profileWithoutId(
                    minName,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            assertThat(profile).isNotNull();
            assertThat(profile.getName()).isEqualTo(minName);
        }

        @Test
        @DisplayName("Should accept maximum valid name length")
        void shouldAcceptMaximumValidNameLength() {
            Set<Skill> skills = createValidSkills();
            String maxName = "a".repeat(100);

            Profile profile = Profile.profileWithoutId(
                    maxName,
                    VALID_BIO,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            assertThat(profile).isNotNull();
            assertThat(profile.getName()).isEqualTo(maxName);
        }

        @Test
        @DisplayName("Should accept maximum valid bio length")
        void shouldAcceptMaximumValidBioLength() {
            Set<Skill> skills = createValidSkills();
            String maxBio = "a".repeat(1000);

            Profile profile = Profile.profileWithoutId(
                    VALID_NAME,
                    maxBio,
                    VALID_LOCATION,
                    VALID_ROLE,
                    VALID_IMAGE_URL,
                    VALID_STATUS,
                    skills
            );

            assertThat(profile).isNotNull();
            assertThat(profile.getBio()).isEqualTo(maxBio);
        }

        @Test
        @DisplayName("Should handle all profile status values")
        void shouldHandleAllProfileStatusValues() {
            Set<Skill> skills = createValidSkills();

            for (ProfileStatus status : ProfileStatus.values()) {
                Profile profile = Profile.profileWithoutId(
                        VALID_NAME,
                        VALID_BIO,
                        VALID_LOCATION,
                        VALID_ROLE,
                        VALID_IMAGE_URL,
                        status,
                        skills
                );

                assertThat(profile).isNotNull();
                assertThat(profile.getProfileStatus()).isEqualTo(status);
                assertThat(profile.getProfileStatusAsString()).isEqualTo(status.toString());
            }
        }
    }
}