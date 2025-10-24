package org.example.dimollbackend.audio.metadata;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MusicMetadata {
    private String title;
    private String genre;
    private String artistName;
    private String albumName;
    private int year;

    // getters / setters
}