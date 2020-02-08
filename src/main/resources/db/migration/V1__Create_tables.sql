CREATE TABLE organization (
   id INT NOT NULL,
   url varchar NOT NULL,
   external_id VARCHAR NOT NULL,
   name VARCHAR NOT NULL,
   details VARCHAR,
   shared_tickets bit NOT NULL
);

CREATE TABLE organization_domain_names (
    organization_id int NOT NULL,
    domain_name varchar NOT NULL
);

CREATE TABLE organization_tags (
    organization_id int NOT NULL,
    tag varchar NOT NULL
);

CREATE TABLE user (
    id INT NOT NULL,
    url varchar NOT NULL,
    external_id VARCHAR NOT NULL,
    name VARCHAR,
    alias VARCHAR,
    active bit,
    verified bit,
    shared bit,
    locale VARCHAR,
    timezone VARCHAR,
    email VARCHAR,
    phone VARCHAR,
    signature VARCHAR,
    o_id Int,
    suspended BIT,
    role VARCHAR
);

CREATE TABLE user_tags (
    user_id int NOT NULL,
    tag varchar NOT NULL
);


CREATE TABLE ticket (
    id VARCHAR NOT NULL,
    o_id Int,
    a_id Int,
    s_id Int,
    url varchar NOT NULL,
    external_id VARCHAR NOT NULL,
    type VARCHAR,
    subject VARCHAR,
    description VARCHAR,
    priority VARCHAR,
    status VARCHAR,
    has_incidents bit,
    via VARCHAR
);

CREATE TABLE ticket_tags (
    ticket_id VARCHAR NOT NULL,
    tag varchar NOT NULL
);

