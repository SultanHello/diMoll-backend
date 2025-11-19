package org.example.dimollbackend.audio.cover.repository;

import org.example.dimollbackend.audio.album.model.Album;
import org.example.dimollbackend.audio.cover.model.Cover;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoverRepository extends JpaRepository<Cover,Long> {
    List<Cover> findByTitleStartingWith(String text);
}
