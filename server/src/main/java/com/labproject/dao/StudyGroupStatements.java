package com.labproject.dao;

public class StudyGroupStatements {
    public static String CREATE_HAIR_COLOR_TYPE = "DO $$ BEGIN create type hair_color as enum ('RED', 'BLACK', 'ORANGE'); EXCEPTION WHEN duplicate_object THEN null; END $$";
    public static String CREATE_FORM_OF_EDUCATION_TYPE = "DO $$ BEGIN create type form_of_education as enum ('DISTANCE_EDUCATION', 'FULL_TIME_EDUCATION', 'EVENING_CLASSES'); EXCEPTION WHEN duplicate_object THEN null; END $$";
    public static String CREATE_SEMESTER_TYPE = "DO $$ BEGIN create type semester as enum ('FIRST', 'THIRD', 'FIFTH', 'SIXTH', 'SEVENTH'); EXCEPTION WHEN duplicate_object THEN null; END $$" ;
    public static String CREATE_STUDY_GROUP_TABLE = "create table if not exists study_group (id bigserial primary key, name varchar(255) not null, coord_x integer not null, coord_y double precision not null, creation_date date not null, students_count bigint, form_of_education form_of_education not null, semester semester not null, admin_name varchar(255) not null, admin_birthday date, admin_hair_color hair_color not null, admin_location_x double precision not null, admin_location_y double precision not null, admin_location_z bigint not null, admin_location_name varchar(255), user_id bigint not null references users(id) on delete cascade)";
    public static String SELECT_ALL_GROUPS = "select * from study_group";
    public static String INSERT_INTO_GROUPS = "insert into study_group (name, coord_x, coord_y, students_count, form_of_education, semester, admin_name, admin_birthday, admin_hair_color, admin_location_x, admin_location_y, admin_location_z, admin_location_name, user_id, creation_date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static String UPDATE_GROUP = "update study_group set name = ?, coord_x = ?, coord_y = ?, students_count = ?, form_of_education = ?, semester = ?, admin_name = ?, admin_birthday = ?, admin_hair_color = ?, admin_location_x = ?, admin_location_y = ?, admin_location_z = ?, admin_location_name = ?, user_id = ? where id = ?";
    public static String DELETE_GROUP = "delete from study_group where id = ?";
}
