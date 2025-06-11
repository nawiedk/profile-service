package com.nawiedk.profile.application.port.in.usecase.profile;

import com.nawiedk.profile.application.port.in.command.profile.DeleteProfileCommand;

public interface DeleteProfileUseCase {
    void deleteByProfileId(DeleteProfileCommand command);
}
