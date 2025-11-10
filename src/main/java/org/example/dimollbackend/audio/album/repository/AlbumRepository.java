package org.example.dimollbackend.audio.album.repository;

import org.example.dimollbackend.audio.album.model.Album;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album,Long> {
    void deleteAlbumByArtist_Id(Long id);
}
