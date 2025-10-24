package org.example.dimollbackend.audio.comment.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.user.entity.User;

import java.util.Scanner;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    private Long likes;
    @ManyToOne(optional = true)
    @JoinColumn(name = "cover_id")
    private Cover owner;
    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

}
