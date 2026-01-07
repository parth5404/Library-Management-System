package com.tcs.Library.service;
import org.springframework.stereotype.Service;


public interface UniqueCheckService {
    boolean isEmailRegistered(String email);
    boolean isMobileRegistered(String countryCode, String mobileNumber);
}
