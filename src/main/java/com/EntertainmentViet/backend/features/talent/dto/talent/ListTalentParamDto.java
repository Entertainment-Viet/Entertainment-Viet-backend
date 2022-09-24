package com.EntertainmentViet.backend.features.talent.dto.talent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
public class ListTalentParamDto {

    private String displayName;

    // category name
    private String category;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endTime;

    // TODO
//    private Integer budget;
}
