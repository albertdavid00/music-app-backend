package com.unibuc.music.dtos;

import com.unibuc.music.models.Visibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private String description;
    @NotBlank
    private String videoUrl;
    private Visibility visibility;
}
