package org.example.dimollbackend.auth.service.impl;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.auth.service.AuthService;
import org.example.dimollbackend.auth.service.jwt.JwtService;
import org.example.dimollbackend.config.properties.JwtProperties;
import org.example.dimollbackend.dto.request.RegisterRequestDto;
import org.example.dimollbackend.dto.request.SignInRequestDto;
import org.example.dimollbackend.dto.response.AuthResponseDto;
import org.example.dimollbackend.enums.TokenType;
import org.example.dimollbackend.exeption.ExistException;
import org.example.dimollbackend.exeption.UnauthorizedException;
import org.example.dimollbackend.user.entity.User;
import org.example.dimollbackend.user.repository.UserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository usersRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    @Override
    public AuthResponseDto register(RegisterRequestDto registerRequestDto, HttpServletResponse response) {
        usersRepository.findAll().stream()
                .map(User::getUsername)
                .forEach(email -> isCorrectEmail(registerRequestDto, email));

        User user = userBuilder(registerRequestDto);
        usersRepository.save(user);
        String accessToken = jwtService.generateToken(user.getUsername(), TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user.getUsername(), TokenType.REFRESH_TOKEN);

        Cookie refreshTokenCookie = buildRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);

        return buildAuthResponseDto(accessToken);
    }

    private void isCorrectEmail(RegisterRequestDto dto, String email) {
        if (dto.getEmail().equals(email)) {
            throw new ExistException("user is alredy excist");
        }
    }


    public AuthResponseDto signIn(SignInRequestDto signInRequestDto, HttpServletResponse response) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), signInRequestDto.getPassword())
            );
            User user = (User) auth.getPrincipal();

            String accessToken = jwtService.generateToken(user.getUsername(), TokenType.ACCESS_TOKEN);
            String refreshToken = jwtService.generateToken(user.getUsername(), TokenType.REFRESH_TOKEN);

            Cookie refreshTokenCookie = buildRefreshTokenCookie(refreshToken);
            response.addCookie(refreshTokenCookie);

            return buildAuthResponseDto(accessToken);
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Invalid email or password");
        } catch (Exception e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }

    }
    private Cookie buildRefreshTokenCookie(String refreshToken) {
        Cookie refreshTokenCookie = new Cookie(TokenType.REFRESH_TOKEN.name(), refreshToken);
        refreshTokenCookie.setMaxAge(jwtProperties.getRefreshCookieTtl());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        return refreshTokenCookie;
    }

    private AuthResponseDto buildAuthResponseDto(String accessToken) {
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .tokenType(TokenType.ACCESS_TOKEN.name())
                .expiresIn(jwtProperties.getAccessTokenTtl())
                .build();
    }


    private User userBuilder(RegisterRequestDto registerRequestDto){
        return User.builder()
                .username(registerRequestDto.getUsername())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .userRole(registerRequestDto.getUserRole())
                .build();


    }
}
