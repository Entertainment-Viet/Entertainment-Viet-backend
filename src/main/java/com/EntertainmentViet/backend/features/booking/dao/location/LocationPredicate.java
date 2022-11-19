package com.EntertainmentViet.backend.features.booking.dao.location;

import java.util.UUID;

import com.EntertainmentViet.backend.domain.values.Location;
import com.EntertainmentViet.backend.domain.values.QLocation;
import com.EntertainmentViet.backend.domain.values.QLocationType;
import com.EntertainmentViet.backend.features.booking.dto.location.ListLocationParamDto;
import com.EntertainmentViet.backend.features.common.dao.IdentifiablePredicate;
import com.querydsl.core.types.ExpressionUtils;
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
	private final QLocationType parentLocationType = new QLocationType("parentLocationType");

	@Override
	public Predicate joinAll(JPAQueryFactory queryFactory) {
		queryFactory.selectFrom(location).distinct()
						.leftJoin(location.locationType(), locationType).fetchJoin()
						.leftJoin(location.parent(), parent).fetchJoin()
						.leftJoin(parent.locationType(), parentLocationType).fetchJoin()
						.fetch();

		return null;
	}

	public Predicate fromLocationParams(ListLocationParamDto paramDto) {
		var predicate = defaultPredicate();
		if (paramDto.getType() != null) {
			predicate = ExpressionUtils.allOf(
							predicate,
							location.locationType().type.eq(paramDto.getType())
			);
		}
		if (paramDto.getParentId() != null) {
			predicate = ExpressionUtils.allOf(
							predicate,
							location.parent().uid.eq(paramDto.getParentId())
			);
		}

		return predicate;
	}

	@Override
	public BooleanExpression uidEqual(UUID uid) {
		return location.uid.eq(uid);
	}
}