CREATE TABLE email
(
    email_id        bigserial primary key
    , from_addr     varchar(65) not null
    , to_addr       varchar(65) not null
    , subject       varchar(128) not null
    , name          varchar(32)
    , sex           varchar(1)
    , type          varchar(1)
    , naiyo         text
);

CREATE TABLE email_item
(
    email_item_id   bigserial primary key
    , email_id      bigint not null references EMAIL(email_id) on delete cascade
    , item          varchar(3)
);
