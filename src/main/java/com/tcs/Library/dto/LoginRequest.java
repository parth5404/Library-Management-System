package com.tcs.Library.dto;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
