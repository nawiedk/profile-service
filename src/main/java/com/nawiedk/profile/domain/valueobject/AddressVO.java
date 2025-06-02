package com.nawiedk.profile.domain.valueobject;

import static com.nawiedk.profile.domain.validation.Validation.validate;

public class AddressVO {


    public record AddressId(Long id) {
        public AddressId(Long id) {
            this.id = id;
            validate(this);
        }
    }
}
