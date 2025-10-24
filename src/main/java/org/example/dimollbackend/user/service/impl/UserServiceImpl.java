package org.example.dimollbackend.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.comment.model.Comment;
import org.example.dimollbackend.audio.comment.repository.CommentRepository;
import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.audio.cover.repository.CoverRepository;
import org.example.dimollbackend.audio.cover.service.CoverService;
import org.example.dimollbackend.dto.request.RegisterRequestDto;
import org.example.dimollbackend.dto.response.CommentResponseDto;
import org.example.dimollbackend.dto.response.CoverResponseDto;
import org.example.dimollbackend.dto.response.UserResponseDto;
import org.example.dimollbackend.user.entity.User;
import org.example.dimollbackend.user.repository.UserRepository;
import org.example.dimollbackend.user.service.UserService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User createUser(RegisterRequestDto registerRequestDto) {
        return User.builder()
                .username(registerRequestDto.getUsername())
                .password(registerRequestDto.getPassword())
                .build();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(null);
    }



    @Override
    public String subscribeToUser(Long userId, String subscriberName) {
        User subscriber = userRepository.findByUsername(subscriberName).orElseThrow();
        User subscribePerson =userRepository.findById(userId).orElseThrow();
        subscriber.getFollowing().add(subscribePerson);
        userRepository.save(subscriber);

        return "success subscribing";
    }

    @Override
    public List<UserResponseDto> getUsersName() {
        return userRepository.findAll().stream()
                .map(mapToResponseDto()).toList();
    }

    @Override
    public List<UserResponseDto> getSubscribers(Long userId) {
        return userRepository.findById(userId).orElseThrow(null).getFollowers().stream()
                .map(mapToResponseDto()).toList();
    }

    @Override
    public List<UserResponseDto> getSubscribes(Long userId) {
        return userRepository.findById(userId).orElseThrow(null).getFollowing().stream()
                .map(mapToResponseDto()).toList();
    }

    private static Function<User, UserResponseDto> mapToResponseDto() {
        return user -> UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
