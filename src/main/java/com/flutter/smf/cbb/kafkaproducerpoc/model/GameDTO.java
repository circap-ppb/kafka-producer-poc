package com.flutter.smf.cbb.kafkaproducerpoc.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
public class GameDTO {
    private String id;
    private String competition_id;
    private Date start_time;
    private String venue;
    private List<Team> teams;
}
