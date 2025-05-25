package com.nawiedk.profile.application.service;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.out.persistence.dao.ProfileEntity;
import com.nawiedk.profile.out.persistence.dao.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetProfileServiceTest {

    private ProfileRepository repository;
    private GetProfileService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProfileRepository.class);
        service = new GetProfileService(repository);
    }

    @Test
    void returnsProfileWhenIdExists() {
        Long id = 1L;
        ProfileEntity entity = new ProfileEntity(
                id,
                "Nawied Khaleqi",
                "My Bio",
                "Düsseldorf, Germany",
                "Software Engineer",
                List.of("Java", "Spring", "Docker"),
                "image.jpg",
                true
        );

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        Profile result = service.getProfileById(id);

        assertEquals("Nawied Khaleqi", result.getName());
        assertEquals("Software Engineer", result.getRole());
        assertTrue(result.getProfileStatus());
    }

    @Test
    void throwsExceptionWhenProfileNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getProfileById(999L));
    }
}