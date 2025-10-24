package org.example.dimollbackend.audio.artist.repository;

import org.example.dimollbackend.audio.artist.model.Artist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
}
