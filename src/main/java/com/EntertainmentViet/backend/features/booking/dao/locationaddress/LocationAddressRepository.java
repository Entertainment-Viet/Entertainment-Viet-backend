package com.EntertainmentViet.backend.features.booking.dao.locationaddress;

import javax.transaction.Transactional;

import com.EntertainmentViet.backend.domain.values.LocationAddress;
import com.EntertainmentViet.backend.features.common.dao.IdentifiableRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface LocationAddressRepository extends IdentifiableRepository<LocationAddress> {

}

