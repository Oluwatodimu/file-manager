package com.todimu.backend.dropboxclone.controller;

import com.todimu.backend.dropboxclone.data.dto.request.CreateUserRequest;
import com.todimu.backend.dropboxclone.data.dto.request.UserLoginRequest;
import com.todimu.backend.dropboxclone.data.dto.response.BaseResponse;
import com.todimu.backend.dropboxclone.data.entity.User;
import com.todimu.backend.dropboxclone.security.jwt.Token;
import com.todimu.backend.dropboxclone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth", produces = {MediaType.APPLICATION_JSON_VALUE})
public class AuthController {

    public static final String SUCCESS_MESSAGE = "successful";

    private final UserService userService;

    @PostMapping(path = "/register")
    public ResponseEntity<BaseResponse> registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        log.info("registering new user with email: {}", createUserRequest.getEmail());
        User newUser = userService.createUser(createUserRequest);
        return new ResponseEntity<>(new BaseResponse(newUser, SUCCESS_MESSAGE, false), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<BaseResponse> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        log.info("login attempt by user with email: {}", loginRequest.getEmail());
        Token authToken = userService.authenticateUser(loginRequest);
        return new ResponseEntity<>(new BaseResponse(authToken, SUCCESS_MESSAGE, false), HttpStatus.OK);
    }
}
