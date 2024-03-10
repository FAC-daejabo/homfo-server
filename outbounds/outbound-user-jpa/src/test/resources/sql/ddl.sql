CREATE TABLE IF NOT EXISTS USERS
(
    user_id           INT                 NOT NULL,
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
    deleted_at   timestamp,
    PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS USER_REFRESH_TOKEN
(
    user_id         INT not null auto_increment,
    token      varchar(60),
    updated_at timestamp,
    primary key (user_id)
);

CREATE TABLE IF NOT EXISTS USER_MARKETING_AGREEMENT
(
    user_marketing_agreement_id         int auto_increment,
    user_id    int       not null,
    code       char(18)  not null,
    agreement  bool      not null,
    created_at timestamp not null,
    updated_at timestamp,
    primary key (user_marketing_agreement_id)
);

CREATE TABLE IF NOT EXISTS EMPLOYEE
(
    employee_id           INT                 NOT NULL,
    account      varchar(15) unique,
    password     varchar(60),
    nickname     varchar(15) unique,
    phone_number varchar(15) unique,
    birthday     date,
    gender       char(1),
    job          varchar(15),
    role         varchar(15),
    status       char(1) default 'U' not null,
    created_at   timestamp           not null,
    updated_at   timestamp,
    deleted_at   timestamp,
    PRIMARY KEY (employee_id)
    );

CREATE TABLE IF NOT EXISTS EMPLOYEE_REFRESH_TOKEN
(
    employee_id         INT not null auto_increment,
    token      varchar(60),
    updated_at timestamp,
    primary key (employee_id)
    );

CREATE TABLE IF NOT EXISTS EMPLOYEE_MARKETING_AGREEMENT
(
    employee_marketing_agreement_id         int auto_increment,
    employee_id    int       not null,
    code       char(18)  not null,
    agreement  bool      not null,
    created_at timestamp not null,
    updated_at timestamp,
    primary key (employee_marketing_agreement_id)
    );