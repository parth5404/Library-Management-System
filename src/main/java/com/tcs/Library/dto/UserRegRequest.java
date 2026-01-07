package com.tcs.Library.dto;
import java.time.LocalDate;
import lombok.*;

@Data
public class UserRegRequest {
    private String id;
    private String customerName;
    private String email;
    private String countryCode;
    private String mobileNumber;
    private String address;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pinCode;
    private LocalDate dateOfBirth;
    private String password;
    private String confirmPassword;
    private String secretQuestion;
    private String secretAnswer;
}

