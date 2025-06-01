package com.nawiedk.profile.application.port.in.command;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetProfileCommandTest {

    @Test
    void shouldCreateValidCommand() {
        GetProfileCommand command = new GetProfileCommand(1L);
        assertThat(command.id()).isEqualTo(1L);
    }

    @Test
    void shouldThrowException_WhenIdIsNull() {
        assertThatThrownBy(() -> new GetProfileCommand(null))
                .isInstanceOf(ConstraintViolationException.class);
    }
} 