package com.myleaderboard.leaderboard_service.service;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;

import java.util.List;

public interface PlayerScoreService {
    void saveScore(PlayerScoreDto playerScoreDto);
    List<PlayerScoreDto> getTopKScores(String gameId, int k);
}
