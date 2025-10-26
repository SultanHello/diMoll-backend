package org.example.dimollbackend.audio.track.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.metadata.TrackMetadata;
import org.example.dimollbackend.audio.track.model.Track;
import org.example.dimollbackend.audio.track.service.TrackService;
import org.example.dimollbackend.dto.response.MusicResponseDto;
import org.example.dimollbackend.dto.response.TrackResponseDto;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;


@RequiredArgsConstructor
@RestController
@RequestMapping("/audio/tracks")
public class TrackController {
    private final TrackService trackService;
    private final ObjectMapper objectMapper;


    @GetMapping("/{trackId}/listen")
    public ResponseEntity<String> listen(@PathVariable Long trackId) {
        String audioFile = trackService.getAudioFile(trackId);
        return ResponseEntity.ok(audioFile);
    }

    @GetMapping
    public ResponseEntity<Page<TrackResponseDto>> tracks(Pageable pageable) {
        return ResponseEntity.ok(trackService.getTracks(pageable));
    }

    @GetMapping("/{trackId}")
    public ResponseEntity<MusicResponseDto> findTrack(@RequestParam Long trackId){
        return ResponseEntity.ok(trackService.findTrack(trackId));
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTrack(
            @RequestParam("file") MultipartFile file,
            @RequestParam("metadata") String metadataJson
    ) throws Exception {
        TrackMetadata metadata = objectMapper.readValue(metadataJson, TrackMetadata.class);
        trackService.uploadTrack(file, metadata);
        return ResponseEntity.ok("Трек сохранён: ");
    }
    @DeleteMapping("/{trackId}/delete")
    public ResponseEntity<TrackResponseDto> deleteTrack(@RequestParam Long trackId){

        return ResponseEntity.ok(trackService.deleteTrackById(trackId));
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamTrack(@PathVariable Long id, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        return trackService.streamTrackById(id, rangeHeader);
    }
}
