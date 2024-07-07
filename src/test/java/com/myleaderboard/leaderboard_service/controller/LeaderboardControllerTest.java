package com.myleaderboard.leaderboard_service.controller;

import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import com.myleaderboard.leaderboard_service.service.PlayerScoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

public class LeaderboardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PlayerScoreService playerScoreService;

    @InjectMocks
    private LeaderboardController leaderboardController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(leaderboardController).build();
    }

    @Test
    public void testPostGameScore() throws Exception {
        String gameId = "1";
        int k = 3;
        List<PlayerScoreDto> scores = List.of(
                new PlayerScoreDto("player1", gameId, 100, new Date()),
                new PlayerScoreDto("player2", gameId, 95, new Date())
        );

        when(playerScoreService.getTopKScores(anyString(), anyInt())).thenReturn(scores);

        mockMvc.perform(get("/leaderboard/v1/score/{gameId}", gameId)
                        .param("k", String.valueOf(k))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].userName", is("player1")))
                .andExpect(jsonPath("$[0].score", is(100)))
                .andExpect(jsonPath("$[1].userName", is("player2")))
                .andExpect(jsonPath("$[1].score", is(95)));
    }
}