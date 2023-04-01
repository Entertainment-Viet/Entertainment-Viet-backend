package com.EntertainmentViet.backend.features.booking.dto.booking;

import com.EntertainmentViet.backend.config.MappingConfig;
import com.EntertainmentViet.backend.domain.entities.booking.Booking;
import com.EntertainmentViet.backend.domain.standardTypes.BookingStatus;
import com.EntertainmentViet.backend.features.admin.dto.bookings.AdminUpdateBookingDto;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.JobDetailMapper;
import com.EntertainmentViet.backend.features.booking.dto.jobdetail.ReadJobDetailDto;
import com.EntertainmentViet.backend.features.common.dto.EntityMapper;
import com.EntertainmentViet.backend.features.common.dto.ExtensionsMapper;
import com.EntertainmentViet.backend.features.common.dto.StandardTypeMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(uses = {
    JobDetailMapper.class,
    ExtensionsMapper.class,
    EntityMapper.class,
    StandardTypeMapper.class
}, config = MappingConfig.class)
public abstract class BookingMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id", "isReview"})
    @Mapping(target = "isPaid", source = "isPaid")
    @Mapping(target = "status", source = "status.i18nKey")
    @Mapping(target = "paymentType", source = "paymentType", qualifiedBy = StandardTypeMapper.ToPaymentTypeKey.class)
    @Mapping(target = "organizerId", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerUid.class)
    @Mapping(target = "organizerName", source = "organizer", qualifiedBy = EntityMapper.ToOrganizerName.class)
    @Mapping(target = "talentId", source = "talent", qualifiedBy = EntityMapper.ToTalentUid.class)
    @Mapping(target = "talentName", source = "talent", qualifiedBy = EntityMapper.ToTalentName.class)
    @Mapping(target = "packageId", source = "talentPackage", qualifiedBy = EntityMapper.ToPackageUid.class)
    @Mapping(target = "packageName", source = "talentPackage", qualifiedBy = EntityMapper.ToPackageName.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToJson.class)
    public abstract ReadBookingDto toReadDto(Booking booking);

    @BeanMapping(ignoreUnmappedSourceProperties = {"repeatPattern"})
    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "confirmedAt", ignore = true)
    @Mapping(target = "isPaid", constant = "false")
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "talentPackage", ignore = true)
    @Mapping(target = "bookingCode", ignore = true)
    @Mapping(target = "paymentType", source = "paymentType", qualifiedBy = StandardTypeMapper.ToPaymentType.class)
    @Mapping(target = "organizer", source = "organizerId", qualifiedBy = EntityMapper.ToOrganizerEntity.class)
    @Mapping(target = "talent", source = "talentId", qualifiedBy = EntityMapper.ToTalentEntity.class)
    @Mapping(target = "isReview", constant = "false")
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    public abstract Booking fromCreateDtoToModel(CreateBookingDto createBookingDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "confirmedAt", ignore = true)
    @Mapping(target = "isPaid", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "bookingCode", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "talentPackage", ignore = true)
    @Mapping(target = "isReview", ignore = true)
    @Mapping(target = "paymentType", source = "paymentType", qualifiedBy = StandardTypeMapper.ToPaymentType.class)
    @Mapping(target = "extensions", source = "extensions", qualifiedBy = ExtensionsMapper.ToNode.class)
    public abstract Booking fromUpdateDtoToModel(UpdateBookingDto updateBookingDto);

    @Mapping(target = "uid", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "jobDetail", ignore = true)
    @Mapping(target = "talentPackage", ignore = true)
    @Mapping(target = "paymentType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "extensions", ignore = true)
    @Mapping(target = "organizer", ignore = true)
    @Mapping(target = "talent", ignore = true)
    @Mapping(target = "paidAt", ignore = true)
    @Mapping(target = "bookingCode", ignore = true)
    @Mapping(target = "confirmedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "isReview", ignore = true)
    public abstract Booking fromAdminUpdateDtoToModel(AdminUpdateBookingDto updateBookingDto);

    // Only return non-confidential detail if token have enough permission
    public ReadBookingDto checkPermission(ReadBookingDto readBookingDto, boolean isOwnerUser) {
        if (!isOwnerUser) {
            return ReadBookingDto.builder()
                .uid(readBookingDto.getUid())
                .jobDetail(ReadJobDetailDto.builder()
                    .performanceStartTime(readBookingDto.getJobDetail().getPerformanceStartTime())
                    .performanceEndTime(readBookingDto.getJobDetail().getPerformanceEndTime())
                    .build()
                ).build();
        }
        return readBookingDto;
    }

    @Named("toBookingStatus")
    public BookingStatus toBookingStatus(String status) {
        return status != null ? BookingStatus.ofI18nKey(status) : null;
    }
}
