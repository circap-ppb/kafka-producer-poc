package com.flutter.smf.cbb.kafkaproducerpoc.controller;

import com.flutter.smf.cbb.kafkaproducerpoc.model.GameDTO;
import com.flutter.smf.cbb.kafkaproducerpoc.model.Team;
import com.flutter.smf.cbb.kafkaproducerpoc.model.TeamHostDesignation;
import com.flutter.smf.cbb.kafkaproducerpoc.service.GameService;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@GraphQLApi
@Component
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GraphQLMutation(name = "createGame")
    public boolean createGame(@GraphQLNonNull String id) {
        GameDTO gameDTO = generateGame(id);
        convertgameDTOtoProtobuf(gameDTO);
        gameService.createGame(gameDTO);

        return true;
    }

    private void convertgameDTOtoProtobuf(GameDTO gameDTO) {

    }

    private GameDTO generateGame(@GraphQLNonNull String id) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setCompetition_id("1");
        gameDTO.setId(id);
        gameDTO.setStart_time(new Date());
        gameDTO.setVenue("2");
        Team teamA = generateTeam();
        gameDTO.setTeams(List.of(teamA));
        return gameDTO;
    }

    private Team generateTeam() {
        Team teamA = new Team();
        teamA.setId("A");
        teamA.setType(TeamHostDesignation.AWAY);
        return teamA;
    }

}
