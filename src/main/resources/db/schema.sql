drop table IF exists users;
create table users(
    username varchar(20) primary key,
    password varchar(50),
    status bigint(1) default 1
) default charset=utf8;

drop table IF exists permission;
create table permission (
    id bigint(20) not null auto_increment primary key,
    url varchar(256) default null,
    name varchar(64) default null
) default charset=utf8;

drop table if exists role;
create table role(
    id bigint(32) not null auto_increment primary key,
    name varchar(32) default null,
    type varchar(10) default null
) default charset=utf8;

drop table if exists role_permission;
create table role_permission(
    rid bigint(20) default null,
    pid bigint(20) default null
) default charset=utf8;

drop table if exists user_role;
create table user_role(
    uid bigint(20) default null,
    rid bigint(20) default null
) default charset=utf8;