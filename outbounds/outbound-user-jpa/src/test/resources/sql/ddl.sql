CREATE TABLE IF NOT EXISTS USERS
(
    id           INT                 NOT NULL,
    account      varchar(15) unique,
    password     varchar(60),
    nickname     varchar(15) unique,
    phone_number varchar(15) unique,
    birthday     date,
    gender       char(1),
    job          varchar(15),
    status       char(1) default 'U' not null,
    created_at   timestamp           not null,
    updated_at   timestamp,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS USER_REFRESH_TOKEN
(
    id         INT not null auto_increment,
    token      varchar(60),
    updated_at timestamp,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS USER_MARKETING_AGREEMENT
(
    id         int auto_increment,
    user_id    int       not null,
    code       char(18)  not null,
    agreement  bool      not null,
    created_at timestamp not null,
    updated_at timestamp,
    primary key (id)
);