package com.myleaderboard.leaderboard_service.dto;

import com.google.gson.annotations.JsonAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerScoreDto {
    private String userName;
    private String gameId;
    private Integer score;
    private Integer rank;
    @JsonAdapter(TimestampAdapter.class)
    private Date createdAt;

    public PlayerScoreDto(String userName, String gameId, Integer score, Date createdAt) {
        this.userName = userName;
        this.gameId = gameId;
        this.score = score;
        this.createdAt = createdAt;
    }
}
