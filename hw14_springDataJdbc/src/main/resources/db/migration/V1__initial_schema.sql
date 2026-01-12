create table client
(
    id          bigserial   not null    primary key,
    name        varchar(50) not null
);
create unique index idx_client_name on client (name);

create table address
(
    client_id   bigint      not null    primary key references client (id),
    street varchar(64)      not null
);

create table phone
(
    id          bigserial   not null    primary key,
    client_id   bigint      not null    references client (id),
    ordering       integer     not null,
    number      varchar(64) not null
);