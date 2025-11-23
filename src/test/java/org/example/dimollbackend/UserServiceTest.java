package org.example.dimollbackend;


import org.example.dimollbackend.dto.request.RegisterRequestDto;
import org.example.dimollbackend.dto.response.UserResponseDto;
import org.example.dimollbackend.user.entity.User;
import org.example.dimollbackend.user.repository.UserRepository;
import org.example.dimollbackend.user.service.UserService;
import org.example.dimollbackend.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // ---------------------------------------------------------
    // createUser
    // ---------------------------------------------------------
    @Test
    void createUser_shouldReturnUserWithGivenFields() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setUsername("sultan");
        dto.setPassword("1234");
        dto.setEmail("sultan@example.com");

        User user =userService.createUser(dto);

        assertEquals("sultan", user.getUsername());
        assertEquals("1234", user.getPassword());
    }

    // ---------------------------------------------------------
    // getUsers
    // ---------------------------------------------------------
    @Test
    void getUsers_shouldReturnAllUsers() {
        List<User> mockUsers = List.of(
                User.builder().id(1L).username("a").build(),
                User.builder().id(2L).username("b").build()
        );

        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getUsers();

        assertEquals(2, result.size());
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    // ---------------------------------------------------------
    // findByUsername
    // ---------------------------------------------------------
    @Test
    void findByUsername_shouldReturnUser() {
        User user = User.builder().username("sultan").build();
        Mockito.when(userRepository.findByUsername("sultan"))
                .thenReturn(Optional.of(user));

        User result = userService.findByUsername("sultan");

        assertEquals("sultan", result.getUsername());
    }

    // ---------------------------------------------------------
    // subscribeToUser
    // ---------------------------------------------------------
    @Test
    void subscribeToUser_shouldAddFollowingAndSave() {

        User subscriber = User.builder()
                .username("sub")
                .following(new ArrayList<>())
                .build();

        User target = User.builder()
                .id(10L)
                .build();

        Mockito.when(userRepository.findByUsername("sub")).thenReturn(Optional.of(subscriber));
        Mockito.when(userRepository.findById(10L)).thenReturn(Optional.of(target));

        String result = userService.subscribeToUser(10L, "sub");

        assertEquals("success subscribing", result);
        assertTrue(subscriber.getFollowing().contains(target));

        Mockito.verify(userRepository).save(subscriber);
    }

    // ---------------------------------------------------------
    // getUsersName
    // ---------------------------------------------------------
    @Test
    void getUsersName_shouldMapToDto() {
        List<User> mockUsers = List.of(
                User.builder().id(1L).username("aaa").build(),
                User.builder().id(2L).username("bbb").build()
        );

        Mockito.when(userRepository.findAll()).thenReturn(mockUsers);

        List<UserResponseDto> result = userService.getUsersName();

        assertEquals(2, result.size());
        assertEquals("aaa", result.get(0).getUsername());
        assertEquals("bbb", result.get(1).getUsername());
    }

    // ---------------------------------------------------------
    // getSubscribers
    // ---------------------------------------------------------
    @Test
    void getSubscribers_shouldReturnFollowersAsDto() {
        User follower = User.builder().id(2L).username("bob").build();

        User user = User.builder()
                .id(1L)
                .followers(new ArrayList<>(List.of(follower)))
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<UserResponseDto> result = userService.getSubscribers(1L);

        assertEquals(1, result.size());
        assertEquals("bob", result.get(0).getUsername());
    }

    // ---------------------------------------------------------
    // getSubscribes
    // ---------------------------------------------------------
    @Test
    void getSubscribes_shouldReturnFollowingAsDto() {
        User following = User.builder().id(3L).username("mike").build();

        User user = User.builder()
                .id(1L)
                .following(new ArrayList<>(List.of(following)))
                .build();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<UserResponseDto> result = userService.getSubscribes(1L);

        assertEquals(1, result.size());
        assertEquals("mike", result.get(0).getUsername());
    }
}