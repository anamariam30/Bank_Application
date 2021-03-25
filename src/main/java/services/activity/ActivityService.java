package services.activity;

import model.Activity;

import java.time.LocalDate;
import java.util.List;

public interface ActivityService {
    Boolean addActivity(Activity activity);

    List<Activity> getActivities(LocalDate From, LocalDate Until);
}
