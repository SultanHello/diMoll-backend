package org.example.dimollbackend.audio.cover.service.impl;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.audio.cover.repository.CoverRepository;
import org.example.dimollbackend.audio.cover.service.CoverService;
import org.example.dimollbackend.audio.metadata.CoverMetadata;

import org.example.dimollbackend.audio.track.service.TrackService;

import org.example.dimollbackend.dto.response.CoverResponseDto;
import org.example.dimollbackend.exeption.NotFoundException;
import org.example.dimollbackend.file.config.properties.S3Properties;
import org.example.dimollbackend.file.exception.FileStorageException;
import org.example.dimollbackend.file.service.S3Service;
import org.example.dimollbackend.file.util.S3KeyUtils;
import org.example.dimollbackend.file.util.mapper.CoverMapper;
import org.example.dimollbackend.notification.email.service.EmailNotificationService;
import org.example.dimollbackend.user.entity.User;
import org.example.dimollbackend.user.repository.UserRepository;
import org.example.dimollbackend.user.service.UserService;

import org.springframework.core.io.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class CoverServiceImpl implements CoverService {
    private static final String MUSIC_DIR = "/Users/sultanasimbek/Documents/projects/diMoll-backend/covers/";
    private final UserService userService;
    private final CoverRepository coverRepository;
    private final TrackService trackService;
    private final S3Service s3Service;
    private final S3Properties s3Properties;
    private final CoverMapper coverMapper;
    private final UserRepository userRepository;
    private final EmailNotificationService emailNotificationService;

    @Override
    public CoverResponseDto uploadCover(MultipartFile file, CoverMetadata metadata,String username) {
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Файл пустой");
            }

            String generatedKey = S3KeyUtils.generateKey(file.getOriginalFilename());
            log.info("Uploading file '{}' to bucket '{}'", generatedKey, s3Properties.getBucket());
            s3Service.uploadFile(file, generatedKey);
            Cover createdFile = this.createCover(metadata,username, generatedKey);
            this.sendNotification(userService.findByUsername(username),metadata);
            return coverMapper.toCoverResponseDto(createdFile);

        } catch (IOException e) {
            throw new FileStorageException("Failed to read file content", e);
        } catch (S3Exception e) {
            throw new FileStorageException("Failed to upload file to S3", e);
        } catch (Exception e) {
            throw new FileStorageException("File upload failed", e);
        }
    }
    @Override
    public CoverResponseDto putLikeCover(Long coverId, String username) {
        Cover cover = coverRepository.findById(coverId).orElseThrow();
        User user = userService.findByUsername(username);
        user.getLiked().add(cover);

        userRepository.save(user);
        CoverResponseDto.builder()
                .id(cover.getId())
                .title(cover.getTitle())
                .creater(username)
                .build();

        return CoverResponseDto.builder()
                .id(cover.getId())
                .title(cover.getTitle())
                .creater(username)

                .build();
    }

    private void sendNotification(User user,CoverMetadata metadata) {
        List<String> emails = user.getFollowers().stream().map(User::getEmail).toList();
        for(String email:emails){
            emailNotificationService.sendNewTrackEmail(email,user.getUsername(),metadata.getTitle());
        }

    }

    private Cover createCover(CoverMetadata metadata,String username,String generatedKey){
        Cover cover = Cover.builder()
                .user(userService.findByUsername(username))
                .s3key(generatedKey)
                .title(metadata.getTitle())
                .track(trackService.findTrackById(metadata.getTrackId()))
                .build();

        return coverRepository.save(cover);
    }

//    public Resource getTrackResource(Long trackId) {
//        Cover cover = coverRepository.findById(trackId)
//                .orElseThrow(() -> new RuntimeException("Трек не найден"));
//
//        try {
//            File file = new File(cover.getAudioUrl());
//            return new FileSystemResource(file);
//        } catch (Exception e) {
//            throw new RuntimeException("Не удалось загрузить файл", e);
//        }
//    }

    @Override
    public List<Cover> getAllCovers() {
        return coverRepository.findAll();
    }

    @Override
    public Cover findCoverById(Long coverId) {
        return coverRepository.findById(coverId).orElseThrow(null);
    }

    public ResponseEntity<Resource> streamCoverById(Long id, String rangeHeader) {
        String key = getKeyById(id);
        return s3Service.streamAudio(key, rangeHeader);
    }

    @Override
    public void deleteCoverById(Long coverId,String username) {
        User user= userService.findByUsername(username);
        if(user.getOwnedCovers().stream().map(Cover::getId).toList().contains(coverId)){
            coverRepository.deleteById(coverId);
        }else {
            throw new NotFoundException();
        }
    }

    private String getKeyById(Long id){
        return coverRepository.findById(id).orElseThrow(null).getS3key();
    }


}
