package com.todimu.backend.dropboxclone.service;

import com.todimu.backend.dropboxclone.data.dto.request.CreateUserRequest;
import com.todimu.backend.dropboxclone.data.dto.request.UserLoginRequest;
import com.todimu.backend.dropboxclone.data.entity.User;
import com.todimu.backend.dropboxclone.exception.EmailAlreadyExistsException;
import com.todimu.backend.dropboxclone.repository.UserRepository;
import com.todimu.backend.dropboxclone.security.jwt.Token;
import com.todimu.backend.dropboxclone.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public User createUser(CreateUserRequest createUserRequest) {

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new EmailAlreadyExistsException("email already in use");
        }

        User newUser = User.builder()
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();

        return userRepository.save(newUser);
    }

    public Token authenticateUser(UserLoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        return tokenProvider.createToken(authentication);
    }
}
