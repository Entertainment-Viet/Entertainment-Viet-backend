package com.EntertainmentViet.backend.features.organizer.dto.joboffer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ListJobOfferParamDto {
    private String name;
    private Boolean isActive;

    // organizer name
    private String organizer;
    // category name
    private String category;
}
