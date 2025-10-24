package org.example.dimollbackend.user.service;

import org.example.dimollbackend.dto.request.RegisterRequestDto;
import org.example.dimollbackend.dto.response.CommentResponseDto;
import org.example.dimollbackend.dto.response.CoverResponseDto;
import org.example.dimollbackend.dto.response.UserResponseDto;
import org.example.dimollbackend.user.entity.User;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface UserService {
    public User createUser(RegisterRequestDto registerRequestDto);
    public List<User> getUsers();
    public User findByUsername(String username);


    String subscribeToUser(Long userId,String subscriberName);

    List<UserResponseDto> getUsersName();

    List<UserResponseDto> getSubscribers(Long userId);

    List<UserResponseDto> getSubscribes(Long userId);
}
