package com.epam.brest.kafka.writeProducer.service;

import com.epam.brest.kafka.model.SongEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@PropertySource("classpath:kafka.properties")
public class KafkaWriteSongProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaWriteSongProducerService.class);

    @Value("${kafka.topic1}")
    private String topic1;

    private final KafkaTemplate<Integer, SongEvent> kafkaTemplate;

    public KafkaWriteSongProducerService(KafkaTemplate<Integer, SongEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent (SongEvent songEvent){
        LOGGER.debug("sendEvent ({})", songEvent);
        ListenableFuture<SendResult<Integer, SongEvent>> future = kafkaTemplate.send(topic1, songEvent.getSong().getSongId(), songEvent);

        future.addCallback(new ListenableFutureCallback<>(){

            @Override
            public void onFailure(Throwable throwable) {
                LOGGER.warn("SongEvent was not delivered:{}, due to: {}",songEvent, throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<Integer, SongEvent> integerSongEventSendResult) {
                LOGGER.debug("Sent message=[{}] with offset=[{}]", songEvent, integerSongEventSendResult.getRecordMetadata().offset());
            }
        });
    }
}
