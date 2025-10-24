package org.example.dimollbackend.audio.comment.service;

import org.example.dimollbackend.audio.comment.model.Comment;
import org.example.dimollbackend.dto.request.CommentRequestDto;
import org.example.dimollbackend.dto.response.CommentResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CommentService {
    CommentResponseDto createComment(String username, CommentRequestDto commentRequestDto);

    CommentResponseDto putLike(Long commentId);

    List<CommentResponseDto> getAllComments();
}
