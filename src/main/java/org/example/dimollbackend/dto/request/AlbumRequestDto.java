package org.example.dimollbackend.dto.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.audio.track.model.Track;

import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AlbumRequestDto {
    private Long id;


    private String albumName;



    private String artistName;
}
