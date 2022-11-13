package com.EntertainmentViet.backend.features.organizer.dto.event;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.EntertainmentViet.backend.features.booking.dto.locationaddress.LocationAddressDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class ReadEventDto extends IdentifiableDto {

  private String name;

  private Boolean isActive;

  private LocationAddressDto occurrenceAddress;

  private OffsetDateTime occurrenceStartTime;

  private OffsetDateTime occurrenceEndTime;

  private UUID organizerId;

  private String organizerName;

  private String description;

  private List<String> legalPaper;

  private Integer openPositionsCount;
}
