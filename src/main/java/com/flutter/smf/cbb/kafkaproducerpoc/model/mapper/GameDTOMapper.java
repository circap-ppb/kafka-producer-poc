package com.flutter.smf.cbb.kafkaproducerpoc.model.mapper;

import com.flutter.smf.cbb.kafkaproducerpoc.model.GameDTO;
import com.flutter.smf.se.bb.game.contract.Game;

public class GameDTOMapper {

    public static Game toProto(GameDTO gameDTO){
        Game game = Game.newBuilder()
                .setId("1")
                .setVenue("1")
                .setCompetitionId("1")
                .build();
        return  game;
    }

}
