package com.EntertainmentViet.backend.features.admin.dto;

import com.EntertainmentViet.backend.features.common.dto.IdentifiableDto;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TalentFeedBackDto extends IdentifiableDto {
    // TODO
}
