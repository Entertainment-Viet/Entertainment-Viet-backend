package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadBookingDto extends Identifiable {

    private OffsetDateTime createdAt;

    private OffsetDateTime confirmedAt;

    private Boolean isPaid;

    private String status;

    private UUID organizerUid;

    private UUID talentUid;

    private ReadJobDetailDto jobDetail;

    private String paymentType;

    private String extensions;
}
