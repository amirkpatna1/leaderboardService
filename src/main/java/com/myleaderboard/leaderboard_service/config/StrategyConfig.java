package com.myleaderboard.leaderboard_service.config;

import com.myleaderboard.leaderboard_service.strategy.RankingStrategy;
import com.myleaderboard.leaderboard_service.strategy.TieBreakStrategy;
import com.myleaderboard.leaderboard_service.strategy.impl.CombinedRankingStrategy;
import com.myleaderboard.leaderboard_service.strategy.impl.DefaultTieBreakStrategy;
import com.myleaderboard.leaderboard_service.strategy.impl.ScoreRankingStrategy;
import com.myleaderboard.leaderboard_service.strategy.impl.TimeRankingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfig {

    @Bean
    public RankingStrategy scoreRankingStrategy() {
        return new ScoreRankingStrategy();
    }

    @Bean
    public RankingStrategy timeRankingStrategy() {
        return new TimeRankingStrategy();
    }

    @Bean
    public RankingStrategy combinedRankingStrategy() {
        return new CombinedRankingStrategy();
    }

    @Bean
    public TieBreakStrategy defaultTieBreakStrategy() {
        return new DefaultTieBreakStrategy();
    }
}
