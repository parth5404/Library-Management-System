
package com.tcs.Library.repository;

import com.tcs.Library.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByCountryCodeAndMobileNumber(String countryCode, String mobileNumber);

    Optional<User> findByEmail(String email);
    User getByEmail(String email);

}


