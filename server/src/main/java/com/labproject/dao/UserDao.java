package com.labproject.dao;

import com.labproject.server.Server;
import com.labproject.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.labproject.models.*;
import com.labproject.utils.PasswordEncoder;

import static com.labproject.dao.UserStatements.*;


/** Класс, предоставляющий доступ к объектам пользователей в базе данных. */
public class UserDao {
    private final Connection connection = DatabaseConnection.getInstance();

    public UserDao() {
        createTables();
    }

    void createTables() {
        try {
            var statement = connection.prepareStatement(CREATE_USERS_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while creating tables: " + e.getMessage());
        }
    }

    public User findById(long id) {
        try (var statement = connection.prepareStatement(SELECT_USER_BY_ID)) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while finding user by id: " + e.getMessage());
            return null;
        }
    }

    public void create(User user) {
        try (var statement = connection.prepareStatement(INSERT_INTO_USERS, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, PasswordEncoder.encode(user.getPassword()));
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            user.setId(generatedKeys.getLong(1));
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while creating user: " + e.getMessage());
        }
    }

    public User findByUsername(String username) {
        try (var statement = connection.prepareStatement(SELECT_USER_BY_USERNAME)) {
            statement.setString(1, username);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while finding user by username: " + e.getMessage());
            return null;
        }
    }
}
