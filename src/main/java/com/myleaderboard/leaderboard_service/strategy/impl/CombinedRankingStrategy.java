package com.myleaderboard.leaderboard_service.strategy.impl;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import com.myleaderboard.leaderboard_service.strategy.RankingStrategy;
import com.myleaderboard.leaderboard_service.strategy.TieBreakStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CombinedRankingStrategy implements RankingStrategy {

    @Override
    public List<PlayerScoreDto> rankPlayers(List<PlayerScoreDto> playerScores, TieBreakStrategy tieBreakStrategy) {
        List<PlayerScoreDto> playerScoreDtos = playerScores.stream()
                .sorted(Comparator.comparingInt(PlayerScoreDto::getScore).reversed()
                        .thenComparing(PlayerScoreDto::getCreatedAt))
                .collect(Collectors.toList());
        return tieBreakStrategy.applyTieBreaks(playerScoreDtos);
    }
}
