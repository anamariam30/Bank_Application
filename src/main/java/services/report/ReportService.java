package services.report;

import model.Activity;
import model.User;
import model.validation.Notification;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {

    Notification<Boolean> generateReports(String from, String until);
}
