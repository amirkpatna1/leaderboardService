package com.myleaderboard.leaderboard_service.service;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.myleaderboard.leaderboard_service.constant.LeaderboardConstants.HASH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisServiceTest {

    @InjectMocks
    private RedisService redisService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ZSetOperations<String, Object> zSetOperations;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOperations);
    }

    @Test
    public void testAddScore() {
        PlayerScoreDto playerScoreDto = new PlayerScoreDto("player1", "game1", 100, new Date());

        redisService.addScore(playerScoreDto);

        verify(zSetOperations, times(1)).add(playerScoreDto.getGameId(), playerScoreDto.getUserName(), playerScoreDto.getScore());
    }

    @Test
    public void testGetTopKScores() {
        String gameId = "game1";
        long k = 3;
        long fixedTimestamp = 1627814400000L;
        String fixedKey = "player1" + HASH + 123 + HASH + fixedTimestamp;
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = new HashSet<>();
        typedTuples.add(new ZSetOperations.TypedTuple<Object>() {
            @Override
            public Object getValue() {
                return fixedKey;
            }

            @Override
            public Double getScore() {
                return 100.0;
            }

            @Override
            public int compareTo(ZSetOperations.TypedTuple<Object> o) {
                return 0;
            }
        });

        when(zSetOperations.reverseRangeWithScores(anyString(), eq(0L), eq(k - 1L))).thenReturn(typedTuples);

        List<PlayerScoreDto> result = redisService.getTopKScores(gameId, (int)k);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("player1", result.get(0).getUserName());
        assertEquals(100, result.get(0).getScore());
        assertEquals(new Date(fixedTimestamp), result.get(0).getCreatedAt());
        verify(zSetOperations, times(1)).reverseRangeWithScores(eq(gameId), eq(0L), eq(k - 1L));
    }

    @Test
    public void testGetUserRank() {
        String gameId = "game1";
        String username = "player1";

        when(zSetOperations.reverseRank(anyString(), anyString())).thenReturn(0L);

        Long rank = redisService.getUserRank(gameId, username);

        assertNotNull(rank);
        assertEquals(1, rank);
        verify(zSetOperations, times(1)).reverseRank(anyString(), anyString());
    }

    @Test
    public void testGetUserRankNull() {
        String gameId = "game1";
        String username = "player1";

        when(zSetOperations.reverseRank(anyString(), anyString())).thenReturn(null);

        Long rank = redisService.getUserRank(gameId, username);

        assertNull(rank);
        verify(zSetOperations, times(1)).reverseRank(anyString(), anyString());
    }
}