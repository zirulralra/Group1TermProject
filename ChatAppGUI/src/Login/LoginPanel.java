package Login;

import javax.swing.*;

import SignUp.SignUpFrame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JTextField idField;
    private JPasswordField pwField;
    private JButton loginButton;
    private JButton joinButton;

    public LoginPanel() {
        setLayout(new GridLayout(5, 2));

        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField();

        JLabel pwLabel = new JLabel("Password:");
        pwField = new JPasswordField();

        loginButton = new JButton("로그인");
        joinButton = new JButton("회원가입");
        
        add(idLabel);
        add(idField);
        add(pwLabel);
        add(pwField);
        add(new JLabel()); // 공백
        add(loginButton);

        loginButton.addActionListener(new LoginController(idField, pwField));

        
        add(loginButton);
        
        add(new JLabel()); // 공백
        add(new JLabel()); // 공백
        add(new JLabel()); // 공백
        
        add(joinButton);
     
        // 버튼 이벤트 등록
        loginButton.addActionListener(new LoginController(idField, pwField));
        joinButton.addActionListener(e -> new SignUpFrame()); // 새 창 열기

    }
}