package org.example.dimollbackend.file.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dimollbackend.file.config.properties.S3Properties;
import org.example.dimollbackend.file.exception.FileStorageException;
import org.example.dimollbackend.file.service.S3Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Properties s3Properties;
    private final S3Client s3Client;

    @Override
    public void uploadFile(MultipartFile file, String keyName) throws IOException {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(s3Properties.getBucket())
                            .key(keyName)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new FileStorageException("Failed to read file content: " + file.getOriginalFilename(), e);
        } catch (S3Exception e) {
            throw new FileStorageException("Failed to upload file to S3: " + keyName, e);
        }
    }

    @Override
    public byte[] downloadFile(String keyName) {
        try {
            ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(s3Properties.getBucket())
                            .key(keyName)
                            .build());
            return objectAsBytes.asByteArray();
        } catch (NoSuchKeyException e) {
            throw new FileStorageException("File with key '" + keyName + "' not found in S3", e);
        } catch (S3Exception e) {
            throw new FileStorageException("Failed to download file from S3: " + keyName, e);
        }
    }


    public ResponseEntity<Resource> streamAudio(String key, String rangeHeader) {
        log.info("▶️ Starting streamAudio() for key: {}", key);
        log.debug("Received Range header: {}", rangeHeader);

        // 1️⃣ Get file metadata
        HeadObjectResponse head = s3Client.headObject(
                HeadObjectRequest.builder()
                        .bucket(s3Properties.getBucket())
                        .key(key)
                        .build()
        );

        long fileSize = head.contentLength();
        long start = 0;
        long end = fileSize - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            try {
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()) {
                    end = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException e) {
                log.warn("⚠️ Failed to parse Range header '{}': {}", rangeHeader, e.getMessage());
            }
        }

        long contentLength = end - start + 1;

        log.debug("📏 File size: {}", fileSize);
        log.debug("📦 Byte range: {} - {}", start, end);
        log.debug("📤 Content-Length for response: {}", contentLength);

        // 2️⃣ Build S3 request with byte range
        String rangeValue = "bytes=" + start + "-" + end;
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getBucket())
                .key(key)
                .range(rangeValue)
                .build();

        log.info("📡 Sending S3 request with range: {}", rangeValue);

        ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getRequest);
        InputStreamResource resource = new InputStreamResource(s3Object);

        HttpStatus status = (rangeHeader == null) ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;
        log.info("✅ Returning status: {}", status);
        log.debug("📋 Response headers: Content-Range=bytes {}-{}/{}", start, end, fileSize);

        // 3️⃣ Return proper HTTP response
        return ResponseEntity.status(status)
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileSize)
                .body(resource);
    }


//
//    @Override
//    public void deleteFile(String keyName) {
//        try {
//            deleteObject(keyName);
//        } catch (S3Exception e) {
//            throw new FileStorageException("Failed to delete file from S3: " + keyName, e);
//        }
//    }
//
//    @Override
//    public void renameFile(String oldKey, String newKey) {
//        try {
//            copyObject(oldKey, newKey);
//            deleteObject(oldKey);
//        } catch (S3Exception e) {
//            throw new FileStorageException("Failed to rename file in S3: " + oldKey + " → " + newKey, e);
//        }
//    }
//
//    private void copyObject(String oldKey, String newKey) {
//        s3Client.copyObject(CopyObjectRequest.builder()
//                .sourceBucket(s3Properties.getBucket())
//                .sourceKey(oldKey)
//                .destinationBucket(s3Properties.getBucket())
//                .destinationKey(newKey)
//                .build());
//    }
//
//    private void deleteObject(String keyName) {
//        s3Client.deleteObject(DeleteObjectRequest.builder()
//                .key(keyName)
//                .bucket(s3Properties.getBucket())
//                .build());
//    }
}
