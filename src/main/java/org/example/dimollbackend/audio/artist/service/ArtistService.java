package org.example.dimollbackend.audio.artist.service;

import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.dto.request.ArtistRequestDto;
import org.example.dimollbackend.dto.request.CreateArtistDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ArtistService {
    public Artist createArtist(CreateArtistDto metadata);
    public void saveArtist(Artist artist);
    public Page<ArtistRequestDto> getArtists(Pageable pageable);
    Artist findArtistById(Long id);

    String deleteArtist(Long artistId);

    List<ArtistRequestDto> searchArtist(String text);
}
