package com.EntertainmentViet.backend.config.db;

import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.spi.MetadataBuilderContributor;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.SerializableType;

public class SqlFunctionMetadataBuilderContributor implements MetadataBuilderContributor {
  @Override
  public void contribute(MetadataBuilder metadataBuilder) {
    metadataBuilder.applySqlFunction(
        "search_talent", new StandardSQLFunction("search_talent", SerializableType.INSTANCE)
    );
    metadataBuilder.applySqlFunction(
        "search_event", new StandardSQLFunction("search_event", SerializableType.INSTANCE)
    );
  }
}
