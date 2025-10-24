package org.example.dimollbackend.dto.request;


import lombok.Data;

@Data
public class CreateAlbumDto {
    private String albumName;
    private Long artistId;
}
