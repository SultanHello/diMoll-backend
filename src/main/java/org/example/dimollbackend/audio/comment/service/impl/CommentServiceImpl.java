package org.example.dimollbackend.audio.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.comment.model.Comment;
import org.example.dimollbackend.audio.comment.repository.CommentRepository;
import org.example.dimollbackend.audio.comment.service.CommentService;

import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.audio.cover.service.CoverService;
import org.example.dimollbackend.dto.request.CommentRequestDto;
import org.example.dimollbackend.dto.response.CommentResponseDto;

import org.example.dimollbackend.notification.email.service.EmailNotificationService;
import org.example.dimollbackend.user.entity.User;
import org.example.dimollbackend.user.service.UserService;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CoverService coverService;
    private final UserService userService;
    private final CommentLikeService commentLikeService;
    private final EmailNotificationService emailNotificationService;
    @Override
    public CommentResponseDto createComment(String username, CommentRequestDto commentRequestDto) {
        Cover cover = coverService.findCoverById(commentRequestDto.getCoverId());
        Comment comment = Comment.builder()
                .owner(cover)
                .text(commentRequestDto.getText())
                .writer(userService.findByUsername(username))
                .build();
        emailNotificationService.sendNewLikeEmail(cover.getUser().getEmail(),comment.getWriter().getEmail());
        commentRepository.save(comment);
        return CommentResponseDto.builder()
                .text(comment.getText())
                .writerName(username)
                .build();
    }

    @Override
    public CommentResponseDto putLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(null);

        comment.setLikes(commentLikeService.getLikesCount(commentId));
        commentRepository.save(comment);


        return CommentResponseDto.builder()
                .id(comment.getId())
                .writerName(comment.getWriter().getUsername())
                .likes(comment.getLikes())
                .text(comment.getText())

                .build();
    }

    @Override
    public List<CommentResponseDto> getAllComments() {
        return  commentRepository.findAll()
                .stream().map(comment -> CommentResponseDto.builder()
                        .id(comment.getId())
                        .text(comment.getText())
                        .likes(comment.getLikes())
                        .writerName(comment.getWriter()!=null?comment.getWriter().getUsername():null)
                        .build()).toList();
    }
}
