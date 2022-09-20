package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadBookingDto extends Identifiable {

    private Instant createdAt;

    private Instant confirmedAt;

    private boolean isPaid;

    private String status;

    private UUID organizerUid;

    private UUID talentUid;

    private ReadJobDetailDto jobDetail;
}
