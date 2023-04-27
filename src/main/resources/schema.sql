drop table if exists booking;
drop table if exists comments;
drop table if exists items;
drop table if exists requests;
drop table if exists users;

create table users (
id BIGSERIAL primary key,
name varchar,
email varchar,
CONSTRAINT users_unique_email UNIQUE (email)
);

create table requests (
id BIGSERIAL primary key,
description varchar,
requestor_id BIGINT REFERENCES users (id)
);

create table items (
id BIGSERIAL primary key,
name varchar,
description varchar,
available boolean,
owner_id BIGINT REFERENCES users (id),
request_id BIGINT REFERENCES requests (id)
);

create table booking (
id BIGSERIAL primary key,
start_time timestamp,
end_time timestamp,
item_id BIGINT REFERENCES items (id),
booker_id BIGINT REFERENCES users (id),
status varchar
);

create table comments (
id BIGSERIAL primary key,
text varchar,
item_id BIGINT REFERENCES items (id),
author_id BIGINT REFERENCES users (id)
);


