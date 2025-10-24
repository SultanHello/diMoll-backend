package org.example.dimollbackend.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommentRequestDto {
    private String text;
    private Long coverId;

}
