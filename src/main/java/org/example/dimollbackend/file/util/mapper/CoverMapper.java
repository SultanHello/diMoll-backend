package org.example.dimollbackend.file.util.mapper;

import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.audio.track.model.Track;
import org.example.dimollbackend.dto.response.CoverResponseDto;
import org.example.dimollbackend.dto.response.TrackResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CoverMapper {

    @Mapping(target = "creater", source = "user.username")
    @Mapping(target = "trackName", source = "track.title")  // имя поля в Track!
    CoverResponseDto toCoverResponseDto(Cover cover);

    List<CoverResponseDto> toCoverResponseDtoList(List<Cover> covers);
}