create database todo;

create table task (
                       id serial primary key,
                       description text,
                       created timestamp,
                       done boolean
);