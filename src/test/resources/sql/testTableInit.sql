create table products
(
    id          int auto_increment
        primary key,
    name        varchar(20)                              not null,
    category    varchar(20)                              not null,
    price       int                                      not null,
    description text                                     null,
    created_at  datetime(6) default CURRENT_TIMESTAMP(6) null,
    updated_at  datetime(6) default CURRENT_TIMESTAMP(6) null
);