package com.EntertainmentViet.backend.features.booking.dto.booking;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadBookingDto extends Identifiable {

    private OffsetDateTime createdAt;

    private OffsetDateTime confirmedAt;

    private Boolean isPaid;

    private String status;

    private UUID organizerId;

    private String organizerName;

    private UUID talentId;

    private String talentName;

    private UUID packageId;

    private String packageName;

    private ReadJobDetailDto jobDetail;

    private String paymentType;

    private List<String> finishProof;

    private String extensions;
}
