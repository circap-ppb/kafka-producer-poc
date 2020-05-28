package com.flutter.smf.cbb.kafkaproducerpoc.model.mapper;

import com.flutter.smf.cbb.kafkaproducerpoc.model.GameDTO;
import com.flutter.smf.cbb.kafkaproducerpoc.model.TeamDTO;
import com.flutter.smf.cbb.kafkaproducerpoc.model.TeamHostDesignationDTO;
import com.flutter.smf.se.bb.contract.TeamHostDesignation;
import com.flutter.smf.se.bb.game.contract.Game;
import com.flutter.smf.se.bb.game.contract.Team;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        uses = TimestampMapper.class)
public interface GameDTOMapper {

    GameDTOMapper INSTANCE = Mappers.getMapper( GameDTOMapper.class );

    @Mapping(source = "teamDTOS", target = "teamsList")
    @Mapping(source = "startTime", target = "startTime", qualifiedByName = "timestampDateMapper")
    Game toProto(GameDTO gameDTO);

    @Mapping(source = "teamsList", target = "teamDTOS")
    @Mapping(source = "startTime", target = "startTime", qualifiedByName = "timestampDateMapper")
    GameDTO fromProto(Game game);


    Team toProto(TeamDTO teamDTO);

    TeamDTO fromProto(Team team);

    TeamHostDesignation toProto (TeamHostDesignationDTO orderType);

    TeamHostDesignationDTO fromProto (TeamHostDesignation orderType);

}
