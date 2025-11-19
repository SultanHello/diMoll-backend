package org.example.dimollbackend.audio.album.repository;

import org.example.dimollbackend.audio.album.model.Album;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album,Long> {
    void deleteAlbumByArtist_Id(Long id);
    List<Album> findByAlbumNameStartingWith(String text);

}
