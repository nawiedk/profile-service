package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.domain.Profile;
import org.springframework.stereotype.Component;

public interface UpdateProfileUseCase {
    Profile update(UpdateProfileCommand command);
}
