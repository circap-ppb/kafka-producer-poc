package com.flutter.smf.cbb.kafkaproducerpoc.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
public class GameDTO {
    private String id;
    private String competitionId;
    private Date startTime;
    private String venue;
    private List<TeamDTO> teamDTOS;
}
