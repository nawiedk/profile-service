package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteProfileServiceTest {

    @Autowired
    private ProfileRepositoryPort repositoryPort;

    @Autowired
    private DeleteProfileService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ProfileRepositoryPort profileRepositoryPort() {
            return mock(ProfileRepositoryPort.class);
        }
    }

    @Test
    void deleteByIdSuccessfullyDeletesProfileWhenExists() {
        Long profileId = 1L;
        when(repositoryPort.existsById(profileId)).thenReturn(true);
        doNothing().when(repositoryPort).deleteById(profileId);

        service.deleteById(new DeleteProfileCommand(profileId));

        verify(repositoryPort, times(1)).existsById(profileId);
        verify(repositoryPort, times(1)).deleteById(profileId);
    }

    @Test
    void deleteByIdThrowsExceptionWhenProfileNotFound() {
        Long profileId = 99L;
        when(repositoryPort.existsById(profileId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.deleteById(new DeleteProfileCommand(profileId)));
        assertEquals("Profile not found", exception.getMessage());

        verify(repositoryPort, times(1)).existsById(profileId);
        verify(repositoryPort, never()).deleteById(profileId);
    }
}