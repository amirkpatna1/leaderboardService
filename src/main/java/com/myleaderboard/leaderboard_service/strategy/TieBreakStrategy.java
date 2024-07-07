package com.myleaderboard.leaderboard_service.strategy;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;

import java.util.List;

public interface TieBreakStrategy {
    List<PlayerScoreDto> applyTieBreaks(List<PlayerScoreDto> rankedPlayers);
}
