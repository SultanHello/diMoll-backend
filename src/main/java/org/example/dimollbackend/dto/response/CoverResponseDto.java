package org.example.dimollbackend.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dimollbackend.audio.comment.model.Comment;
import org.example.dimollbackend.audio.track.model.Track;
import org.example.dimollbackend.user.entity.User;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoverResponseDto {

    private Long id;
    private String title;

    private String creater;

    private String trackName;

}
