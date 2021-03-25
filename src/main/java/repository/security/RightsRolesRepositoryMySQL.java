package repository.security;

import model.Right;
import model.Role;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.ADMINISTRATOR;
import static database.Constants.Roles.EMPLOYEE;
import static database.Constants.Tables.*;

public class RightsRolesRepositoryMySQL implements RightsRolesRepository {
    private final Connection connection;

    public RightsRolesRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Role findRoleForUser(long userId) {
        try {
            Statement statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + USER_ROLE + " where `user_id`=\'" + userId + "\'";
            ResultSet userRoleResultSet = statement.executeQuery(fetchRoleSql);
            if (userRoleResultSet.next()) {
                long roleId = userRoleResultSet.getLong("role_id");
                return findRoleById(roleId);
            } else {
                return null;
            }
        } catch (SQLException throwables) {
            return null;

        }

    }


    @Override
    public Role findRoleByTitle(String role) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ROLE + " where `role`=\'" + role + "\'";
            ResultSet roleResultSet = statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            Long roleId = roleResultSet.getLong("id");
            Role role1 = new Role(roleId, role, null);
            role1.setRights(findRightsForRole(role1));
            return role1;
        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public Boolean addRoleToUser(User user, Role role) {
        try {
            PreparedStatement insertUserRoleStatement = connection
                    .prepareStatement("INSERT INTO `user_role` values (null, ?, ?)");
            insertUserRoleStatement.setLong(1, user.getId());
            insertUserRoleStatement.setLong(2, role.getId());
            insertUserRoleStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;

        }

    }

    @Override
    public Boolean updateRoleToUser(User user) {
        Role userRole = user.getRoles();
        Role newRole;
        if (userRole.getRole().equals(EMPLOYEE)) {
            newRole = findRoleByTitle(ADMINISTRATOR);
        } else {
            newRole = findRoleByTitle(EMPLOYEE);
        }
        try {
            PreparedStatement updateUserRoleStatement = connection
                    .prepareStatement("UPDATE `user_role` SET role_id =? WHERE (user_id= ?)");
            updateUserRoleStatement.setLong(1, newRole.getId());
            updateUserRoleStatement.setLong(2, user.getId());
            updateUserRoleStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }

    }


    @Override
    public Boolean addRole(String role) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT INTO " + ROLE + " values (null, ?)");
            insertStatement.setString(1, role);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Boolean addRight(String right) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT INTO `" + RIGHT + "` values (null, ?)");
            insertStatement.setString(1, right);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {

            return false;
        }

    }

    @Override
    public Right findRightByTitle(String right) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRightSql = "Select * from `" + RIGHT + "` where `right`=\'" + right + "\'";
            ResultSet rightResultSet = statement.executeQuery(fetchRightSql);
            rightResultSet.next();
            Long rightId = rightResultSet.getLong("id");
            String rightTitle = rightResultSet.getString("right");
            return new Right(rightId, rightTitle);

        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public List<Right> findRightsForRole(Role role) {
        List<Right> rights = new ArrayList<>();
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRightSql = "SELECT * FROM `right` inner join role_right on role_right.right_id= `right`.id where role_right.role_id=\'" + role.getId() + "\'";
            ResultSet rightResultSet = statement.executeQuery(fetchRightSql);
            while (rightResultSet.next()) {
                Long rightId = rightResultSet.getLong("id");
                String rightTitle = rightResultSet.getString("right");
                rights.add(new Right(rightId, rightTitle));
            }


            return rights;
        } catch (SQLException e) {
            return null;
        }

    }

    @Override
    public Boolean addRoleRight(Long roleId, Long rightId) {
        try {
            PreparedStatement insertStatement = connection
                    .prepareStatement("INSERT INTO " + ROLE_RIGHT + " values (null, ?, ?)");
            insertStatement.setLong(1, roleId);
            insertStatement.setLong(2, rightId);
            insertStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;

        }

    }

    @Override
    public Boolean removeAllRoles() {
        String sql = "DELETE FROM `role` where id>=0";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    @Override
    public Boolean removeAllRights() {
        String sql = "DELETE FROM `right` where id>=0";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }

    @Override
    public Boolean removeAll() {
        String sql = "DELETE FROM `role_right` where id>=0";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwables) {
            return false;
        }
    }


    private Role findRoleById(long roleId) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String fetchRoleSql = "Select * from " + ROLE + " where `id`=\'" + roleId + "\'";
            ResultSet roleResultSet = statement.executeQuery(fetchRoleSql);
            roleResultSet.next();
            String roleTitle = roleResultSet.getString("role");
            Role role = new Role(roleId, roleTitle, null);
            role.setRights(findRightsForRole(role));
            return role;
        } catch (SQLException e) {
            return null;
        }


    }


}
