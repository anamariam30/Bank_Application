package controller;

import model.dataTransferObject.UserDto;
import model.validation.Notification;
import services.report.GenerateReportsMySQL;
import services.user.management.AdminManagerService;
import view.AdministratorView;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdministratorController {
    private final AdministratorView administratorView;
    private final LoginView loginView;
    private final AdminManagerService adminManagerService;
    private final GenerateReportsMySQL generateReportsMySQL;

    public AdministratorController(AdministratorView administratorView, LoginView loginView, AdminManagerService adminManagerService, GenerateReportsMySQL generateReportsMySQL) {
        this.administratorView = administratorView;
        this.loginView = loginView;
        this.adminManagerService = adminManagerService;
        this.generateReportsMySQL = generateReportsMySQL;
        administratorView.setAddButtonActionListener(new AddButtonListener());
        administratorView.setDeleteButtonActionListener(new DeleteButtonListener());
        administratorView.setUpdateButtonActionListener(new UpdateButtonListener());
        administratorView.setViewAllButtonActionListener(new ViewAllButtonListener());
        administratorView.setGenerateReportsButtonActionListener(new GenerateReportsButtonListener());
        administratorView.setBackButtonActionListener(new BackButtonListener());
    }

    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            UserDto user=administratorView.getUserDto();

            Notification<Boolean> registerNotification = adminManagerService.createEmployee(user);

            if (registerNotification.hasErrors()) {
                JOptionPane.showMessageDialog(administratorView.getContentPane(), registerNotification.getFormattedErrors());
            } else {
                boolean result = registerNotification.getResult();
                if (result) {
                    JOptionPane.showMessageDialog(administratorView.getContentPane(), "Employee add successful!");
                } else
                    JOptionPane.showMessageDialog(administratorView.getContentPane(), "Duplicate user!");

            }
        }
    }

    private class DeleteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            UserDto user=administratorView.getUserDto();
            Notification<Boolean> notification = adminManagerService.deleteEmployee(user.getUsername());
            if (!notification.hasErrors()) {
                JOptionPane.showMessageDialog(administratorView.getContentPane(), "Employee deleted successful!");
            } else {
                JOptionPane.showMessageDialog(administratorView.getContentPane(), notification.getFormattedErrors());

            }
        }
    }

    private class UpdateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            UserDto oldUser=administratorView.getUserDto();
            UserDto newUser=administratorView.getUpdateUserDto();
            Notification<Boolean> notification = adminManagerService.updateEmployee(oldUser,newUser);

            if (!notification.hasErrors()) {
                JOptionPane.showMessageDialog(administratorView.getContentPane(), "Employee updated successful!");
            } else {
                JOptionPane.showMessageDialog(administratorView.getContentPane(), notification.getFormattedErrors());

            }
        }
    }

    private class ViewAllButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
             administratorView.displayList("Employees",adminManagerService.getAllEmployees().toString());

        }
    }

    private class GenerateReportsButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {


            String date1 = administratorView.getFrom();
            String date2 = administratorView.getUntil();


            Notification<Boolean> notification = generateReportsMySQL.generateReports(date1, date2);
            if (notification.hasErrors()) {
                JOptionPane.showMessageDialog(administratorView.getContentPane(), notification.getFormattedErrors());

            } else {
                JOptionPane.showMessageDialog(administratorView.getContentPane(), "Done reporting!");

            }

        }
    }


    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            administratorView.setVisible(false);
            loginView.setVisible(true);

        }
    }
}
