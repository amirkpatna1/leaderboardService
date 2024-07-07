package com.myleaderboard.leaderboard_service.controller;


import com.myleaderboard.leaderboard_service.service.PlayerScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaderboard/v1")
public class LeaderboardController {
    @Autowired
    private PlayerScoreService playerScoreService;
    @GetMapping("/score/{gameId}")
    public ResponseEntity<Object> postGameScore(@PathVariable String gameId , @RequestParam("k") int k) {
        return ResponseEntity.ok(playerScoreService.getTopKScores(gameId, k));
    }
}
