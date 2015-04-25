CREATE TABLE EMAIL
(
    EMAIL_ID        bigserial primary key
    , FROM_ADDR     varchar(65) not null
    , TO_ADDR       varchar(65) not null
    , SUBJECT       varchar(128) not null
    , NAME          varchar(32)
    , SEX           smallint
    , TYPE          smallint
    , NAIYO         text
);

CREATE TABLE EMAIL_ITEM
(
    EMAIL_ITEM_ID   bigserial primary key
    , EMAIL_ID      bigint not null references EMAIL(EMAIL_ID) on delete cascade
    , ITEM          smallint
);
