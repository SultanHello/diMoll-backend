package org.example.dimollbackend.audio.metadata;


import lombok.Data;

@Data
public class TrackMetadata {
    private String title;
    private String genre;
    private int year;
    private Long albumId;
}
