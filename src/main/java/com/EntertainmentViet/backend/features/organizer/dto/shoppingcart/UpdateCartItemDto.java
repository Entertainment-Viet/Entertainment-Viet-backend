package com.EntertainmentViet.backend.features.organizer.dto.shoppingcart;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class UpdateCartItemDto {

    private Double suggestedPrice;
}
