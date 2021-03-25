package services.activity;

import model.Activity;
import repository.activity.ActivityRepository;

import java.time.LocalDate;
import java.util.List;

public class ActivityServiceMySQL implements ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityServiceMySQL(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public Boolean addActivity(Activity activity) {
        activity.setLocalDate(LocalDate.now());
        return activityRepository.addActivity(activity);

    }

    @Override
    public List<Activity> getActivities(LocalDate From, LocalDate Until) {
        return activityRepository.getActivities(From, Until);
    }
}
