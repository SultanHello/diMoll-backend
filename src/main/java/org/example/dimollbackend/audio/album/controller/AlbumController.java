package org.example.dimollbackend.audio.album.controller;


import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.album.model.Album;
import org.example.dimollbackend.audio.album.service.AlbumService;
import org.example.dimollbackend.dto.request.CreateAlbumDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audio/albums")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;
    @GetMapping()
    public List<Album> albums(){
        return albumService.allAlbums();
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAlbum(@RequestBody CreateAlbumDto createAlbumDto){
        return ResponseEntity.ok(albumService.createAlbum(createAlbumDto));
    }


}
