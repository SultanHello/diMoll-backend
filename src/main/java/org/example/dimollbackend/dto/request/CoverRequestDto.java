package org.example.dimollbackend.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CoverRequestDto {

    private Long id;
    private String title;

    private String creater;

    private String trackName;

}
