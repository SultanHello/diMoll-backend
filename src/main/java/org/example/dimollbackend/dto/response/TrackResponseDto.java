package org.example.dimollbackend.dto.response;

import lombok.*;

import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data

public class TrackResponseDto {
    private Long id;

    private String title;

    private String genre;
    private int year;
    private List<String> coverTitles;

    private String authorName;
    private String albumName;

}

