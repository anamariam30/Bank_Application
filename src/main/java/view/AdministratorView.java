package view;

import model.User;
import model.dataTransferObject.UserDto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

public class AdministratorView extends JFrame {
    private JButton btnAddEmployee;
    private JButton btnUpdateEmployee;
    private JButton btnDeleteEmployee;
    private JButton btnGenerateReport;
    private JButton btnViewAllEmployees;
    private JButton btnBack;

    private JTextField tfUsername;
    private JTextField tfNewUsername;
    private JTextField tfPassword;
    private JTextField tfNewPassword;
    private JTextField tfDataFrom;
    private JTextField tfDataUntil;

    private JLabel lUsername;
    private JLabel lNewUsername;
    private JLabel lPassword;
    private JLabel lNewPassword;
    private JLabel lDataFrom;
    private JLabel lDataUntil;

    private JRadioButton changeRole;


    public AdministratorView() throws HeadlessException {
        setSize(400, 500);
        setTitle("ADMINISTRATOR");
        setLocationRelativeTo(null);
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), Y_AXIS));
        add(lUsername);
        add(tfUsername);
        add(lPassword);
        add(tfPassword);
        add(lDataFrom);
        add(tfDataFrom);
        add(lDataUntil);
        add(tfDataUntil);

        add(lNewUsername);
        add(tfNewUsername);
        add(lNewPassword);
        add(tfNewPassword);

        add(btnAddEmployee);
        add(btnDeleteEmployee);
        add(btnUpdateEmployee);
        add(changeRole);
        add(btnViewAllEmployees);
        add(btnGenerateReport);
        add(btnBack);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {
        btnAddEmployee = new JButton("AddEmployee");
        btnDeleteEmployee = new JButton("DeleteEmployee");
        btnUpdateEmployee = new JButton("UpdateEmployee");
        btnGenerateReport = new JButton("GenerateReports");
        btnViewAllEmployees = new JButton("ViewAllEmployees");
        btnBack = new JButton("Back");


        lUsername = new JLabel("Username");
        lNewUsername = new JLabel("New Username");
        lPassword = new JLabel("Password");
        lNewPassword = new JLabel("New Password");
        lDataFrom = new JLabel("Data Starting From");
        lDataUntil = new JLabel("Data Until");

        tfUsername = new JTextField();
        tfNewUsername = new JTextField();
        tfPassword = new JTextField();
        tfNewPassword = new JTextField();
        tfDataFrom = new JTextField();
        tfDataUntil = new JTextField();

        changeRole = new JRadioButton("Change Role");

    }

    public void setAddButtonActionListener(ActionListener AddButtonListener) {
        this.btnAddEmployee.addActionListener(AddButtonListener);

    }

    public void setDeleteButtonActionListener(ActionListener DeleteButtonListener) {
        this.btnDeleteEmployee.addActionListener(DeleteButtonListener);
    }

    public void setUpdateButtonActionListener(ActionListener UpdateButtonListener) {
        this.btnUpdateEmployee.addActionListener(UpdateButtonListener);
    }


    public void setViewAllButtonActionListener(ActionListener ViewAllButtonListener) {

        this.btnViewAllEmployees.addActionListener(ViewAllButtonListener);
    }

    public void setGenerateReportsButtonActionListener(ActionListener GenerateReportsButtonListener) {

        this.btnGenerateReport.addActionListener(GenerateReportsButtonListener);
    }

    public void setBackButtonActionListener(ActionListener BackButtonListener) {

        this.btnBack.addActionListener(BackButtonListener);
    }

    public String getPassword() {
        return tfPassword.getText();
    }

    public String getUsername() {
        return tfUsername.getText();
    }

    public String getFrom() {
        return tfDataFrom.getText();
    }

    public String getUntil() {
        return tfDataUntil.getText();
    }

    public UserDto getUserDto()
    {
        UserDto user=new UserDto(tfUsername.getText(),tfPassword.getText());
        return user;
    }

    public UserDto getUpdateUserDto()
    {
        UserDto user=new UserDto(tfNewUsername.getText(),tfNewPassword.getText(),changeRole.isSelected());
        return user;

    }

    public void displayList(String title,String displayList ) {
        JFrame displayFrame = new JFrame(title);
        displayFrame.setLocationRelativeTo(null);
        displayFrame.setSize(500, 500);
        displayFrame.setVisible(true);

        JTextArea displayText = new JTextArea();
        displayText.append(displayList);
        displayText.setEditable(false);
        displayFrame.add(displayText);

    }


}
