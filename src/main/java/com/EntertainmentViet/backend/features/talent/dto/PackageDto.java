package com.EntertainmentViet.backend.features.talent.dto;

import com.EntertainmentViet.backend.features.booking.dto.BookingDto;
import com.EntertainmentViet.backend.features.booking.dto.JobDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class PackageDto {

    private List<BookingDto> orders;

    private String name;

    private boolean isActive;

    private UUID talentId;

    private JobDetailDto jobDetail;
}
