-- Add score_type table --
DROP TABLE IF EXISTS score_type CASCADE;
CREATE TABLE score_type (
  id BIGINT NOT NULL,
  name TEXT NOT NULL,
  rate DOUBLE PRECISION NOT NULL,
  CONSTRAINT pk_scoretype PRIMARY KEY (id)
);

-- Insert default song score_type
INSERT INTO score_type (id, name, rate)
VALUES (1, 'song', '1');

-- Add priority_score table --
DROP TABLE IF EXISTS priority_score CASCADE;
CREATE TABLE priority_score (
  id BIGINT NOT NULL,
  talent_id BIGINT NOT NULL,
  score_type_id BIGINT NOT NULL,
  proof TEXT[],
  approved BOOLEAN NOT NULL,
  achievement TEXT NOT NULL,
  CONSTRAINT pk_priorityscore PRIMARY KEY (id)
);

ALTER TABLE priority_score ADD CONSTRAINT FK_PRIORITYSCORE_ON_TALENT FOREIGN KEY (talent_id) REFERENCES talent (id);
ALTER TABLE priority_score ADD CONSTRAINT FK_PRIORITYSCORE_ON_SCORE_TYPE FOREIGN KEY (score_type_id) REFERENCES score_type (id);
