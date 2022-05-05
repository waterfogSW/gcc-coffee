create table products
(
    product_id          bigint auto_increment primary key,
    name        varchar(50)                              not null,
    category    varchar(50)                              not null,
    price       bigint                                   not null,
    description text                                     null,
    created_at  datetime(6) default CURRENT_TIMESTAMP(6) null,
    updated_at  datetime(6) default CURRENT_TIMESTAMP(6) null
);

create table orders
(
    order_id           bigint auto_increment primary key,
    email        varchar(50)  not null,
    address      varchar(200) not null,
    postcode     varchar(200) not null,
    order_status varchar(50)  not null,
    created_at    datetime(6)  not null,
    updated_at    datetime(6) default null
);

create table order_products
(
    seq        bigint      not null primary key auto_increment,
    order_id   bigint      not null,
    product_id bigint      not null,
    category   varchar(50) not null,
    price      bigint      not null,
    quantity   int         not null,
    created_at datetime(6) not null,
    updated_at datetime(6) default null,
    INDEX (order_id),
    constraint fk_order_products_to_order foreign key (order_id) references orders (order_id) on delete cascade,
    constraint fk_order_products_product foreign key (product_id) references products (product_id)
);