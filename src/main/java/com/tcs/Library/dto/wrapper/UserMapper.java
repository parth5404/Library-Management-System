
package com.tcs.Library.dto.wrapper;

import com.tcs.Library.dto.UserRegRequest;
import com.tcs.Library.entity.*;

public class UserMapper {
    private UserMapper() {
    }

    public static User toEntity(UserRegRequest dto) {
        if (dto == null)
            return null;

        User user = new User();

        user.setCustomerName(dto.getCustomerName());
        user.setEmail(dto.getEmail());
        user.setCountryCode(dto.getCountryCode());
        user.setMobileNumber(dto.getMobileNumber());
        user.setAddress(dto.getAddress());
        user.setDateOfBirth(dto.getDateOfBirth());
        // user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setSecretQuestion(dto.getSecretQuestion());
        user.setSecretAnswer(dto.getSecretAnswer());

        return user;
    }
}
