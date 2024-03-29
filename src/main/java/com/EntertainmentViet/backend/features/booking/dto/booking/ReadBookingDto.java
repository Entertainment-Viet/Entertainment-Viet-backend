package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class ReadBookingDto extends IdentifiableDto {

    private OffsetDateTime createdAt;

    private OffsetDateTime confirmedAt;

    private Boolean isPaid;

    private OffsetDateTime paidAt;

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

    private String bookingCode;

    private String extensions;
}
