package com.nawiedk.profile.application.port.in.usecase.profile;

import com.nawiedk.profile.application.port.in.command.profile.CreateProfileCommand;
import com.nawiedk.profile.domain.model.Profile;

public interface CreateProfileUseCase {
    Profile create(CreateProfileCommand command);
}