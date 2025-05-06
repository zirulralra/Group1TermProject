package Login;

import java.awt.event.*;

import javax.swing.*;

import Chat.ChatFrame;
import User.UserDatabase;

public class LoginController implements ActionListener {
    private JTextField idField;
    private JPasswordField pwField;
    private UserDatabase db;

    public LoginController(JTextField idField, JPasswordField pwField) {
        this.idField = idField;
        this.pwField = pwField;
        this.db = UserDatabase.shared();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = idField.getText();
        String pw = new String(pwField.getPassword());

        if (UserDatabase.shared().isValidUser(id, pw)) {
            JOptionPane.showMessageDialog(null, "로그인 성공");
            SwingUtilities.invokeLater(() -> new ChatFrame(id));
        } else {
            JOptionPane.showMessageDialog(null, "로그인 실패");
        }
    }
}

