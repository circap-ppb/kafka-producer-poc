package com.flutter.smf.cbb.kafkaproducerpoc.service;

import com.flutter.smf.cbb.kafkaproducerpoc.kafka.producer.GameProducer;
import com.flutter.smf.cbb.kafkaproducerpoc.model.GameDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(staticName = "of")
public class GameService {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public void createGame(GameDTO gameDTO){
        GameProducer gameProducer = new GameProducer(kafkaTemplate);
        gameProducer.sendGameEventSyncProducerRecord(gameDTO);
    }
}
