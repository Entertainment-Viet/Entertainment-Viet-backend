package com.EntertainmentViet.backend.features.talent.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class PackageBookingDto {

    private UUID packageId;

    private UUID organizerId;
}
