package com.flutter.smf.cbb.kafkaproducerpoc.kafka.producer;

import com.flutter.smf.feeds.bb.contract.FeedsBasketballGameScheduled;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class GameProducer {


    KafkaTemplate<String,String> kafkaTemplate;

    private static String TOPIC = "cbb.gameui.events";

    public ListenableFuture<SendResult<String, FeedsBasketballGameScheduled>> sendGameEventSyncProducerRecord(FeedsBasketballGameScheduled game) {

        String key = TOPIC;
        FeedsBasketballGameScheduled value = game;

        ProducerRecord producerRecord = buildProducerRecord(key,value,TOPIC);
        ListenableFuture<SendResult<String, FeedsBasketballGameScheduled>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, FeedsBasketballGameScheduled>>() {
            @Override
            public void onFailure(Throwable throwable) {
                handleFailure(key, value, throwable);
            }

            @Override
            public void onSuccess(SendResult<String, FeedsBasketballGameScheduled> result) {
                handleSuccess(key, value, result);
            }
        });
        return listenableFuture;
    }

    private ProducerRecord buildProducerRecord(String key, FeedsBasketballGameScheduled value, String topic) {
        //metadata about message
        List<Header> recordHeaders = List.of(
                new RecordHeader("event-source", "scanner".getBytes()),
                new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<String,FeedsBasketballGameScheduled>(topic, null,key,value,recordHeaders);
    }



    private void handleFailure(String key, FeedsBasketballGameScheduled value, Throwable ex) {
        log.error("Error sending the message and the exception is {}", ex.getMessage());
        try{
            throw  ex;
        } catch (Throwable throwable){
            log.error("Error in OnFailure: {}", throwable.getMessage());
        }
    }

    private void handleSuccess(String key, FeedsBasketballGameScheduled value, SendResult<String, FeedsBasketballGameScheduled> result) {
        log.info("Message sent successfully for the key : {} and the value is {} , partition is {}", key, value, result.getRecordMetadata().partition());
    }

}
