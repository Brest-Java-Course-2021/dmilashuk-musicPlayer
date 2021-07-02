package com.epam.brest.kafka.consumerDao.service;

import com.epam.brest.dao.SongDao;
import com.epam.brest.kafka.model.SongEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:kafka.properties")
public class KafkaReadSongConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaReadSongConsumer.class);

    @Value("${kafka.topic1}")
    private String topic1;

    private final SongDao songDao;

    public KafkaReadSongConsumer(SongDao songDao) {
        this.songDao = songDao;
    }

    @KafkaListener(topics = "songEvents")
    public void listenGroupFoo(SongEvent songEvent) {
        LOGGER.debug("listenGroupFoo({})", songEvent);
        switch (songEvent.getEventType()){
            case  CREATE:
                LOGGER.debug("CREATE:{}", songEvent.getSong());
                songDao.create(songEvent.getSong());
                break;
            case UPDATE:
                LOGGER.debug("UPDATE:{}", songEvent.getSong());
                songDao.update(songEvent.getSong());
                break;
            case DELETE:
                LOGGER.debug("DELETE:{}", songEvent.getSong().getSongId());
                songDao.delete(songEvent.getSong().getSongId());
                break;
            default:
                LOGGER.warn("EventType is null or incorrect");
        }
    }
}
