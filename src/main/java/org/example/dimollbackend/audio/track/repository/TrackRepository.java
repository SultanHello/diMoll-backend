package org.example.dimollbackend.audio.track.repository;

import org.example.dimollbackend.audio.track.model.Track;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track,Long> {
    public Optional<Track> findById(Long trackId);


}
