create database todo;

create table task (
                      id serial primary key,
                      description text,
                      created timestamp,
                      done boolean,
                      user_id int not null references users(id)
);

create table users (
                       id serial primary key,
                       name varchar not null ,
                       email varchar not null UNIQUE ,
                       password varchar not null
);