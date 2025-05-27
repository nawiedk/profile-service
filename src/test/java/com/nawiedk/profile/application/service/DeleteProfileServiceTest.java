package com.nawiedk.profile.application.service;

import com.nawiedk.profile.adapter.out.persistence.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteProfileServiceTest {

    private ProfileRepository repository;
    private DeleteProfileService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProfileRepository.class);
        service = new DeleteProfileService(repository);
    }

    @Test
    void deletesExistingProfile() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        service.deleteById(id);

        verify(repository).deleteById(id);
    }

    @Test
    void throwsWhenProfileNotFound() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.deleteById(999L));
    }
}
