CREATE TABLE IF NOT EXISTS UUIDUUIDGENENTITY (ID VARCHAR(255) NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE IF NOT EXISTS UUIDIDCLASSENTITY (UUID_ID VARCHAR(255) NOT NULL, L_ID BIGINT NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (UUID_ID, L_ID));
CREATE TABLE IF NOT EXISTS UUIDAUTOGENENTITY (ID VARCHAR(255) NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE IF NOT EXISTS UUIDENTITY (ID VARCHAR(255) NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE IF NOT EXISTS UUIDEMBEDDABLEIDENTITY (STRDATA VARCHAR(255), ID VARCHAR(255) NOT NULL, PRIMARY KEY (ID));

CREATE TABLE IF NOT EXISTS XMLUUIDUUIDGENENTITY (ID VARCHAR(255) NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE IF NOT EXISTS XMLUUIDIDCLASSENTITY (UUID_ID VARCHAR(255) NOT NULL, L_ID BIGINT NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (UUID_ID, L_ID));
CREATE TABLE IF NOT EXISTS XMLUUIDAUTOGENENTITY (ID VARCHAR(255) NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE IF NOT EXISTS XMLUUIDENTITY (ID VARCHAR(255) NOT NULL, STRDATA VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE IF NOT EXISTS XMLUUIDEMBEDDABLEIDENTITY (STRDATA VARCHAR(255), EID VARCHAR(255) NOT NULL, PRIMARY KEY (EID));


CREATE TABLE IF NOT EXISTS QUERYDATETIMEENTITY (ID INTEGER NOT NULL, LOCALDATEDATA DATE, LOCALDATETIMEDATA TIMESTAMP, LOCALTIMEDATA TIME, PRIMARY KEY (ID));
CREATE TABLE IF NOT EXISTS QUERYENTITY (ID INTEGER NOT NULL, DOUBLEVAL FLOAT, FLOATVAL FLOAT, INTVAL INTEGER, LONGVAL BIGINT, STRVAL VARCHAR(255), PRIMARY KEY (ID));

