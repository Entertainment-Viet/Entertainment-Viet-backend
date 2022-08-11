-- Define Enum type

DROP TYPE IF EXISTS user_state CASCADE;
CREATE TYPE user_state AS ENUM (
    'GUEST',
    'VERIFIED',
    'CHARGEABLE',
    'ARCHIVED'
);

DROP TYPE IF EXISTS booking_status CASCADE;
CREATE TYPE booking_status AS ENUM (
    'ORGANIZER_PENDING',
    'TALENT_PENDING',
    'CONFIRMED',
    'CANCELLED',
    'FINISHED'
);

DROP TYPE IF EXISTS currency_type CASCADE;
CREATE TYPE currency_type AS ENUM (
    'VND',
    'USD',
    'EUR'
);

DROP TYPE IF EXISTS work_type CASCADE;
CREATE TYPE work_type AS ENUM (
    'SINGLE_TIME',
    'SINGLE_SHOW',
    'PERIOD_CONTRACT'
);

-- Table: organizer --

DROP TABLE IF EXISTS organizer CASCADE;
CREATE TABLE organizer (
  id BIGINT NOT NULL,
   phone_number TEXT,
   email TEXT,
   address TEXT,
   bio TEXT,
   created_at TIMESTAMP WITH TIME ZONE,
   language TEXT,
   user_state USER_STATE,
   display_name TEXT NOT NULL,
   uid UUID NOT NULL,
   CONSTRAINT pk_organizer PRIMARY KEY (id)
);

-- Table: talent --

DROP TABLE IF EXISTS talent CASCADE;
CREATE TABLE talent (
  id BIGINT NOT NULL,
   phone_number TEXT,
   email TEXT,
   address TEXT,
   bio TEXT,
   created_at TIMESTAMP WITH TIME ZONE,
   language TEXT,
   user_state USER_STATE,
   display_name TEXT NOT NULL,
   uid UUID NOT NULL,
   CONSTRAINT pk_talent PRIMARY KEY (id)
);

-- Table: admin --

DROP TABLE IF EXISTS admin CASCADE;
CREATE TABLE admin (
  id BIGINT NOT NULL,
   display_name TEXT NOT NULL,
   uid UUID NOT NULL,
   CONSTRAINT pk_admin PRIMARY KEY (id)
);

-- Table: talent_feedback --

DROP TABLE IF EXISTS talent_feedback CASCADE;
CREATE TABLE talent_feedback (
  id BIGINT NOT NULL,
   created_at TIMESTAMP WITH TIME ZONE,
   content TEXT,
   admin_id BIGINT,
   uid UUID NOT NULL,
   talent_id BIGINT NOT NULL,
   CONSTRAINT pk_talentfeedback PRIMARY KEY (id)
);

ALTER TABLE talent_feedback ADD CONSTRAINT FK_TALENTFEEDBACK_ON_ADMIN FOREIGN KEY (admin_id) REFERENCES admin (id);
ALTER TABLE talent_feedback ADD CONSTRAINT FK_TALENTFEEDBACK_ON_TALENT FOREIGN KEY (talent_id) REFERENCES talent (id);

-- Table: organizer_feedback --

DROP TABLE IF EXISTS organizer_feedback CASCADE;
CREATE TABLE organizer_feedback (
  id BIGINT NOT NULL,
   created_at TIMESTAMP WITH TIME ZONE,
   content TEXT,
   admin_id BIGINT,
   uid UUID NOT NULL,
   organizer_id BIGINT NOT NULL,
   CONSTRAINT pk_organizerfeedback PRIMARY KEY (id)
);

ALTER TABLE organizer_feedback ADD CONSTRAINT FK_ORGANIZERFEEDBACK_ON_ADMIN FOREIGN KEY (admin_id) REFERENCES admin (id);
ALTER TABLE organizer_feedback ADD CONSTRAINT FK_ORGANIZERFEEDBACK_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES organizer (id);

-- Table: review --

DROP TABLE IF EXISTS review CASCADE;
CREATE TABLE review (
  id BIGINT NOT NULL,
   created_at TIMESTAMP WITH TIME ZONE,
   talent_id BIGINT NOT NULL,
   comment TEXT,
   score INTEGER NOT NULL,
   CONSTRAINT pk_review PRIMARY KEY (id)
);

ALTER TABLE review ADD CONSTRAINT FK_REVIEW_ON_TALENT FOREIGN KEY (talent_id) REFERENCES talent (id);

-- Table: category --

DROP TABLE IF EXISTS category CASCADE;
CREATE TABLE category (
  id BIGINT NOT NULL,
   name TEXT NOT NULL,
   parent_id BIGINT,
   CONSTRAINT pk_category PRIMARY KEY (id)
);

ALTER TABLE category ADD CONSTRAINT FK_CATEGORY_ON_PARENT FOREIGN KEY (parent_id) REFERENCES category (id);

-- Table: job_detail --

DROP TABLE IF EXISTS job_detail CASCADE;
CREATE TABLE job_detail (
  id BIGINT NOT NULL,
   category_id BIGINT,
   work_type WORK_TYPE NOT NULL,
   performance_duration BIGINT NOT NULL,
   performance_time TIMESTAMP WITH TIME ZONE,
   note TEXT,
   min DOUBLE PRECISION,
   max DOUBLE PRECISION,
   currency CURRENCY_TYPE NOT NULL,
   extensions JSONB,
   CONSTRAINT pk_jobdetail PRIMARY KEY (id)
);

ALTER TABLE job_detail ADD CONSTRAINT FK_JOBDETAIL_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES category (id);

-- Table: job_offer --

DROP TABLE IF EXISTS job_offer CASCADE;
CREATE TABLE job_offer (
  id BIGINT NOT NULL,
   uid UUID NOT NULL,
   is_active BOOLEAN NOT NULL,
   quantity INTEGER NOT NULL,
   job_detail_id BIGINT NOT NULL,
   organizer_id BIGINT NOT NULL,
   CONSTRAINT pk_joboffer PRIMARY KEY (id)
);

ALTER TABLE job_offer ADD CONSTRAINT FK_JOBOFFER_ON_JOB_DETAIL FOREIGN KEY (job_detail_id) REFERENCES job_detail (id);
ALTER TABLE job_offer ADD CONSTRAINT FK_JOBOFFER_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES organizer (id);

-- Table: booking --

DROP TABLE IF EXISTS booking CASCADE;
CREATE TABLE booking (
  id BIGINT NOT NULL,
   uid UUID NOT NULL,
   created_at TIMESTAMP WITH TIME ZONE,
   status BOOKING_STATUS NOT NULL,
   organizer_id BIGINT NOT NULL,
   talent_id BIGINT NOT NULL,
   job_detail_id BIGINT NOT NULL,
   CONSTRAINT pk_booking PRIMARY KEY (id)
);

ALTER TABLE booking ADD CONSTRAINT FK_BOOKING_ON_JOB_DETAIL FOREIGN KEY (job_detail_id) REFERENCES job_detail (id);
ALTER TABLE booking ADD CONSTRAINT FK_BOOKING_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES organizer (id);
ALTER TABLE booking ADD CONSTRAINT FK_BOOKING_ON_TALENT FOREIGN KEY (talent_id) REFERENCES talent (id);

-- Table: package --

DROP TABLE IF EXISTS package CASCADE;
CREATE TABLE package (
  id BIGINT NOT NULL,
   uid UUID NOT NULL,
   name TEXT NOT NULL,
   talent_id BIGINT NOT NULL,
   job_detail_id BIGINT NOT NULL,
   CONSTRAINT pk_package PRIMARY KEY (id)
);

ALTER TABLE package ADD CONSTRAINT FK_PACKAGE_ON_JOB_DETAIL FOREIGN KEY (job_detail_id) REFERENCES job_detail (id);
ALTER TABLE package ADD CONSTRAINT FK_PACKAGE_ON_TALENT FOREIGN KEY (talent_id) REFERENCES talent (id);

-- Table: package_order --

DROP TABLE IF EXISTS package_order CASCADE;
CREATE TABLE package_order (
  order_id BIGINT NOT NULL,
   package_id BIGINT NOT NULL,
   CONSTRAINT pk_package_order PRIMARY KEY (order_id, package_id)
);
ALTER TABLE package_order ADD CONSTRAINT uc_package_order_order UNIQUE (order_id);
ALTER TABLE package_order ADD CONSTRAINT fk_pacord_on_booking FOREIGN KEY (order_id) REFERENCES booking (id);
ALTER TABLE package_order ADD CONSTRAINT fk_pacord_on_package FOREIGN KEY (package_id) REFERENCES package (id);

-- Table: event --

DROP TABLE IF EXISTS event CASCADE;
CREATE TABLE event (
  id BIGINT NOT NULL,
   uid UUID NOT NULL,
   is_active BOOLEAN NOT NULL,
   occurrence_address TEXT NOT NULL,
   occurrence_time TIMESTAMP WITH TIME ZONE,
   organizer_id BIGINT NOT NULL,
   CONSTRAINT pk_event PRIMARY KEY (id)
);

ALTER TABLE event ADD CONSTRAINT FK_EVENT_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES organizer (id);

-- Table: talent_advertisement --

DROP TABLE IF EXISTS talent_advertisement CASCADE;
CREATE TABLE talent_advertisement (
  id BIGINT NOT NULL,
   expired_time TIMESTAMP WITH TIME ZONE,
   priority INTEGER,
   uid UUID NOT NULL,
   talent_id BIGINT NOT NULL,
   CONSTRAINT pk_talentadvertisement PRIMARY KEY (id)
);

ALTER TABLE talent_advertisement ADD CONSTRAINT FK_TALENTADVERTISEMENT_ON_TALENT FOREIGN KEY (talent_id) REFERENCES talent (id);

-- Table: event_advertisement --

DROP TABLE IF EXISTS event_advertisement CASCADE;
CREATE TABLE event_advertisement (
  id BIGINT NOT NULL,
   expired_time TIMESTAMP WITH TIME ZONE,
   priority INTEGER,
   uid UUID NOT NULL,
   event_id BIGINT NOT NULL,
   CONSTRAINT pk_eventadvertisement PRIMARY KEY (id)
);

ALTER TABLE event_advertisement ADD CONSTRAINT FK_EVENTADVERTISEMENT_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);

-- Table: event_open_position --

DROP TABLE IF EXISTS event_open_position CASCADE;
CREATE TABLE event_open_position (
  id BIGINT NOT NULL,
   uid UUID NOT NULL,
   event_id BIGINT NOT NULL,
   job_offer_id BIGINT NOT NULL,
   CONSTRAINT pk_eventopenposition PRIMARY KEY (id)
);

ALTER TABLE event_open_position ADD CONSTRAINT FK_EVENTOPENPOSITION_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);
ALTER TABLE event_open_position ADD CONSTRAINT FK_EVENTOPENPOSITION_ON_JOB_OFFER FOREIGN KEY (job_offer_id) REFERENCES job_offer (id);

-- Table: open_position_applicant --

DROP TABLE IF EXISTS open_position_applicant CASCADE;
CREATE TABLE open_position_applicant (
  applicant_id BIGINT NOT NULL,
   open_position_id BIGINT NOT NULL,
   CONSTRAINT pk_open_position_applicant PRIMARY KEY (applicant_id, open_position_id)
);

ALTER TABLE open_position_applicant ADD CONSTRAINT uc_open_position_applicant_applicant UNIQUE (applicant_id);
ALTER TABLE open_position_applicant ADD CONSTRAINT fk_opeposapp_on_booking FOREIGN KEY (applicant_id) REFERENCES booking (id);
ALTER TABLE open_position_applicant ADD CONSTRAINT fk_opeposapp_on_event_open_position FOREIGN KEY (open_position_id) REFERENCES event_open_position (id);

-- Table: shopping_cart --

DROP TABLE IF EXISTS shopping_cart CASCADE;
CREATE TABLE shopping_cart (
  id BIGINT NOT NULL,
   cart_item TEXT NOT NULL,
   shoppable_id BIGINT NOT NULL
);

ALTER TABLE shopping_cart ADD CONSTRAINT fk_shopping_cart_organizer FOREIGN KEY (id) REFERENCES organizer (id);