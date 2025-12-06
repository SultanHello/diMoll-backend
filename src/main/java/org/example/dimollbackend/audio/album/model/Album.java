package org.example.dimollbackend.audio.album.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dimollbackend.audio.artist.model.Artist;
import org.example.dimollbackend.audio.track.model.Track;


import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "albums")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "AlbumName")
    private String albumName;


    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<Track> tracks=new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "artist_id") // FK на Artist
    private Artist artist;
}
