package com.todimu.backend.dropboxclone.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @Email
    private String email;

    @Size(min = 8, message = "password should be at least 8 characters long")
    @NotEmpty(message = "password cannot be empty")
    private String password;
}
