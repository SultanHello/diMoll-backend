package org.example.dimollbackend.audio.artist.controller;


import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.audio.artist.service.ArtistService;
import org.example.dimollbackend.dto.request.AlbumRequestDto;
import org.example.dimollbackend.dto.request.ArtistRequestDto;
import org.example.dimollbackend.dto.request.CreateArtistDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audio/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;


    @GetMapping()
    public Page<ArtistRequestDto> artists(@RequestParam int page,@RequestParam int size){
        Pageable pageable= PageRequest.of(page,size);
        return artistService.getArtists(pageable);
    }
    @PostMapping("/create")
    public ResponseEntity<Artist> create(@RequestBody CreateArtistDto createArtistDto){
        return ResponseEntity.ok(artistService.createArtist(createArtistDto));
    }
    @DeleteMapping("/{artistId}/delete")
    public ResponseEntity<String> delete(@PathVariable Long artistId){
        return ResponseEntity.ok(artistService.deleteArtist(artistId));
    }
    @GetMapping("/search")
    public ResponseEntity<List<ArtistRequestDto>> search(@RequestParam String text){
        return ResponseEntity.ok(artistService.searchArtist(text));
    }

}
