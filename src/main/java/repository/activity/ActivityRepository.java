package repository.activity;

import model.Activity;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository {

    Boolean addActivity(Activity activity);

    List<Activity> getActivities(LocalDate startDate, LocalDate endDate);

    Boolean removeAll();
}
