package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.CreateJobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.RepeatPattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class CreateBookingDto {

    @NotNull
    private UUID organizerId;

    @NotNull
    private UUID talentId;

    @NotNull
    private CreateJobDetailDto jobDetail;

    @NotNull
    private String paymentType;

    private List<String> finishProof;

    private String extensions;

    private RepeatPattern repeatPattern;
}
