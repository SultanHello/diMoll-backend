package org.example.dimollbackend.audio.cover.repository;

import org.example.dimollbackend.audio.cover.model.Cover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverRepository extends JpaRepository<Cover,Long> {
}
