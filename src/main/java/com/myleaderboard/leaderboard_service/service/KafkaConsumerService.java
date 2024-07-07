package com.myleaderboard.leaderboard_service.service;

import com.google.gson.Gson;
import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Executor;

@Service
@Slf4j
public class KafkaConsumerService {
    @Value("${kafka.score-topic}")
    private String scoreTopic;
    @Autowired
    private PlayerScoreService playerScoreService;
    @Autowired
    private Executor taskExecutor;
    @Autowired
    private Gson gson;

    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    private KafkaConsumer<String, String> consumer;

    @PostConstruct
    public void init() {
        consumer = (KafkaConsumer<String, String>) consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList(scoreTopic));
    }

    @Scheduled(fixedRate = 1000)
    public void pollKafka() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            taskExecutor.execute(() -> processMessage(record));
        }
        consumer.commitSync();
    }

    public void processMessage(ConsumerRecord<String, String> record) {
        log.info("processMessage: Started processing events for {}", record);
        PlayerScoreDto playerScoreDto = gson.fromJson(record.value(), PlayerScoreDto.class);
        playerScoreService.saveScore(playerScoreDto);
    }
}
