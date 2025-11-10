package org.example.dimollbackend.user.entity;


import jakarta.persistence.*;
import lombok.*;

import org.example.dimollbackend.audio.comment.model.Comment;
import org.example.dimollbackend.audio.cover.model.Cover;
import org.example.dimollbackend.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = true)
    private String email;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole=UserRole.USER;

    @ManyToMany
    @JoinTable(
            name = "liked", // таблица связи
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "cover_id")
    )
    private List<Cover> liked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Cover> ownedCovers=new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment;


    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = @JoinColumn(name = "follower_id"), // кто подписывается
            inverseJoinColumns = @JoinColumn(name = "following_id") // на кого подписан
    )
    private List<User> following = new ArrayList<>(); // мои подписки

    @ManyToMany(mappedBy = "following")
    private List<User> followers = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userRole == null) {
            return List.of(); // пустой список, если роль не задана
        }
        // добавляем префикс ROLE_ к enum и создаём GrantedAuthority
        return List.of(new SimpleGrantedAuthority("ROLE_" + userRole.name()));
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}
