package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class BookingDto extends Identifiable {

    private Instant createdAt;

    private boolean isPaid;

    private String status;

    private String organizerUid;

    private String talentUid;

    private JobDetailDto jobDetailDto;
}
