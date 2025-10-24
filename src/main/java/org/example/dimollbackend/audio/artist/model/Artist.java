package org.example.dimollbackend.audio.artist.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.dimollbackend.audio.album.model.Album;


import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="artists")
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String artistName;

    private int artistAge;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Album> albums=new ArrayList<>();


}
