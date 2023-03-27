DROP TABLE IF EXISTS user_deal_fee_rate CASCADE;
CREATE TABLE user_deal_fee_rate (
  user_id BIGINT NOT NULL,
  fee_rate DOUBLE PRECISION NOT NULL,
  CONSTRAINT pk_user_deal_fee_rate PRIMARY KEY (user_id)
);
ALTER TABLE user_deal_fee_rate ADD CONSTRAINT FK_USERDEALFEERATE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
