package org.example.dimollbackend.audio.artist.service;

import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.dto.request.CreateArtistDto;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ArtistService {
    public Artist createArtist(CreateArtistDto metadata);
    public void saveArtist(Artist artist);
    public List<Artist> getArtists();
    Artist findArtistById(Long id);
}
