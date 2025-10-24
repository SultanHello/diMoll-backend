package org.example.dimollbackend.file.util.mapper;

import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.audio.track.model.Track;
import org.example.dimollbackend.dto.response.TrackResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrackMapper {

    @Mapping(target = "albumName", source = "album.albumName")
    @Mapping(target = "coverTitles", expression = "java(getCoverTitles(track.getCovers()))")
    @Mapping(target = "authorName", source = "album.artist.artistName")
    TrackResponseDto toTrackResponseDto(Track track);

//    @Mapping(target = "albumName", source = "album.albumName")
//    @Mapping(target = "coverTitles", expression = "java(getCoverTitles(track.getCovers()))")
//    @Mapping(target = "authorName", source = "album.artist.artistName")
//    List<TrackResponseDto> toFileResponseDtoList(List<Track> tracks);
    default List<String> getCoverTitles(List<Cover> covers) {
        if (covers == null) return List.of();
        return covers.stream()
                .map(Cover::getTitle)
                .collect(Collectors.toList());
}
}