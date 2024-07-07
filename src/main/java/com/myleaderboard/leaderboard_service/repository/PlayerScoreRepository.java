package com.myleaderboard.leaderboard_service.repository;

import com.myleaderboard.leaderboard_service.entity.PlayerScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerScoreRepository extends JpaRepository<PlayerScore, String> {

}
