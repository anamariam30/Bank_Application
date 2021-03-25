package repository.activity;


import model.Activity;
import model.builder.ActivityBuilder;
import repository.user.UserRepository;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Tables.ACTIVITY;

public class ActivityRepositoryMySQL implements ActivityRepository {
    private final UserRepository userRepository;
    private final Connection connection;

    public ActivityRepositoryMySQL(UserRepository userRepository, Connection connection) {
        this.userRepository = userRepository;

        this.connection = connection;
    }

    @Override
    public Boolean addActivity(Activity activity) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO `" + ACTIVITY + "` values (null, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setLong(1, activity.getUser().getId());
            insertUserStatement.setString(2, activity.getAction());
            insertUserStatement.setDate(3, java.sql.Date.valueOf(activity.getLocalDate()));
            insertUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {

            return false;
        }
    }

    @Override
    public List<Activity> getActivities(LocalDate startDate, LocalDate endDate) {
        String sql = "Select * from `" + ACTIVITY + "` where `data` >= \'" + startDate + "\' and `data` <=\'" + endDate + "\'";
        List<Activity> activities = new ArrayList<>();


        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);


            while (resultSet.next()) {
                activities.add(getActivityFromResultSet(resultSet));

            }
        } catch (SQLException throwables) {
            return null;
        }

        return activities;
    }

    @Override
    public Boolean removeAll() {
        String sql = "DELETE FROM `" + ACTIVITY + "` where id>=0";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException throwable) {

            return false;
        }


    }

    private Activity getActivityFromResultSet(ResultSet rs) throws SQLException {
        return new ActivityBuilder()
                .setUser(userRepository.getUserById(rs.getLong("user_id")))
                .setAction(rs.getString("action"))
                .setTime(Instant.ofEpochMilli(rs.getDate("data").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                .build();
    }
}
