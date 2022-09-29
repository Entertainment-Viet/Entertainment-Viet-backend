package com.EntertainmentViet.backend.features.talent.dto.packagetalent;

import com.EntertainmentViet.backend.features.booking.dto.jobdetail.CreateJobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class CreatePackageOrderDto {
    @NotNull
    private UUID organizerId;

    @NotNull
    private CreateJobDetailDto jobDetail;

    @NotNull
    private String paymentType;
}
