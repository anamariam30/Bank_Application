package model;

import java.time.LocalDate;

public class Activity {
    private User user;
    private String action;
    private LocalDate localDate;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public String toString() {
        return "Date= " + localDate +
                "   employee= " + user.getUsername() +
                "   activity= " + action + "\n";
    }
}
