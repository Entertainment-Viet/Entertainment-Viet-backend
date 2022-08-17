package com.EntertainmentViet.backend.features.booking.dto;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(config = MappingConfig.class)
public abstract class BookingMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "createdAt", "paid", "status", "organizer", "talent", "jobDetail"})
    public abstract BookingDto toDto(Booking booking);
}
