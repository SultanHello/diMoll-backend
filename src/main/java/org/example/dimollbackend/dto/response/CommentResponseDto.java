package org.example.dimollbackend.dto.response;


import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String text;
    private String writerName;
    private Long likes;
}
