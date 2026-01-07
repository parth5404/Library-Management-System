package com.tcs.Library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tcs.Library.repository.UserRepo;

@Service

public class UniqueCheckServiceImpl implements UniqueCheckService {
    @Autowired
    private UserRepo userDS;
    public boolean isEmailRegistered(String email){
        if(userDS.existsByEmail(email)) return true;
        return false;
    }
    public boolean isMobileRegistered(String countryCode, String mobileNumber){
        if(userDS.existsByCountryCodeAndMobileNumber(countryCode, mobileNumber)) return true;
        return false;
    }

}
