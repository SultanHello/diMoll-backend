package org.example.dimollbackend.audio.artist.repository;

import org.example.dimollbackend.audio.album.model.Album;
import org.example.dimollbackend.audio.artist.model.Artist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
    Artist getArtistById(Long artistId);

    List<Artist> findByArtistNameStartingWith(String text);
}
