package org.example.dimollbackend.audio.artist.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.audio.artist.repository.ArtistRepository;
import org.example.dimollbackend.audio.artist.service.ArtistService;
import org.example.dimollbackend.dto.request.ArtistRequestDto;
import org.example.dimollbackend.dto.request.CreateArtistDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class ArtistServiceImpl implements ArtistService {
    private final ArtistRepository artistRepository;
    private final RedisTemplate<String, Object> redisTemplate;




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
    public Page<ArtistRequestDto> getArtists(Pageable pageable) {
        Page<Artist> page = artistRepository.findAll(pageable);

        // Преобразуем элементы, сохраняя страницу
        return page.map(getArtistRequestDtoFunction());
    }

    private static Function<Artist, ArtistRequestDto> getArtistRequestDtoFunction() {
        return artist -> ArtistRequestDto.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .artistAge(artist.getArtistAge())
                .build();
    }

    @Override
    public Artist findArtistById(Long id) {
        return artistRepository.findById(id).orElseThrow(null);
    }



    @Override
    public String deleteArtist(Long artistId) {
        artistRepository.deleteById(artistId);
        return "deleted artist";
    }

    @Override
    public List<ArtistRequestDto> searchArtist(String text) {
        return artistRepository.findByArtistNameStartingWith(text).stream()
                .map(getArtistArtistRequestDtoFunction()).toList();
    }

    private static Function<Artist, ArtistRequestDto> getArtistArtistRequestDtoFunction() {
        return artist -> ArtistRequestDto.builder()
                .id(artist.getId())
                .artistName(artist.getArtistName())
                .artistAge(artist.getArtistAge())
                .build();
    }

}
