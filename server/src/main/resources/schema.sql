create table if not exists users (
    id BIGSERIAL primary key,
    name varchar not null,
    email varchar not null,
    CONSTRAINT users_unique_email UNIQUE (email)
);

create table if not exists requests (
    id BIGSERIAL primary key,
    description varchar not null,
    author_id BIGINT REFERENCES users (id),
    created timestamp not null
);

create table if not exists items (
    id BIGSERIAL primary key,
    name varchar not null,
    description varchar not null,
    available boolean not null,
    owner_id BIGINT REFERENCES users (id),
    request_id BIGINT REFERENCES requests (id)
);

create table if not exists booking (
    id BIGSERIAL primary key,
    start_time timestamp not null,
    end_time timestamp not null,
    item_id BIGINT REFERENCES items (id),
    booker_id BIGINT REFERENCES users (id),
    status varchar not null
);

create table if not exists comments (
    id BIGSERIAL primary key,
    text varchar not null,
    item_id BIGINT REFERENCES items (id),
    author_id BIGINT REFERENCES users (id),
    created timestamp not null
);






