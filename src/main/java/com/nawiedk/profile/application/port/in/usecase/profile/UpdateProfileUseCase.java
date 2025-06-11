package com.nawiedk.profile.application.port.in.usecase.profile;

import com.nawiedk.profile.application.port.in.command.profile.UpdateProfileCommand;
import com.nawiedk.profile.domain.model.Profile;

public interface UpdateProfileUseCase {
    Profile update(UpdateProfileCommand command);
}
