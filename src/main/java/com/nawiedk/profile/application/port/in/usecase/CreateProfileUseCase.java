package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.domain.model.Profile;

public interface CreateProfileUseCase {
    Profile create(CreateProfileCommand command);
}