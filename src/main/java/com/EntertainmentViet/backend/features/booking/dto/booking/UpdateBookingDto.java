package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.UpdateJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;


@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UpdateBookingDto {

    @NotNull
    private UpdateJobDetailDto jobDetail;

    private String paymentType;

    private String extensions;
}
