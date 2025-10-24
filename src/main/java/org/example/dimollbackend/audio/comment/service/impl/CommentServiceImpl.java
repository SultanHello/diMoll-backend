package org.example.dimollbackend.audio.comment.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.comment.model.Comment;
import org.example.dimollbackend.audio.comment.repository.CommentRepository;
import org.example.dimollbackend.audio.comment.service.CommentService;

import org.example.dimollbackend.audio.cover.service.CoverService;
import org.example.dimollbackend.dto.request.CommentRequestDto;
import org.example.dimollbackend.dto.response.CommentResponseDto;

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
    @Override
    public CommentResponseDto createComment(String username, CommentRequestDto commentRequestDto) {
        System.out.println(userService.findByUsername(username).getUsername());
        Comment comment = Comment.builder()
                .owner(coverService.findCoverById(commentRequestDto.getCoverId()))
                .text(commentRequestDto.getText())
                .writer(userService.findByUsername(username))
                .build();
        System.out.println(comment.getWriter());
        commentRepository.save(comment);
        return CommentResponseDto.builder()
                .text(comment.getText())
                .writerName(username)
                .build();
    }

    @Override
    public CommentResponseDto putLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(null);
        ;
        comment.setLikes(commentLikeService.getLikesCount(commentId));
        commentRepository.save(comment);
        System.out.println(comment.getWriter());

        return CommentResponseDto.builder()
                .id(comment.getId())
                .writerName(comment.getWriter().getUsername())
                .likes(comment.getLikes())
                .text(comment.getText())

                .build();
    }


//    return trackRepository.findAll(pageable)
//            .map(track -> TrackResponseDto.builder()
//            .id(track.getId())
//            .title(track.getTitle())
//            .genre(track.getGenre())
//            .year(track.getYear())
//            .albumName(track.getAlbum() != null ? track.getAlbum().getAlbumName() : null)
//            .authorName(track.getAlbum() != null && track.getAlbum().getArtist() != null
//            ? track.getAlbum().getArtist().getArtistName() : null)
//            .coverTitles(track.getCovers() != null
//            ? track.getCovers().stream()
//                                .map(Cover::getTitle)   // вытаскиваем title из каждого Cover
//                                .collect(Collectors.toList())
//            : Collections.emptyList())
//            .build());

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
