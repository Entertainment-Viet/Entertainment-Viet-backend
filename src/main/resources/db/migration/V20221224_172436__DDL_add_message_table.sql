-- Table: account --
DROP TABLE IF EXISTS conversation CASCADE;
CREATE TABLE conversation (
    id BIGINT NOT NULL,
    uid UUID NOT NULL,
    name TEXT,
    archived BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_conversation PRIMARY KEY (id)
);


-- Table: account --
DROP TABLE IF EXISTS conversation_participant CASCADE;
CREATE TABLE conversation_participant (
   conversation_id BIGINT NOT NULL,
   account_id BIGINT NOT NULL,
   CONSTRAINT pk_conversation_participant PRIMARY KEY (conversation_id, account_id)
);

ALTER TABLE conversation_participant ADD CONSTRAINT fk_converpart_on_conversation FOREIGN KEY (conversation_id) REFERENCES conversation (id);
ALTER TABLE conversation_participant ADD CONSTRAINT fk_converpart_on_account FOREIGN KEY (account_id) REFERENCES account (id);

-- Table: account --
DROP TABLE IF EXISTS message CASCADE;
CREATE TABLE message (
   id BIGINT NOT NULL,
   content_input_lang SUPPORT_LANGUAGE,
   content_raw_input TEXT,
   content_input_translation JSONB,
   sent_at TIMESTAMP WITH TIME ZONE,
   from_account_id BIGINT NOT NULL,
   conversation_id BIGINT NOT NULL,
   CONSTRAINT pk_message PRIMARY KEY (id)
);

ALTER TABLE message ADD CONSTRAINT fk_message_on_account FOREIGN KEY (from_account_id) REFERENCES account (id);
ALTER TABLE message ADD CONSTRAINT fk_message_on_conversation FOREIGN KEY (conversation_id) REFERENCES conversation (id);