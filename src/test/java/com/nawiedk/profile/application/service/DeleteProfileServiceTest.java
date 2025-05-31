package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProfileServiceTest {

    @Mock
    private ProfileRepositoryPort profileRepositoryPort;

    @InjectMocks
    private DeleteProfileService deleteProfileService;

    @Test
    void deleteById_deletesProfile_whenProfileExists() {
        Long profileId = 1L;
        DeleteProfileCommand command = new DeleteProfileCommand(profileId);
        when(profileRepositoryPort.existsById(profileId)).thenReturn(true);
        doNothing().when(profileRepositoryPort).deleteById(profileId);

        deleteProfileService.deleteById(command);

        verify(profileRepositoryPort, times(1)).existsById(profileId);
        verify(profileRepositoryPort, times(1)).deleteById(profileId);
    }

    @Test
    void deleteById_throwsException_whenProfileDoesNotExist() {
        Long profileId = 1L;
        DeleteProfileCommand command = new DeleteProfileCommand(profileId);
        when(profileRepositoryPort.existsById(profileId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> deleteProfileService.deleteById(command));
        assertEquals("Profile not found", exception.getMessage());
        verify(profileRepositoryPort, times(1)).existsById(profileId);
        verify(profileRepositoryPort, never()).deleteById(profileId);
    }

    @Test
    void deleteById_throwsNullPointerException_whenCommandIsNull() {
        assertThrows(NullPointerException.class, () -> deleteProfileService.deleteById(null));
        verifyNoInteractions(profileRepositoryPort);
    }

    @Test
    void deleteProfileCommand_throwsIllegalArgumentException_whenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new DeleteProfileCommand(null));
        assertTrue(exception.getMessage().toLowerCase().contains("id: must not be null"));
        verifyNoInteractions(profileRepositoryPort);
    }
}
