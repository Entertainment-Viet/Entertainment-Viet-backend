package com.EntertainmentViet.backend.features.booking.dao.locationaddress;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.values.LocationAddress;
import com.EntertainmentViet.backend.domain.values.QLocationAddress;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationAddressPredicate extends IdentifiablePredicate<LocationAddress> {

	private final QLocationAddress locationAddress = QLocationAddress.locationAddress;

	@Override
	public Predicate joinAll(JPAQueryFactory queryFactory) {
		queryFactory.selectFrom(locationAddress).distinct()
						.fetch();

		return null;
	}

	@Override
	public BooleanExpression uidEqual(UUID uid) {
		return locationAddress.uid.eq(uid);
	}
}