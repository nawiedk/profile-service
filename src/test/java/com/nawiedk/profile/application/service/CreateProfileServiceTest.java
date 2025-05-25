package com.nawiedk.profile.application.service;

import com.nawiedk.profile.domain.Profile;
import com.nawiedk.profile.out.persistence.dao.ProfileEntity;
import com.nawiedk.profile.out.persistence.dao.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateProfileServiceTest {

    private ProfileRepository repository;
    private CreateProfileService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProfileRepository.class);
        service = new CreateProfileService(repository);
    }

    @Test
    void createsProfileSuccessfully() {
        Profile profile = new Profile(
                2L,
                "nawiedk",
                "Software Engineer",
                "Düsseldorf",
                "Software Engineer",
                List.of("Java", "Spring", "Docker"),
                "nawiedk.jpg",
                true
        );

        ProfileEntity entity = new ProfileEntity(
                2L,
                "nawiedk",
                "Software Engineer",
                "Düsseldorf",
                "Software Engineer",
                List.of("Java", "Spring", "Docker"),
                "nawiedk.jpg",
                true
        );

        when(repository.save(any())).thenReturn(entity);

        Profile saved = service.create(profile);

        assertEquals("nawiedk", saved.getName());
        assertTrue(saved.getProfileStatus());
    }
}