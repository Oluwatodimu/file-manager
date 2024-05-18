package com.todimu.backend.dropboxclone.config;

import com.todimu.backend.dropboxclone.security.filter.JwtValidationFilter;
import com.todimu.backend.dropboxclone.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String URL_TRAILER = "/api/v1";

    private final TokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(
                        httpSecuritySessionManagementCustomizer -> httpSecuritySessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtValidationFilter(tokenProvider), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        .requestMatchers( URL_TRAILER + "/auth/**").permitAll()
                        .requestMatchers(URL_TRAILER + "/test/nice").authenticated()
                        .requestMatchers(URL_TRAILER + "/folders/**").authenticated()
                        .requestMatchers(URL_TRAILER + "/files/**").authenticated()
                        .requestMatchers(URL_TRAILER + "/links/create").authenticated()
                        .requestMatchers(URL_TRAILER + "/links/**").permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
