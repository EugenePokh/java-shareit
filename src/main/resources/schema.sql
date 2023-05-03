create table if not exists users (
    id BIGSERIAL primary key,
    name varchar not null,
    email varchar not null,
    CONSTRAINT users_unique_email UNIQUE (email)
);

create table if not exists requests (
    id BIGSERIAL primary key,
    description varchar,
    requestor_id BIGINT REFERENCES users (id)
);

create table if not exists items (
    id BIGSERIAL primary key,
    name varchar,
    description varchar,
    available boolean,
    owner_id BIGINT REFERENCES users (id),
    request_id BIGINT REFERENCES requests (id)
);

create table if not exists booking (
    id BIGSERIAL primary key,
    start_time timestamp,
    end_time timestamp,
    item_id BIGINT REFERENCES items (id),
    booker_id BIGINT REFERENCES users (id),
    status varchar
);

create table if not exists comments (
    id BIGSERIAL primary key,
    text varchar,
    item_id BIGINT REFERENCES items (id),
    author_id BIGINT REFERENCES users (id),
    created timestamp
);


