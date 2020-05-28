package com.flutter.smf.cbb.kafkaproducerpoc.controller;

import com.flutter.smf.cbb.kafkaproducerpoc.model.GameDTO;
import com.flutter.smf.cbb.kafkaproducerpoc.model.TeamDTO;
import com.flutter.smf.cbb.kafkaproducerpoc.model.TeamHostDesignationDTO;
import com.flutter.smf.cbb.kafkaproducerpoc.service.GameService;
import io.leangen.graphql.annotations.GraphQLArgument;
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
    public boolean createGame(@GraphQLNonNull @GraphQLArgument(name = "id") String id) {
        GameDTO gameDTO = generateGame(id);
        gameService.createGame(gameDTO);
        return true;
    }

    private GameDTO generateGame(@GraphQLNonNull String id) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setCompetitionId("1");
        gameDTO.setId(id);
        gameDTO.setStartTime(new Date());
        gameDTO.setVenue("2");
        List<TeamDTO> teams = generateTeam();
        gameDTO.setTeamDTOS(teams);
        return gameDTO;
    }

    private List<TeamDTO> generateTeam() {
        TeamDTO teamDTOA = new TeamDTO();
        teamDTOA.setId("A");
        teamDTOA.setType(TeamHostDesignationDTO.AWAY);

        TeamDTO teamDTOB = new TeamDTO();
        teamDTOB.setId("B");
        teamDTOB.setType(TeamHostDesignationDTO.HOME);
        return List.of(teamDTOA, teamDTOB);
    }

}
