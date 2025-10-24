package org.example.dimollbackend.audio.artist.controller;


import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.audio.artist.service.ArtistService;
import org.example.dimollbackend.dto.request.CreateArtistDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audio/artists")
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService artistService;


    @GetMapping()
    public List<Artist> artists(){
        return artistService.getArtists();
    }
    @PostMapping("/create")
    public ResponseEntity<Artist> create(@RequestBody CreateArtistDto createArtistDto){
        return ResponseEntity.ok(artistService.createArtist(createArtistDto));
    }




}
