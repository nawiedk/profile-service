package com.nawiedk.profile.application.service;

import com.nawiedk.profile.application.port.in.command.profile.DeleteProfileCommand;
import com.nawiedk.profile.application.port.out.ProfileRepositoryPort;
import com.nawiedk.profile.application.service.profile.DeleteProfileService;
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
    void deleteById_WhenProfileExists_ShouldDeleteProfileProfile() {
        when(profileRepositoryPort.existsByProfileId(existingProfileId)).thenReturn(true);

        deleteProfileService.deleteByProfileId(new DeleteProfileCommand(existingProfileId));

        verify(profileRepositoryPort).existsByProfileId(existingProfileId);
        verify(profileRepositoryPort).deleteByProfileId(existingProfileId);
    }

    @Test
    void deleteByProfileId_WhenProfileDoesNotExist_ShouldThrowException() {
        when(profileRepositoryPort.existsByProfileId(existingProfileId)).thenReturn(false);

        assertThatThrownBy(() -> deleteProfileService.deleteByProfileId(new DeleteProfileCommand(existingProfileId)))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Profile not found");

        verify(profileRepositoryPort).existsByProfileId(existingProfileId);
        verify(profileRepositoryPort, never()).deleteByProfileId(any());
    }
}
