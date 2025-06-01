package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.application.port.in.command.UpdateProfileCommand;
import com.nawiedk.profile.domain.model.Profile;

public interface UpdateProfileUseCase {
    Profile update(UpdateProfileCommand command);
}
