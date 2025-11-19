package org.example.dimollbackend.audio.cover.service;


import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.audio.metadata.CoverMetadata;
import org.example.dimollbackend.dto.request.CoverRequestDto;
import org.example.dimollbackend.dto.response.CoverResponseDto;
import org.example.dimollbackend.dto.response.TrackResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@Service
public interface CoverService {

//    Resource getTrackResource(Long id);


    public CoverResponseDto uploadCover(MultipartFile file, CoverMetadata metadata,String username);

    List<Cover> getAllCovers();

    Cover findCoverById(Long coverId);
    public CoverResponseDto putLikeCover(Long coverId, String username);

    ResponseEntity<Resource> streamCoverById(Long id, String rangeHeader);

    void deleteCoverById(Long coverId,String username);

    List<CoverRequestDto> searchCover(String text);


    //CoverResponseDto putLike(Long coverId, String username);
}
