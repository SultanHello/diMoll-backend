package org.example.dimollbackend.audio.album.service;

import org.example.dimollbackend.audio.album.model.Album;
import org.example.dimollbackend.audio.metadata.MusicMetadata;
import org.example.dimollbackend.dto.request.CreateAlbumDto;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface AlbumService {
    public Album createAlbum(MusicMetadata metadata);

    public Album findAlbumById(Long albumId);
    String createAlbum(CreateAlbumDto createAlbumDto);
    public void saveAlbum(Album album);
    public List<Album> allAlbums();

    String deleteAlbum(Long albumId);

//    void deleteByArtistId(List<Long> listOfAlbumId);
}
