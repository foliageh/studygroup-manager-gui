package com.labproject.dao;

import com.labproject.server.Server;
import com.labproject.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.labproject.models.*;

import static com.labproject.dao.StudyGroupStatements.*;


/** Класс, предоставляющий доступ к объектам коллекции в базе данных. */
public class StudyGroupDao {
    private final Connection connection = DatabaseConnection.getInstance();
    private final UserDao userDao = new UserDao();

    public StudyGroupDao() {
        createTables();
    }

    void createTables() {
        try {
            var statement = connection.prepareStatement(CREATE_HAIR_COLOR_TYPE);
            statement.executeUpdate();
            statement = connection.prepareStatement(CREATE_FORM_OF_EDUCATION_TYPE);
            statement.executeUpdate();
            statement = connection.prepareStatement(CREATE_SEMESTER_TYPE);
            statement.executeUpdate();
            statement = connection.prepareStatement(CREATE_STUDY_GROUP_TABLE);
            statement.executeUpdate();
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while creating tables: " + e.getMessage());
        }
    }

    public List<StudyGroup> findAll() {
        try (var statement = connection.prepareStatement(SELECT_ALL_GROUPS)) {
            var resultSet = statement.executeQuery();
            List<StudyGroup> studyGroups = new ArrayList<>();
            while (resultSet.next()) {
                var studyGroup = new StudyGroup();
                studyGroup.setId(resultSet.getLong("id"));
                studyGroup.setName(resultSet.getString("name"));
                studyGroup.setCoordinates(new Coordinates(resultSet.getInt("coord_x"), resultSet.getDouble("coord_y")));
                studyGroup.setCreationDate(resultSet.getDate("creation_date").toLocalDate());
                studyGroup.setStudentsCount(resultSet.getLong("students_count"));
                if (resultSet.wasNull()) studyGroup.setStudentsCount(null);
                studyGroup.setFormOfEducation(FormOfEducation.valueOf(resultSet.getString("form_of_education")));
                studyGroup.setSemester(Semester.valueOf(resultSet.getString("semester")));
                var admin = new Person();
                admin.setName(resultSet.getString("admin_name"));
                try { admin.setBirthday(resultSet.getDate("admin_birthday").toLocalDate()); }
                catch (Exception ignored) {}
                if (resultSet.wasNull()) admin.setBirthday(null);
                admin.setHairColor(Color.valueOf(resultSet.getString("admin_hair_color")));
                var location = new Location();
                location.setX(resultSet.getDouble("admin_location_x"));
                location.setY(resultSet.getDouble("admin_location_y"));
                location.setZ(resultSet.getLong("admin_location_z"));
                location.setName(resultSet.getString("admin_location_name"));
                if (resultSet.wasNull()) location.setName(null);
                admin.setLocation(location);
                studyGroup.setGroupAdmin(admin);
                studyGroup.setCreator(userDao.findById(resultSet.getLong("user_id")));
                studyGroups.add(studyGroup);
            }
            return studyGroups;
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while finding all study groups: " + e.getMessage());
            return null;
        }
    }

    /** Добавляет в базу данных объект и присваивает ему id. */
    public void create(StudyGroup studyGroup) {
        studyGroup.setCreationDate(LocalDate.now());
        try (var statement = connection.prepareStatement(INSERT_INTO_GROUPS, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, studyGroup.getName());
            statement.setInt(2, studyGroup.getCoordinates().getX());
            statement.setDouble(3, studyGroup.getCoordinates().getY());
            if (studyGroup.getStudentsCount() == null) statement.setNull(4, Types.BIGINT);
            else statement.setLong(4, studyGroup.getStudentsCount());
            statement.setObject(5, studyGroup.getFormOfEducation().toString(), Types.OTHER);
            statement.setObject(6, studyGroup.getSemester().toString(), Types.OTHER);
            statement.setString(7, studyGroup.getGroupAdmin().getName());
            if (studyGroup.getGroupAdmin().getBirthday() == null) statement.setNull(8, Types.DATE);
            else statement.setDate(8, Date.valueOf(studyGroup.getGroupAdmin().getBirthday()));
            statement.setObject(9, studyGroup.getGroupAdmin().getHairColor().toString(), Types.OTHER);
            statement.setDouble(10, studyGroup.getGroupAdmin().getLocation().getX());
            statement.setDouble(11, studyGroup.getGroupAdmin().getLocation().getY());
            statement.setLong(12, studyGroup.getGroupAdmin().getLocation().getZ());
            if (studyGroup.getGroupAdmin().getLocation().getName() == null || studyGroup.getGroupAdmin().getLocation().getName().isBlank()) statement.setNull(13, Types.VARCHAR);
            else statement.setString(13, studyGroup.getGroupAdmin().getLocation().getName());
            statement.setLong(14, studyGroup.getCreator().getId());
            statement.setDate(15, Date.valueOf(studyGroup.getCreationDate()));
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            studyGroup.setId(generatedKeys.getLong(1));
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while creating study group: " + e.getMessage());
        }
    }

    /** Обновляет в базе данных объект по его id. */
    public void update(StudyGroup studyGroup) {
        try (var statement = connection.prepareStatement(UPDATE_GROUP)) {
            statement.setString(1, studyGroup.getName());
            statement.setInt(2, studyGroup.getCoordinates().getX());
            statement.setDouble(3, studyGroup.getCoordinates().getY());
            if (studyGroup.getStudentsCount() == null) statement.setNull(4, Types.BIGINT);
            else statement.setLong(4, studyGroup.getStudentsCount());
            statement.setObject(5, studyGroup.getFormOfEducation().toString(), Types.OTHER);
            statement.setObject(6, studyGroup.getSemester().toString(), Types.OTHER);
            statement.setString(7, studyGroup.getGroupAdmin().getName());
            if (studyGroup.getGroupAdmin().getBirthday() == null) statement.setNull(8, Types.DATE);
            else statement.setDate(8, Date.valueOf(studyGroup.getGroupAdmin().getBirthday()));
            statement.setObject(9, studyGroup.getGroupAdmin().getHairColor().toString(), Types.OTHER);
            statement.setDouble(10, studyGroup.getGroupAdmin().getLocation().getX());
            statement.setDouble(11, studyGroup.getGroupAdmin().getLocation().getY());
            statement.setLong(12, studyGroup.getGroupAdmin().getLocation().getZ());
            if (studyGroup.getGroupAdmin().getLocation().getName() == null) statement.setNull(13, Types.VARCHAR);
            else statement.setString(13, studyGroup.getGroupAdmin().getLocation().getName());
            statement.setLong(14, studyGroup.getCreator().getId());
            statement.setLong(15, studyGroup.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while updating study group: " + e.getMessage());
        }
    }

    /** Удаляет объект по id, соответствующему id переданного объекта. */
    public void remove(StudyGroup studyGroup) {
        try (var statement = connection.prepareStatement(DELETE_GROUP)) {
            statement.setLong(1, studyGroup.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            Server.logger.fatal("Something went wrong while removing study group: " + e.getMessage());
        }
    }
}
