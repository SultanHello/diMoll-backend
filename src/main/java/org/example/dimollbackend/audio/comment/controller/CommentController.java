package org.example.dimollbackend.audio.comment.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.comment.model.Comment;
import org.example.dimollbackend.audio.comment.repository.CommentRepository;
import org.example.dimollbackend.audio.comment.service.CommentService;
import org.example.dimollbackend.audio.comment.service.impl.CommentLikeService;
import org.example.dimollbackend.audio.cover.service.CoverService;
import org.example.dimollbackend.dto.request.CommentRequestDto;
import org.example.dimollbackend.dto.response.CommentResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/audio/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;



    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> comments(){
        return ResponseEntity.ok(commentService.getAllComments());
    }
    @PostMapping("/{id}/like")
    public String toggleLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        boolean liked = commentLikeService.toggleLike(id, userDetails);
        long total = commentLikeService.getLikesCount(id);
        commentService.putLike(id);
        return liked ? "👍 Лайк добавлен (" + total + ")" : "👎👎 Лайк снят (" + total + ")";
    }

    @GetMapping("/{id}/likes")
    public long getLikesCount(@PathVariable Long id) {
        return commentLikeService.getLikesCount(id);
    }
    @PostMapping("/create")
    public ResponseEntity<CommentResponseDto> createComment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentRequestDto commentRequestDto
    ){
        return ResponseEntity.ok(commentService.createComment(userDetails.getUsername(),commentRequestDto));


    }


}
