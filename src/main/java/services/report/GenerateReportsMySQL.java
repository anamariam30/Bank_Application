package services.report;

import model.Activity;
import model.User;
import model.validation.Notification;
import services.activity.ActivityService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GenerateReportsMySQL implements ReportService {
    private final ActivityService activityService;

    public GenerateReportsMySQL(ActivityService activityService) {
        this.activityService = activityService;
    }

    @Override
    public Notification<Boolean> generateReports(String from, String until) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
        Notification<Boolean> notification = new Notification<>();

        LocalDate localDate1;
        LocalDate localDate2;
        try {
            localDate1 = LocalDate.parse(from, formatter);
            localDate2 = LocalDate.parse(until, formatter);
        } catch (Exception e) {
            notification.addError("Invalid dates!");
            notification.setResult(false);
            return notification;
        }

        List<Activity> activities = activityService.getActivities(localDate1, localDate2);
        if (activities == null) {
            notification.addError("Invalid dates!");
            notification.setResult(false);
            return notification;
        }

        try {
            FileWriter myWriter = new FileWriter("Report.txt");

            for (Activity activity : activities) {
                myWriter.write(String.valueOf(activity));
            }

            myWriter.close();
        } catch (IOException e) {
            notification.addError(" An error occurred!");
            notification.setResult(false);
        }
        return notification;
    }
}
