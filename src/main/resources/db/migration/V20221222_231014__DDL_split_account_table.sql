-- Refactor account_type and user_type
DROP TYPE IF EXISTS user_type CASCADE;
CREATE TYPE user_type AS ENUM (
    'INDIVIDUAL',
    'CORPORATION'
);

ALTER TABLE organizer
DROP COLUMN account_type;

ALTER TABLE talent
DROP COLUMN account_type;

DROP TYPE IF EXISTS account_type CASCADE;
CREATE TYPE account_type AS ENUM (
    'TALENT',
    'ORGANIZER',
    'ADMIN'
);

-- Table: account --
DROP TABLE IF EXISTS account CASCADE;
CREATE TABLE account (
    id BIGINT NOT NULL,
    uid UUID NOT NULL,
    display_name TEXT NOT NULL,
    account_type ACCOUNT_TYPE,
   CONSTRAINT pk_account PRIMARY KEY (id)
);

-- Table: user --
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
  id BIGINT NOT NULL,
   created_at TIMESTAMP WITH TIME ZONE,
   user_state USER_STATE,
   user_type USER_TYPE,
   archived BOOLEAN NOT NULL DEFAULT FALSE,
   CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT FK_USER_ON_ACCOUNT FOREIGN KEY (id) REFERENCES account (id);
-- Move data from organizer and talent table to account and users table
INSERT INTO account
SELECT id, uid, display_name, 'ORGANIZER'::ACCOUNT_TYPE
FROM organizer;

INSERT INTO users
SELECT id, created_at, user_state
FROM organizer;

INSERT INTO account
SELECT id, uid, display_name, 'TALENT'::ACCOUNT_TYPE
FROM talent;

INSERT INTO users
SELECT id, created_at, user_state
FROM talent;

INSERT INTO account
SELECT id, uid, display_name, 'ADMIN'::ACCOUNT_TYPE
FROM admin;

-- Delete duplicated column in organizer and talent
ALTER TABLE organizer
DROP COLUMN uid,
DROP COLUMN display_name,
DROP COLUMN created_at,
DROP COLUMN user_state,
DROP COLUMN archived;

ALTER TABLE talent
DROP COLUMN uid,
DROP COLUMN display_name,
DROP COLUMN created_at,
DROP COLUMN user_state,
DROP COLUMN archived;

ALTER TABLE admin
DROP COLUMN uid,
DROP COLUMN display_name;

-- Adding foreign key mapping
ALTER TABLE organizer ADD CONSTRAINT FK_ORGANIZER_ON_USER FOREIGN KEY (id) REFERENCES users (id);
ALTER TABLE talent ADD CONSTRAINT FK_TALENT_ON_USER FOREIGN KEY (id) REFERENCES users (id);
