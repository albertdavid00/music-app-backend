package com.unibuc.music.dtos;

import com.unibuc.music.models.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDto {
    @NotNull
    private ReactionType reactionType;
}
