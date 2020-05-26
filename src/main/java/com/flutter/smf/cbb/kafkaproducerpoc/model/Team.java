package com.flutter.smf.cbb.kafkaproducerpoc.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Team {
    private String id;
    private TeamHostDesignation type;
}
