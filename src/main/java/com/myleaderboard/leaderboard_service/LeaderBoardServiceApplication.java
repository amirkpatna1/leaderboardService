package com.myleaderboard.leaderboard_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LeaderBoardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeaderBoardServiceApplication.class, args);
	}

}
