package org.example.dimollbackend;
import static org.junit.jupiter.api.Assertions.assertEquals;

import jakarta.servlet.http.HttpServletResponse;
import org.example.dimollbackend.auth.service.AuthService;
import org.example.dimollbackend.auth.service.impl.AuthServiceImpl;
import org.example.dimollbackend.auth.service.jwt.JwtService;
import org.example.dimollbackend.config.properties.JwtProperties;
import org.example.dimollbackend.dto.request.RegisterRequestDto;
import org.example.dimollbackend.exeption.ExistException;
import org.example.dimollbackend.user.entity.User;
import org.example.dimollbackend.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository usersRepository;

    @Mock
    private  JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private PasswordEncoder passwordEncoder;



    @InjectMocks
    private AuthServiceImpl authService;
    @Test
    void register_ifNotUnique() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("newUser");
        dto.setPassword("1234");
        dto.setEmail("new@example.com"); // обязательно email

        List<User> existingUsers = List.of(
                User.builder().username("user1").email("user1@example.com").build(),
                User.builder().username("user2").email("user2@example.com").build()
        );

        Mockito.when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        Mockito.when(usersRepository.findAll()).thenReturn(existingUsers);
        User savedUser = User.builder().username("newUser").email("new@example.com").build();
        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenReturn(savedUser);
        Mockito.when(jwtService.generateToken(anyString(), Mockito.any())).thenReturn("token");
        Mockito.when(jwtProperties.getRefreshCookieTtl()).thenReturn(3600);
        Mockito.when(jwtProperties.getAccessTokenTtl()).thenReturn(1500L);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        assertEquals("token", authService.register(dto, response).getAccessToken());
        Mockito.verify(response).addCookie(Mockito.any());
    }


    @Test
    void register_ifUnique() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("user1");
        dto.setPassword("1234");
        dto.setEmail("user1@example.com"); // Добавляем email

        List<User> existingUsers = List.of(
                User.builder().username("user1").email("user1@example.com").build(),
                User.builder().username("user2").email("user2@example.com").build()
        );

        Mockito.when(usersRepository.findAll()).thenReturn(existingUsers);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        assertThrows(ExistException.class, () -> authService.register(dto, response));
    }


}
