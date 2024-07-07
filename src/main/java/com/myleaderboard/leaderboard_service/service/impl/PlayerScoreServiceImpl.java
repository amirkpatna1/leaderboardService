package com.myleaderboard.leaderboard_service.service.impl;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import com.myleaderboard.leaderboard_service.entity.PlayerScore;
import com.myleaderboard.leaderboard_service.exception.GenericException;
import com.myleaderboard.leaderboard_service.repository.PlayerScoreRepository;
import com.myleaderboard.leaderboard_service.service.PlayerScoreService;
import com.myleaderboard.leaderboard_service.service.RedisService;
import com.myleaderboard.leaderboard_service.strategy.RankingStrategy;
import com.myleaderboard.leaderboard_service.strategy.TieBreakStrategy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.myleaderboard.leaderboard_service.constant.LeaderboardConstants.HASH;

@Service
@Slf4j
public class PlayerScoreServiceImpl implements PlayerScoreService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private PlayerScoreRepository playerScoreRepository;

    @Value("${strategy.tie-break-strategy}")
    private String tieBreakStrategyName;

    @Value("${strategy.ranking-strategy}")
    private String rankingStrategyName;


    @Autowired
    private ApplicationContext applicationContext;

    private TieBreakStrategy tieBreakStrategy;

    private RankingStrategy rankingStrategy;

    @PostConstruct
    private void initStrategy() {
        this.tieBreakStrategy = applicationContext.getBean(tieBreakStrategyName, TieBreakStrategy.class);
        this.rankingStrategy = applicationContext.getBean(rankingStrategyName, RankingStrategy.class);
    }


    @Override
    @Transactional
    public void saveScore(PlayerScoreDto playerScoreDto) {
        try {
            PlayerScore playerScore = PlayerScore
                    .builder()
                    .score(playerScoreDto.getScore())
                    .userName(playerScoreDto.getUserName())
                    .createdAt(playerScoreDto.getCreatedAt())
                    .gameId(playerScoreDto.getGameId())
                    .build();
            playerScore = playerScoreRepository.save(playerScore);
            String key = String.format("%s%s%s%s%s",playerScoreDto.getUserName(), HASH, playerScore.getId(), HASH, playerScoreDto.getCreatedAt().getTime());
            playerScoreDto.setUserName(key);
            redisService.addScore(playerScoreDto);
        } catch (Exception exception) {
            throw new GenericException(String.format("Some exception occurred while saving data for %s", playerScoreDto.getUserName()), exception);
        }
    }

    @Override
    public List<PlayerScoreDto> getTopKScores(String gameId, int k) {
        log.info("getTopKScores: started processing request to get {} top scores for gameId {}", k, gameId);
        List<PlayerScoreDto> playerScoreDtos = redisService.getTopKScores(gameId, k);
        return rankingStrategy.rankPlayers(playerScoreDtos, tieBreakStrategy);
    }
}
