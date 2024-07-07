package com.myleaderboard.leaderboard_service.service;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.myleaderboard.leaderboard_service.constant.LeaderboardConstants.HASH;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addScore(PlayerScoreDto playerScoreDto) {
        redisTemplate.opsForZSet().add(playerScoreDto.getGameId(), playerScoreDto.getUserName(), playerScoreDto.getScore());
    }

    public List<PlayerScoreDto> getTopKScores(String gameId, int k) {
        List<PlayerScoreDto> playerScoreDtos = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(gameId, 0, k - 1);

        if(Objects.nonNull(typedTuples)) {
            for(ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
                String key = Objects.requireNonNull(typedTuple.getValue()).toString();
                PlayerScoreDto playerScoreDto = splitAndPopulateTimestamp(key);
                playerScoreDto.setGameId(gameId);
                playerScoreDto.setScore(Objects.requireNonNull(typedTuple.getScore()).intValue());
                playerScoreDtos.add(playerScoreDto);
            }
        }
        return playerScoreDtos;
    }

    private PlayerScoreDto splitAndPopulateTimestamp(String key) {
        String[] splittedKey = key.split(HASH);
        return PlayerScoreDto
                .builder()
                .userName(splittedKey[0])
                .createdAt(new Date(Long.parseLong(splittedKey[2])))
                .build();
    }

    public Long getUserRank(String gameId, String username) {
        Long rank = redisTemplate.opsForZSet().reverseRank(gameId, username);
        return (rank != null) ? rank + 1 : null;
    }
}
