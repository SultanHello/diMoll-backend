package org.example.dimollbackend.audio.comment.repository;

import org.example.dimollbackend.audio.comment.controller.CommentController;
import org.example.dimollbackend.audio.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
