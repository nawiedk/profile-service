package com.nawiedk.profile.application.service;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.out.persistence.dao.ProfileEntity;
import com.nawiedk.profile.out.persistence.dao.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProfileServiceTest {

    private ProfileRepository repository;
    private UpdateProfileService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProfileRepository.class);
        service = new UpdateProfileService(repository);
    }

    @Test
    void updatesExistingProfile() {
        Long id = 1L;

        ProfileEntity existing = new ProfileEntity(
                id, "Old Name", "Old Bio", "Old City", "Old Role",
                List.of("OldSkill"), "old.jpg", false
        );

        Profile update = new Profile(
                id, "New Name", "New Bio", "New City", "New Role",
                List.of("NewSkill1", "NewSkill2"), "new.jpg", true
        );

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Profile result = service.update(id, update);

        assertEquals("New Name", result.getName());
        assertEquals("New Role", result.getRole());
        assertTrue(result.getProfileStatus());
    }
}
