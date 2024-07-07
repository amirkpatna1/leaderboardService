package com.myleaderboard.leaderboard_service.strategy.impl;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import com.myleaderboard.leaderboard_service.strategy.TieBreakStrategy;

import java.util.List;

public class DefaultTieBreakStrategy implements TieBreakStrategy {

    @Override
    public List<PlayerScoreDto> applyTieBreaks(List<PlayerScoreDto> rankedPlayers) {
        int currentRank = 1;
        for (int i = 0; i < rankedPlayers.size(); i += 1) {
            if (i > 0 && rankedPlayers.get(i).getScore().equals(rankedPlayers.get(i - 1).getScore())) {
                rankedPlayers.get(i).setRank(rankedPlayers.get(i - 1).getRank());
            } else {
                rankedPlayers.get(i).setRank(currentRank);
                currentRank += 1;
            }
        }
        return rankedPlayers;
    }
}
