package org.example.dimollbackend.audio.album.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.album.model.Album;
import org.example.dimollbackend.audio.album.repository.AlbumRepository;
import org.example.dimollbackend.audio.album.service.AlbumService;
import org.example.dimollbackend.audio.artist.service.ArtistService;
import org.example.dimollbackend.audio.metadata.MusicMetadata;
import org.example.dimollbackend.audio.track.model.Track;
import org.example.dimollbackend.audio.track.service.TrackService;
import org.example.dimollbackend.dto.request.AlbumRequestDto;
import org.example.dimollbackend.dto.request.CreateAlbumDto;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistService artistService;

    @Override
    public Album createAlbum(MusicMetadata metadata) {
        return Album.builder()
                .albumName(metadata.getAlbumName())
                .build();
    }

    @Override
    public Album findAlbumById(Long albumId) {
        return albumRepository.findById(albumId).orElseThrow(null);
    }

    @Override
    public String createAlbum(CreateAlbumDto createAlbumDto) {
        Album album = Album.builder()
                .albumName(createAlbumDto.getAlbumName()).build();
        album.setArtist(artistService.findArtistById(createAlbumDto.getArtistId()));
        albumRepository.save(album);
        return "success create album";
    }

    @Override
    public void saveAlbum(Album album) {
        albumRepository.save(album);


    }

    @Override
    public List<Album> allAlbums() {
        return albumRepository.findAll();
    }
    public String deleteAlbum(Long albumId){
        albumRepository.deleteById(albumId);
        return "deleted album";
    }

    @Override
    public List<AlbumRequestDto> searchAlbum(String text) {
        return albumRepository.findByAlbumNameStartingWith(text).stream().map(alb->AlbumRequestDto.builder()
                .id(alb.getId())
                .albumName(alb.getAlbumName())
                .artistName(alb.getArtist().getArtistName())
                .build()).toList();
    }

}
