-- Table: talent_category --

DROP TABLE IF EXISTS talent_category CASCADE;
CREATE TABLE talent_category (
  talent_id BIGINT NOT NULL,
   category_id BIGINT NOT NULL,
   CONSTRAINT pk_talent_category PRIMARY KEY (talent_id, category_id)
);

ALTER TABLE talent_category ADD CONSTRAINT fk_talentcate_on_talent FOREIGN KEY (talent_id) REFERENCES talent (id);
ALTER TABLE talent_category ADD CONSTRAINT fk_talentcate_on_category FOREIGN KEY (category_id) REFERENCES category (id);