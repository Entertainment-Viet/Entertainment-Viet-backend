package com.EntertainmentViet.backend.features.booking.dao.location;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationPredicate extends IdentifiablePredicate<Location> {

	private final QLocation location = QLocation.location;
	private final QLocation parent = new QLocation("parent");
	private final QLocationType locationType = QLocationType.locationType;

	@Override
	public Predicate joinAll(JPAQueryFactory queryFactory) {
		queryFactory.selectFrom(location).distinct()
						.leftJoin(location.type, locationType).fetchJoin()
						.leftJoin(location.parent, parent).fetchJoin()
						.fetch();

		return null;
	}

	@Override
	public BooleanExpression uidEqual(UUID uid) {
		return location.uid.eq(uid);
	}
}