package org.example.dimollbackend.audio.track.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.dimollbackend.audio.cover.model.Cover;

import org.example.dimollbackend.file.config.properties.S3Properties;
import org.example.dimollbackend.file.exception.FileStorageException;
import org.example.dimollbackend.file.service.S3Service;
import org.example.dimollbackend.file.util.S3KeyUtils;
import org.example.dimollbackend.file.util.mapper.TrackMapper;
import org.example.dimollbackend.user.entity.User;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.album.service.AlbumService;
import org.example.dimollbackend.audio.metadata.TrackMetadata;
import org.example.dimollbackend.audio.track.model.Track;
import org.example.dimollbackend.audio.track.repository.TrackRepository;

import org.example.dimollbackend.audio.track.service.TrackService;
import org.example.dimollbackend.dto.response.MusicResponseDto;

import org.example.dimollbackend.dto.response.TrackResponseDto;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class TrackServiceImpl implements TrackService {
    private final TrackRepository trackRepository;
    private final AlbumService albumService;
    private final S3Properties s3Properties;
    private final TrackMapper trackMapper;
    private final S3Service s3Service;
    private final S3Client s3Client;




    @Override
    public void saveTrack(Track track) {
        trackRepository.save(track);
    }


    @Override
    public TrackResponseDto uploadTrack(MultipartFile file, TrackMetadata trackMetadata) {
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Файл пустой");
            }

            String generatedKey = S3KeyUtils.generateKey(file.getOriginalFilename());
            log.info("Uploading file '{}' to bucket '{}'", generatedKey, s3Properties.getBucket());

            s3Service.uploadFile(file, generatedKey);

            Track createdFile = this.createTrack(trackMetadata, generatedKey);
            return trackMapper.toTrackResponseDto(createdFile);

        } catch (IOException e) {
            throw new FileStorageException("Failed to read file content", e);
        } catch (S3Exception e) {
            throw new FileStorageException("Failed to upload file to S3", e);
        } catch (Exception e) {
            throw new FileStorageException("File upload failed", e);
        }
    }





    public ResponseEntity<Resource> streamTrackById(Long id, String rangeHeader) {
        String key = getKeyById(id);
        return s3Service.streamAudio(key, rangeHeader);
    }

    @Override
    public TrackResponseDto deleteTrackById(Long trackId) {
        Track track = trackRepository.findById(trackId).orElse(null);
        this.deleteCloudTrack(track);
        trackRepository.deleteById(trackId);
        return getTrackResponseDto(track);
    }

    private static TrackResponseDto getTrackResponseDto(Track track) {
        return TrackResponseDto.builder()
                .title(track.getTitle())
                .id(track.getId())
                .albumName(track.getAlbum().getAlbumName())
                .genre(track.getGenre())
                .build();
    }

    private void deleteCloudTrack(Track track) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(track.getS3key())
                .build();
        s3Client.deleteObject(deleteRequest);
    }




    private String getKeyById(Long id){
        return trackRepository.findById(id).orElseThrow(null).getS3key();
    }

    @Override
    public MusicResponseDto findTrack(Long trackId) {
        Track track = trackRepository.findById(trackId).orElse(null);
        return MusicResponseDto.builder()
                .trackName(track.getTitle())
                .albumName(track.getAlbum().getAlbumName())
                .artistName(track.getAlbum().getArtist().getArtistName())
                .build();
    }

    @Override
    public Track findTrackById(Long trackId) {
        return trackRepository.findById(trackId)
                .orElseThrow(() -> new EntityNotFoundException("Track not found with id: " + trackId));

    }

    @Override
    public String getAudioFile(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));
        return track.getS3key();
    }

    private Track createTrack(TrackMetadata metadata, String generatedKey) {
        return trackRepository.save(
                Track.builder()
                        .album(albumService.findAlbumById(metadata.getAlbumId()))
                        .s3key(generatedKey)
                        .genre(metadata.getGenre())
                        .year(metadata.getYear())
                        .title(metadata.getTitle())
                        .build());
    }


    public Resource getTrackResource(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Трек не найден"));

        try {
            File file = new File(track.getS3key());
            return new FileSystemResource(file);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить файл", e);
        }
    }


    @Override
    public Page<TrackResponseDto> getTracks(Pageable pageable) {
        return trackRepository.findAll(pageable)
                .map(track -> TrackResponseDto.builder()
                        .id(track.getId())
                        .title(track.getTitle())
                        .genre(track.getGenre())
                        .year(track.getYear())
                        .albumName(track.getAlbum() != null ? track.getAlbum().getAlbumName() : null)
                        .authorName(track.getAlbum() != null && track.getAlbum().getArtist() != null
                                ? track.getAlbum().getArtist().getArtistName() : null)
                        .coverTitles(track.getCovers() != null
                                ? track.getCovers().stream()
                                .map(Cover::getTitle)   // вытаскиваем title из каждого Cover
                                .collect(Collectors.toList())
                                : Collections.emptyList())
                        .build());
    }

}
