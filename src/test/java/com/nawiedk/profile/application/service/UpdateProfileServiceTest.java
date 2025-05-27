package com.nawiedk.profile.application.service;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.adapter.out.persistence.jpaentity.ProfileEntity;
import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
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
                id, "Nawiedk", "software Engineer", "düsseldorf", "software Engineer",
                List.of("java", "spring", "docker"), "Nawiedk.jpg", false
        );

        Profile update = new Profile(
                id, "nawiedk", "Software Engineer", "Düsseldorf", "Software Engineer",
                List.of("Java", "Spring", "Docker"), "nawiedk.jpg", true
        );

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Profile result = service.update(id, update);

        assertEquals("nawiedk", result.getName());
        assertEquals("Software Engineer", result.getRole());
        assertTrue(result.getProfileStatus());
    }
}
