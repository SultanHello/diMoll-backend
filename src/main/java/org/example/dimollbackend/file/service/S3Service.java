package org.example.dimollbackend.file.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

public interface S3Service {
    void uploadFile(MultipartFile file, String keyName) throws IOException;
    byte[] downloadFile(String keyName);
    ResponseEntity<Resource> streamAudio(String key, String rangeHeader);
    //    void deleteFile(String keyName);
//    void renameFile(String oldKey, String newKey);
}
