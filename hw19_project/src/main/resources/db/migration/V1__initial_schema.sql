create extension if not exists btree_gist;

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence user_seq start with 1 increment by 1;
create sequence reservation_seq start with 1 increment by 1;
create sequence table_seq start with 1 increment by 1;

create table users
(
    id   bigint not null primary key default nextval('user_seq'),
    login varchar(50) not null unique,
    password varchar(255) not null,
    created_at timestamp default now()
);

create table tables
(
    id bigint not null primary key default nextval('table_seq'),
    capacity int not null check (capacity > 0)
);

create table reservations
(
    id bigint not null primary key default nextval('reservation_seq'),
    user_id bigint not null references users(id),
    guest_name  varchar(50) not null,
    guest_phone varchar(64) not null,
    table_id bigint not null references tables(id),
    persons int not null check (persons > 0),
    status text not null default 'CONFIRMED' check (status in ('CONFIRMED','CANCELLED')),
    start_time timestamp not null,
    end_time timestamp not null,
    time_slot tsrange generated always as (tsrange(start_time, end_time, '[)')) stored,
    EXCLUDE USING GIST (table_id WITH =, time_slot WITH &&) WHERE (status = 'CONFIRMED')
);
