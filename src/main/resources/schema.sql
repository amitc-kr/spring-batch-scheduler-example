
drop table Batch_JOB_EXECUTION_CONTEXT;
drop table Batch_JOB_EXECUTION_PARAMS;
drop table Batch_JOB_EXECUTION_SEQ;
drop table Batch_JOB_SEQ;
drop table Batch_STEP_EXECUTION_CONTEXT;
drop table Batch_STEP_EXECUTION_SEQ;
drop table Batch_STEP_EXECUTION;
drop table Batch_JOB_EXECUTION;
drop table Batch_JOB_INSTANCE;
drop table account;
drop table person;

CREATE TABLE person (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  first_name varchar(45) DEFAULT NULL,
  last_name varchar(45) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE account (
  accountId bigint(20) NOT NULL,
  firstName varchar(45) DEFAULT NULL,
  lastName varchar(45) DEFAULT NULL,
  PRIMARY KEY (accountId)
);

INSERT INTO account
(accountId,
firstName,
lastName)
VALUES
(1,'first name','last name');