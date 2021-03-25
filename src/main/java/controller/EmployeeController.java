package controller;

import model.Account;
import model.Client;
import model.builder.AccountBuilder;
import model.builder.ActivityBuilder;
import model.dataTransferObject.AccountDto;
import model.dataTransferObject.ClientDto;
import model.dataTransferObject.PayBillDto;
import model.dataTransferObject.TransferMoneyDto;
import model.validation.AccountValidation;
import model.validation.Notification;
import services.account.AccountService;
import services.activity.ActivityService;
import services.client.ClientService;
import view.EmployeeView;
import view.LoginView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static database.Constants.Rights.*;

public class EmployeeController {
    private final EmployeeView employeeView;
    private final LoginView loginView;
    private final ClientService clientService;
    private final AccountService accountService;
    private final ActivityService activityService;

    public EmployeeController(EmployeeView employeeView, LoginView loginView, ClientService clientService, AccountService accountService, ActivityService activityService) {
        this.employeeView = employeeView;
        this.loginView = loginView;
        this.clientService = clientService;
        this.accountService = accountService;
        this.activityService = activityService;
        employeeView.setAddButtonActionListener(new AddButtonListener());
        employeeView.setUpdateButtonActionListener(new UpdateButtonListener());
        employeeView.setViewButtonActionListener(new ViewButtonListener());
        employeeView.setViewAllButtonActionListener(new ViewAllButtonListener());
        employeeView.setPayBillButtonActionListener(new PayBillButtonListener());
        employeeView.setTransferMoneyButtonActionListener(new TransferMoneyButtonListener());
        employeeView.setAddAccountButtonActionListener(new AddAccountButtonListener());
        employeeView.setViewAccountButtonActionListener(new ViewAccountButtonListener());
        employeeView.setUpdateAccountButtonActionListener(new UpdateAccountButtonListener());
        employeeView.setDeleteAccountButtonActionListener(new DeleteAccountButtonListener());
        employeeView.setBackButtonActionListener(new BackButtonListener());
    }


    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            ClientDto clientDto = employeeView.getClientDto();

            Notification<Boolean> notification = clientService.addClient(clientDto);
            if (notification.hasErrors()) {

                JOptionPane.showMessageDialog(employeeView.getContentPane(), notification.getFormattedErrors());

            } else {
                if (notification.getResult() == false) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Duplicate CNP!");

                } else {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Client successfully added!");
                    addActivities(ADD_CLIENT);
                }
            }


        }
    }

    private class ViewButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ClientDto clientDto = employeeView.getClientDto();

            Notification<Client> notification = clientService.getClientByCNP(clientDto.getCNP());
            Client client = notification.getResult();


            if (notification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), notification.getFormattedErrors());

            } else {
                employeeView.displayList("Client", client.toString());
                addActivities(VIEW_CLIENT);
            }

        }
    }

    private class UpdateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ClientDto oldClient = employeeView.getClientDto();
            ClientDto newClient = employeeView.getUpdateClientDto();

            Notification<Boolean> notification = clientService.updateClient(oldClient, newClient);
            if (!notification.hasErrors()) {

                JOptionPane.showMessageDialog(employeeView.getContentPane(), "Client updated successful!");
                addActivities(UPDATE_CLIENT);


            } else {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), notification.getFormattedErrors());
            }

        }
    }

    private class ViewAllButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            employeeView.displayList("Clients", clientService.getAllClients().toString());
            addActivities(VIEW_CLIENT);


        }
    }

    private class PayBillButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PayBillDto payBillDto = employeeView.getPayBillDto();

            Notification<Boolean> notification = accountService.processBill(payBillDto);
            if (!notification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "Approved!");
                addActivities(PROCESS_UTILITIES);
            } else {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), notification.getFormattedErrors());

            }
        }
    }

    private class TransferMoneyButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            TransferMoneyDto transferMoneyDto = employeeView.getTransferMoneyDto();

            Notification<Boolean> notification = accountService.transferMoney(transferMoneyDto);

            if (notification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "Invalid Operation!");

            } else {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "Transaction success !");

                addActivities(TRANSFER_MONEY);
            }


        }
    }

    private class ViewAccountButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ClientDto clientDto = employeeView.getClientDto();
            Notification<Client> notification = clientService.getClientByCNP(clientDto.getCNP());
            if (!notification.hasErrors()) {
                List<Account> accounts = accountService.getAccount(notification.getResult());
                if (accounts == null) {
                    JOptionPane.showMessageDialog(employeeView.getContentPane(), "Invalid Account!");
                } else {
                    employeeView.displayList("Accounts", accounts.toString());
                    addActivities(VIEW_ACCOUNT);
                }
            } else {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), notification.getFormattedErrors());
            }
        }
    }

    private class AddAccountButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            AccountDto accountDto = employeeView.getAccountDto();
            ClientDto clientDto = employeeView.getClientDto();

            Notification<Boolean> account = accountService.createAccount(clientDto, accountDto);
            if (!account.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "Account successfully created!");
                addActivities(CREATE_ACCOUNT);
            } else {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), account.getFormattedErrors());
            }
        }
    }

    private class UpdateAccountButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            AccountDto accountDto = employeeView.getAccountDto();
            Notification<Boolean> notification = accountService.updateAccount(accountDto);
            if (notification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), notification.getFormattedErrors());

            } else {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "Account successfully updated !");
                addActivities(UPDATE_ACCOUNT);
            }
        }
    }

    private class DeleteAccountButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            AccountDto accountDto = employeeView.getAccountDto();
            ClientDto clientDto = employeeView.getClientDto();
            Notification<Boolean> notification = accountService.deleteAccount(clientDto, accountDto);
            if (notification.hasErrors()) {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), notification.getFormattedErrors());
            } else {
                JOptionPane.showMessageDialog(employeeView.getContentPane(), "Account successfully deleted !");
                addActivities(DELETE_ACCOUNT);
            }

        }


    }

    private class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            employeeView.setVisible(false);
            loginView.setVisible(true);
        }
    }

    private void addActivities(String Activity) {
        activityService.addActivity(new ActivityBuilder()
                .setUser(employeeView.getEmployee())
                .setAction(Activity).build());
    }


}
