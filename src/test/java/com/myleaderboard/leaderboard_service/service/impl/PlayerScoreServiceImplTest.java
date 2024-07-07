package com.myleaderboard.leaderboard_service.service.impl;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import com.myleaderboard.leaderboard_service.entity.PlayerScore;
import com.myleaderboard.leaderboard_service.exception.GenericException;
import com.myleaderboard.leaderboard_service.repository.PlayerScoreRepository;
import com.myleaderboard.leaderboard_service.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayerScoreServiceImplTest {

    @InjectMocks
    private PlayerScoreServiceImpl playerScoreService;

    @Mock
    private RedisService redisService;

    @Mock
    private PlayerScoreRepository playerScoreRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveScoreSuccess() {
        PlayerScoreDto playerScoreDto = new PlayerScoreDto("player1", "game1", 100, new Date());
        PlayerScore playerScore = new PlayerScore();
        playerScore.setId(123L);
        playerScore.setScore(100);
        playerScore.setUserName("player1");
        playerScore.setCreatedAt(playerScoreDto.getCreatedAt());
        playerScore.setGameId("game1");

        when(playerScoreRepository.save(any(PlayerScore.class))).thenReturn(playerScore);

        assertDoesNotThrow(() -> playerScoreService.saveScore(playerScoreDto));
        verify(playerScoreRepository, times(1)).save(any(PlayerScore.class));
        verify(redisService, times(1)).addScore(any(PlayerScoreDto.class));
    }

    @Test
    public void testSaveScoreException() {
        PlayerScoreDto playerScoreDto = new PlayerScoreDto("player1", "game1", 100, new Date());

        when(playerScoreRepository.save(any(PlayerScore.class))).thenThrow(new RuntimeException("Database error"));

        GenericException exception = assertThrows(GenericException.class, () -> playerScoreService.saveScore(playerScoreDto));
        assertEquals("Some exception occurred while saving data for player1", exception.getMessage());
        verify(playerScoreRepository, times(1)).save(any(PlayerScore.class));
        verify(redisService, never()).addScore(any(PlayerScoreDto.class));
    }

    @Test
    public void testGetTopKScoresSuccess() {
        String gameId = "game1";
        int k = 3;
        List<PlayerScoreDto> scores = List.of(
                new PlayerScoreDto("player1", gameId, 100, new Date()),
                new PlayerScoreDto("player2", gameId, 95, new Date())
        );

        when(redisService.getTopKScores(anyString(), anyInt())).thenReturn(scores);

        List<PlayerScoreDto> result = playerScoreService.getTopKScores(gameId, k);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("player1", result.get(0).getUserName());
        assertEquals(100, result.get(0).getScore());
        verify(redisService, times(1)).getTopKScores(anyString(), anyInt());
    }

    @Test
    public void testGetTopKScoresException() {
        String gameId = "game1";
        int k = 3;

        when(redisService.getTopKScores(anyString(), anyInt())).thenThrow(new RuntimeException("Redis error"));

        Exception exception = assertThrows(RuntimeException.class, () -> playerScoreService.getTopKScores(gameId, k));
        assertEquals("Redis error", exception.getMessage());
        verify(redisService, times(1)).getTopKScores(anyString(), anyInt());
    }
}