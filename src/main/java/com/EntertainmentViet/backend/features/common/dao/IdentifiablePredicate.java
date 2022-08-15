package com.EntertainmentViet.backend.features.common.dao;

import com.EntertainmentViet.backend.domain.entities.Identifiable;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.UUID;

public abstract class IdentifiablePredicate<T extends Identifiable> extends BasePredicate<T> {

  public abstract BooleanExpression uidEqual(UUID uid);
}
