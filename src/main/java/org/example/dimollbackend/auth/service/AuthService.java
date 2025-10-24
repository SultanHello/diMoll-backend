package org.example.dimollbackend.auth.service;


import jakarta.servlet.http.HttpServletResponse;
import org.example.dimollbackend.dto.request.RegisterRequestDto;
import org.example.dimollbackend.dto.request.SignInRequestDto;
import org.example.dimollbackend.dto.response.AuthResponseDto;

import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponseDto register(RegisterRequestDto registerRequestDto, HttpServletResponse response);

    AuthResponseDto signIn(SignInRequestDto signInRequestDto, HttpServletResponse response);
}
