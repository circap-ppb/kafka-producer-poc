package com.flutter.smf.cbb.kafkaproducerpoc.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TeamDTO {
    private String id;
    private TeamHostDesignationDTO type;
}
