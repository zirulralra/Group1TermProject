package SignUp;

import javax.swing.*;

import java.awt.*;

public class SignUpPanel extends JPanel {
    private JTextField idField;
    private JPasswordField pwField;
    private JButton signUpButton;

    public SignUpPanel() {
        setLayout(new GridLayout(3, 2));

        idField = new JTextField();
        pwField = new JPasswordField();
        signUpButton = new JButton("가입");

        add(new JLabel("새 ID:"));
        add(idField);
        add(new JLabel("새 Password:"));
        add(pwField);
        add(new JLabel(""));
        add(signUpButton);

        signUpButton.addActionListener(new SignUpController(idField, pwField));
    }
}
