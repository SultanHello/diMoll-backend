package org.example.dimollbackend.audio.track.service;


import org.example.dimollbackend.dto.request.TrackRequestDto;
import org.springframework.core.io.Resource;
import org.example.dimollbackend.audio.metadata.TrackMetadata;
import org.example.dimollbackend.audio.track.model.Track;
import org.example.dimollbackend.dto.response.MusicResponseDto;
import org.example.dimollbackend.dto.response.TrackResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.util.List;

@Service
public interface TrackService {
    public void saveTrack(Track track);

    Page<TrackResponseDto> getTracks(Pageable pageable);

    MusicResponseDto findTrack(Long trackId);
    Track findTrackById(Long trackId);

    String getAudioFile(Long trackId);
    public Resource getTrackResource(Long trackId);
    public TrackResponseDto uploadTrack(MultipartFile file, TrackMetadata trackMetadata);

    ResponseEntity<Resource> streamTrackById(Long id, String rangeHeader);

    TrackResponseDto deleteTrackById(Long trackId);

    List<TrackRequestDto> searchTrack(String text);
//    StreamingResponseBody streamTrackById(Long id);
}
