package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static javax.swing.BoxLayout.Y_AXIS;

public class LoginView extends JFrame {
    private JTextField tfUsername;
    private JTextField tfPassword;
    private JButton btnLogin;
    private JLabel jbUsername;
    private JLabel jbPassword;

    public LoginView() throws HeadlessException {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setTitle("LOGIN");
        initializeFields();
        setLayout(new BoxLayout(getContentPane(), Y_AXIS));
        add(jbUsername);
        add(tfUsername);
        add(jbPassword);
        add(tfPassword);
        add(btnLogin);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initializeFields() {
        tfUsername = new JTextField();
        tfPassword = new JTextField();
        jbUsername = new JLabel("Username");
        jbPassword = new JLabel("Password");
        btnLogin = new JButton("Login");
    }

    public void setLoginButtonActionListener(ActionListener loginButtonListener) {
        this.btnLogin.addActionListener(loginButtonListener);
    }


    public String getPassword() {
        return tfPassword.getText();
    }

    public String getUsername() {
        return tfUsername.getText();
    }
}
