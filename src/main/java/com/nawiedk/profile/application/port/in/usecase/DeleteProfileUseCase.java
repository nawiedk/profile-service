package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.application.port.in.command.DeleteProfileCommand;
import org.springframework.stereotype.Component;

public interface DeleteProfileUseCase {
    void deleteById(DeleteProfileCommand command);
}
