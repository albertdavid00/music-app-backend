package com.unibuc.music.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Builder
public class CommentDto {
    @NotBlank
    private String content;
    private String username;
    private Instant creationTime;
}

