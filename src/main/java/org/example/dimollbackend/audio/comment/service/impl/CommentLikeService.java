package org.example.dimollbackend.audio.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.common.service.RedisService;
import org.example.dimollbackend.user.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    
    private static final String KEY = "comment:%d:likes";

    private final UserService userService;
    private final RedisService redisService;

    public boolean toggleLike(Long commentId, UserDetails userDetails) {
        String userId = this.getUserIdByUsername(userDetails.getUsername()).toString();
        Boolean isMember = redisService.isMember(KEY.formatted(commentId),userId);
        return this.processLike(isMember, KEY, userId);
    }

    private boolean processLike(Boolean isMember, String key, String userId) {
        if (Boolean.TRUE.equals(isMember)) {
            redisService.remove(key,userId);
            return false;
        }
        redisService.add(key,userId);
        return true;
    }

    private Long getUserIdByUsername(String username) {
        return userService.findByUsername(username).getId();
    }


    public Long getLikesCount(Long commentId) {
        String key = KEY.formatted(commentId);
        Long count = redisService.size(key);
        return count != null ? count : 0;
    }

    public boolean hasUserLiked(Long commentId, Long userId) {
        String key = KEY.formatted(commentId);
        return Boolean.TRUE.equals(redisService.isMember(key, userId.toString()));
    }
}