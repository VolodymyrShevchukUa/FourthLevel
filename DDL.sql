-- auto-generated definition
create table category
(
    category_id   serial
        primary key,
    category_name text
);

alter table category
    owner to awsdata;

-------------

-- auto-generated definition
create table goods
(
    goods_id    serial
        primary key,
    category_id integer          not null
        references category
            on update cascade on delete cascade,
    goods_name  text             not null,
    goods_price double precision not null
);

    -- auto-generated definition
    create table goods_list
    (
        market_id integer
            references market
                on update cascade on delete cascade,
        goods_id  integer
            references goods
                on update cascade on delete cascade
    );

    alter table goods_list
        owner to awsdata;

-- auto-generated definition
create table market
(
    market_id    serial
        primary key,
    market_name  text not null,
    market_adres text not null
);

alter table market
    owner to awsdata;



