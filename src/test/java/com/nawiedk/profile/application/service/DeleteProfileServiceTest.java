package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private DeleteProfileService deleteProfileService;

    private final Long existingProfileId = 1L;

    @Test
    void deleteById_WhenProfileExists_ShouldDeleteProfile() {
        when(profileRepositoryPort.existsById(existingProfileId)).thenReturn(true);

        deleteProfileService.deleteById(new DeleteProfileCommand(existingProfileId));

        verify(profileRepositoryPort).existsById(existingProfileId);
        verify(profileRepositoryPort).deleteById(existingProfileId);
    }

    @Test
    void deleteById_WhenProfileDoesNotExist_ShouldThrowException() {
        when(profileRepositoryPort.existsById(existingProfileId)).thenReturn(false);

        assertThatThrownBy(() -> deleteProfileService.deleteById(new DeleteProfileCommand(existingProfileId)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Profile not found");

        verify(profileRepositoryPort).existsById(existingProfileId);
        verify(profileRepositoryPort, never()).deleteById(any());
    }
}
