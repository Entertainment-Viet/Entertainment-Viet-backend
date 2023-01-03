package com.EntertainmentViet.backend.features.admin.dto.bookings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;


@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class AdminUpdateBookingDto {

    private Boolean isPaid;

    private List<String> finishProof;
}
