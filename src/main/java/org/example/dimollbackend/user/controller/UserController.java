package org.example.dimollbackend.user.controller;


import lombok.RequiredArgsConstructor;

import org.example.dimollbackend.dto.response.CommentResponseDto;
import org.example.dimollbackend.dto.response.CoverResponseDto;
import org.example.dimollbackend.dto.response.UserResponseDto;
import org.example.dimollbackend.user.entity.User;
import org.example.dimollbackend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping()
    public List<User> users(){
        return userService.getUsers();
    }

    @PostMapping("/{userId}/subscribe")
    public ResponseEntity<String> subscribe(@PathVariable Long userId,@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(userService.subscribeToUser(userId,userDetails.getUsername()));
    }

    @GetMapping("/{userId}/subscribers")
    public ResponseEntity<List<UserResponseDto>> subscribers(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getSubscribers(userId));
    }
    @GetMapping("/{userId}/subscribes")
    public ResponseEntity<List<UserResponseDto>> subscribes(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getSubscribes(userId));
    }

    @GetMapping("/name")
    public ResponseEntity<List<UserResponseDto>> usersName(){
        return ResponseEntity.ok(userService.getUsersName());
    }



}
