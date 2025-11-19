package org.example.dimollbackend.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TrackRequestDto {
    private Long id;

    private String title;

    private String genre;
    private int year;
}
