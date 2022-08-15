package com.EntertainmentViet.backend.features.common.dao;

import com.EntertainmentViet.backend.domain.entities.organizer.JobOffer;
import com.EntertainmentViet.backend.features.common.JoinExpression;

public abstract class BasePredicate<T> {

  public abstract JoinExpression<JobOffer> joinAll();
}
