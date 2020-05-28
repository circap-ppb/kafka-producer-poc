package com.flutter.smf.cbb.kafkaproducerpoc.model.mapper;


import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Mapper
public interface TimestampMapper {

    @Named("timestampDateMapper")
    static Timestamp dateToTimestamp(Date date) {
        if (Objects.isNull(date)) {
            return Timestamp.newBuilder().build();
        }
        Instant instant = Instant.ofEpochMilli(date.getTime());
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
    }

    @Named("timestampDateMapper")
    static Date timestampToDate(Timestamp timestamp) {
        if (Objects.isNull(timestamp)) {
            return new Date(0);
        }
        return Date.from(Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()));
    }

}

