package repository.user;


import model.User;
import model.builder.UserBuilder;
import model.validation.Notification;
import repository.security.RightsRolesRepository;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {
    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String encodePassword) {
        Notification<User> notification = new Notification<>();
        try {

            Statement statement = connection.createStatement();
            String sql = "Select * from `" + USER + "` where `username` =\'" + username + "\' and `password`= \'" + encodePassword + "\'";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(username)
                        .setPassword(encodePassword)
                        .setRole(rightsRolesRepository.findRoleForUser(resultSet.getLong("id")))
                        .build();
                notification.setResult(user);
            } else {
                notification.addError("Invalid username and/or password");
            }


        } catch (SQLException throwable) {
            notification.addError("The user already exist!");
        }
        return notification;
    }

    @Override
    public Boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRoleToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Boolean delete(User user) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from `" + USER + "` where `id` =\'" + user.getId() + "\'";
            statement.executeUpdate(sql);

            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    @Override
    public User getUserById(long userId) {
        String sql = "Select * from `" + USER + "` WHERE id = " + userId;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                User userFromResultSet = getUserFromResultSet(resultSet);
                userFromResultSet.setRoles(rightsRolesRepository.findRoleForUser(userFromResultSet.getId()));
                return userFromResultSet;
            }

        } catch (SQLException throwable) {
            return null;
        }

        return null;
    }

    @Override
    public User getUserByName(String username) {
        String sql = "Select * from `" + USER + "` where `username` =\'" + username + "\'";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                User userFromResultSet = getUserFromResultSet(resultSet);
                userFromResultSet.setRoles(rightsRolesRepository.findRoleForUser(userFromResultSet.getId()));
                return userFromResultSet;
            }

        } catch (SQLException throwable) {
            return null;
        }

        return null;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "Select * from `" + USER + "`";

        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User userFromResultSet = getUserFromResultSet(resultSet);
                userFromResultSet.setRoles(rightsRolesRepository.findRoleForUser(userFromResultSet.getId()));
                users.add(userFromResultSet);
            }
        } catch (SQLException throwable) {
            return null;
        }

        return users;
    }

    @Override
    public Boolean update(Long userId, String username, String password) {
        String sql = "UPDATE `" + USER + "` SET username=? , password = ? WHERE (id = ?)";
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement(sql);
            insertStatement.setString(1, username);
            insertStatement.setString(2, password);
            insertStatement.setLong(3, userId);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Boolean removeAll() {
        String sql = "DELETE FROM `" + USER + "` where id>=0";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {

            return false;
        }

    }

    private User getUserFromResultSet(ResultSet rs) throws SQLException {
        return new UserBuilder()
                .setId(rs.getLong("id"))
                .setUsername(rs.getString("username"))
                .setPassword(rs.getString("password"))
                .build();
    }
}
