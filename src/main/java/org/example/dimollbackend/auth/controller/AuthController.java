package org.example.dimollbackend.auth.controller;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.auth.service.AuthService;
import org.example.dimollbackend.dto.request.RegisterRequestDto;
import org.example.dimollbackend.dto.request.SignInRequestDto;
import org.example.dimollbackend.dto.response.AuthResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto, HttpServletResponse response){
        return ResponseEntity.ok(authService.register(registerRequestDto,response));

    }

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponseDto> signIn(@RequestBody SignInRequestDto signInRequestDto, HttpServletResponse response){
        System.out.println("hello");
        return ResponseEntity.ok(authService.signIn(signInRequestDto,response));
    }


}
