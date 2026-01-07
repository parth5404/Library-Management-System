package com.tcs.Library.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class SignUpResponse {
    private UUID client_id;
}
