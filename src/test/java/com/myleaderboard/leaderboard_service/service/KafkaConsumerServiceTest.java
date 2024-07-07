package com.myleaderboard.leaderboard_service.service;

import com.google.gson.Gson;
import com.myleaderboard.leaderboard_service.dto.PlayerScoreDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.ConsumerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KafkaConsumerServiceTest {

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Mock
    private PlayerScoreService playerScoreService;

    @Mock
    private Executor taskExecutor;

    @Mock
    private Gson gson;

    @Mock
    private ConsumerFactory<String, String> consumerFactory;

    @Mock
    private KafkaConsumer<String, String> consumer;

    @Value("${kafka.score-topic}")
    private String scoreTopic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(consumerFactory.createConsumer()).thenReturn(consumer);
        kafkaConsumerService.init();
    }

    @Test
    public void testInit() {
        verify(consumer, times(1)).subscribe(Collections.singletonList(scoreTopic));
    }

    @Test
    public void testPollKafka() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "key", "{\"userName\":\"player1\",\"gameId\":\"game1\",\"score\":100,\"createdAt\":\"2023-01-01T00:00:00Z\"}");
        ConsumerRecords<String, String> records = new ConsumerRecords<>(Collections.singletonMap(new org.apache.kafka.common.TopicPartition("topic", 0), Collections.singletonList(record)));

        when(consumer.poll(Duration.ofMillis(100))).thenReturn(records);

        kafkaConsumerService.pollKafka();

        verify(taskExecutor, times(1)).execute(any(Runnable.class));
        verify(consumer, times(1)).commitSync();
    }

    @Test
    public void testProcessMessage() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>("topic", 0, 0L, "key", "{\"userName\":\"player1\",\"gameId\":\"game1\",\"score\":100,\"createdAt\":\"2023-01-01T00:00:00Z\"}");
        PlayerScoreDto playerScoreDto = new PlayerScoreDto("player1", "game1", 100, new Date());

        when(gson.fromJson(record.value(), PlayerScoreDto.class)).thenReturn(playerScoreDto);

        kafkaConsumerService.processMessage(record);

        verify(playerScoreService, times(1)).saveScore(playerScoreDto);
    }
}