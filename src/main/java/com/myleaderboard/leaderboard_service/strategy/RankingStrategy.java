package com.myleaderboard.leaderboard_service.strategy;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;

import java.util.List;

public interface RankingStrategy {
    List<PlayerScoreDto> rankPlayers(List<PlayerScoreDto> playerScores, TieBreakStrategy tieBreakStrategy);
}
