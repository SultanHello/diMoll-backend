package org.example.dimollbackend.audio.cover.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.dimollbackend.audio.comment.repository.CommentRepository;
import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.audio.cover.service.CoverService;
import org.example.dimollbackend.audio.metadata.CoverMetadata;
import org.example.dimollbackend.dto.response.CommentResponseDto;
import org.example.dimollbackend.dto.response.CoverResponseDto;
import org.example.dimollbackend.dto.response.TrackResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audio/covers")
public class CoverController {
    private final CoverService coverService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<Cover>> covers(){
        return ResponseEntity.ok(coverService.getAllCovers());
    }



    @PostMapping("/upload")
    public ResponseEntity<CoverResponseDto> uploadCover(
            @RequestParam("file") MultipartFile file,
            @RequestParam("metadata") String metadataJson,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws Exception {
        CoverMetadata metadata = objectMapper.readValue(metadataJson, CoverMetadata.class);
        coverService.uploadCover(file, metadata,userDetails.getUsername());
        return ResponseEntity.ok(coverService.uploadCover(file, metadata,userDetails.getUsername()));
    }

    @DeleteMapping("/{coverId}/delete")
    public ResponseEntity<String> deleteTrack(@RequestParam Long coverId,@AuthenticationPrincipal UserDetails userDetails){
        coverService.deleteCoverById(coverId,userDetails.getUsername());
        return ResponseEntity.ok("cover success deleted");
    }


    @PutMapping("{coverId}/putLike")
    public ResponseEntity<CoverResponseDto> putLikeCover(@PathVariable Long coverId, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(coverService.putLikeCover(coverId,userDetails.getUsername()));
    }

    @GetMapping("/{id}/stream")
    public ResponseEntity<Resource> streamTrack(@PathVariable Long id, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        return coverService.streamCoverById(id, rangeHeader);
    }

}
