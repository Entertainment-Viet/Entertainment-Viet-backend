package com.EntertainmentViet.backend.features.talent.dto.talent;

import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
public class ListTalentParamDto {

    private String displayName;

    private UUID category;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime endTime;

    private Double maxPrice;

    private Double minPrice;

    private String currency;

    private String city;

    private String district;

    private String street;
}
