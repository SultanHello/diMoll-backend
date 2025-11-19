package org.example.dimollbackend.dto.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dimollbackend.audio.album.model.Album;

import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArtistRequestDto {

    private Long id;


    private String artistName;

    private int artistAge;



}
