package org.example.dimollbackend.audio.track.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.dimollbackend.audio.album.model.Album;
import org.example.dimollbackend.audio.cover.model.Cover;


import java.time.LocalDateTime;
import java.util.List;

@Entity

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String genre;
    private int year;

    @Column(name = "s3_key", nullable = false, unique = true, length = 500)
    private String s3key;// ссылка на S3

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cover> covers;


    private LocalDateTime uploadedAt = LocalDateTime.now();


}