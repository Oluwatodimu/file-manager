package com.todimu.backend.dropboxclone.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginRequest {

    @Email
    private String email;

    @NotEmpty(message = "password cannot be empty")
    private String password;
}
