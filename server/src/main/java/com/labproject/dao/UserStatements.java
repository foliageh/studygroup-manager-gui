package com.labproject.dao;

public class UserStatements {
    public static String CREATE_USERS_TABLE = "create table if not exists users (id bigserial primary key, username varchar(255) not null, password varchar(255) not null)";
    public static String SELECT_USER_BY_ID = "select * from users where id = ?";
    public static String SELECT_USER_BY_USERNAME = "select * from users where username = ?";
    public static String INSERT_INTO_USERS = "insert into users (username, password) values (?, ?)";
}
