package org.example.dimollbackend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}