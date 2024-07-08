package com.myleaderboard.leaderboard_service.strategy.impl;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import com.myleaderboard.leaderboard_service.strategy.TieBreakStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreRankingStrategyTest {

    private ScoreRankingStrategy scoreRankingStrategy;

    private TieBreakStrategy tieBreakStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tieBreakStrategy = new DefaultTieBreakStrategy();
        scoreRankingStrategy = new ScoreRankingStrategy();
    }

    @Test
    void testRankPlayers() {
        PlayerScoreDto player1 = new PlayerScoreDto("player1", "game1", 100, new Date(1627814671000L));
        PlayerScoreDto player2 = new PlayerScoreDto("player2", "game1", 150, new Date(1627814672000L));
        PlayerScoreDto player3 = new PlayerScoreDto("player3", "game1", 100, new Date(1627814673000L));

        List<PlayerScoreDto> players = Arrays.asList(player1, player2, player3);

        List<PlayerScoreDto> rankedPlayers = scoreRankingStrategy.rankPlayers(players, tieBreakStrategy);

        assertEquals(player2, rankedPlayers.get(0));
        assertEquals(player1, rankedPlayers.get(1));
        assertEquals(player3, rankedPlayers.get(2));
    }
}