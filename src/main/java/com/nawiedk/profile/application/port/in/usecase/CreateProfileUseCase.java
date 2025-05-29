package com.nawiedk.profile.application.port.in.usecase;

import com.nawiedk.profile.application.port.in.command.CreateProfileCommand;
import com.nawiedk.profile.domain.Profile;
import org.springframework.stereotype.Component;

public interface CreateProfileUseCase {
    Profile create(CreateProfileCommand command);
}