package model.builder;

import model.*;

import java.time.LocalDate;

public class ActivityBuilder {
    private final Activity activity;

    public ActivityBuilder() {
        this.activity = new Activity();
    }

    public ActivityBuilder setUser(User user) {
        activity.setUser(user);
        return this;
    }

    public ActivityBuilder setAction(String action) {
        activity.setAction(action);
        return this;
    }

    public ActivityBuilder setTime(LocalDate localDate) {
        activity.setLocalDate(localDate);
        return this;
    }

    public Activity build() {
        return activity;
    }


}
