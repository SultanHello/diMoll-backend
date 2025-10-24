package org.example.dimollbackend.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicResponseDto {
    private String artistName;
    private String albumName;
    private String trackName;
    private int time;
}
