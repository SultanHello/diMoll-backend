package org.example.dimollbackend.audio.artist.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.audio.artist.repository.ArtistRepository;
import org.example.dimollbackend.audio.artist.service.ArtistService;
import org.example.dimollbackend.dto.request.CreateArtistDto;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;

    @Override
    public Artist createArtist(CreateArtistDto createArtistDto) {
        Artist artist = Artist.builder()
                .artistName(createArtistDto.getArtistName())
                .artistAge(createArtistDto.getArtistAge())
                .build();
        this.saveArtist(artist);
        return artist;
    }





    @Override
    public void saveArtist(Artist artist) {
        artistRepository.save(artist);
    }

    @Override
    public List<Artist> getArtists() {
        return artistRepository.findAll();
    }

    @Override
    public Artist findArtistById(Long id) {
        return artistRepository.findById(id).orElseThrow(null);
    }
}
