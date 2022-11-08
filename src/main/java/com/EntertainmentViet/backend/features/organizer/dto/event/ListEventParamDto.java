package com.EntertainmentViet.backend.features.organizer.dto.event;

import java.time.OffsetDateTime;

import com.EntertainmentViet.backend.domain.standardTypes.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
public class ListEventParamDto {

  private String name;

  private Boolean isActive;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endTime;

  // organizer name
  private String organizer;

  private Double maxPrice;

  private Double minPrice;

  private Currency currency;

  private String city;

  private String district;

  private String street;
}
