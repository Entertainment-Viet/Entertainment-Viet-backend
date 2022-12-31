DROP TABLE IF EXISTS app_config CASCADE;
CREATE TABLE app_config (
  id BIGINT NOT NULL,
   property_name TEXT NOT NULL,
   property_value TEXT NOT NULL,
   CONSTRAINT pk_app_config PRIMARY KEY (id)
);