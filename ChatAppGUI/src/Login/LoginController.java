package Login;

import javax.swing.*;
import java.awt.event.*;
import multi.*; // MultiChat 관련 클래스 import

public class LoginController implements ActionListener {
    private JTextField idField;
    private JPasswordField pwField;

    public LoginController(JTextField idField, JPasswordField pwField) {
        this.idField = idField;
        this.pwField = pwField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = idField.getText();
        String pw = new String(pwField.getPassword());

        if (UserDatabase.shared().isValidUser(id, pw)) {
            JOptionPane.showMessageDialog(null, "로그인 성공");
            SwingUtilities.invokeLater(() -> {
                MultiChatData data = new MultiChatData();
                MultiChatUI ui = new MultiChatUI(); // 혹은 new MultiChatUI(id) 생성자 필요시
                MultiChatController app = new MultiChatController(data, ui);
                app.appMain();
            });
        } else {
            JOptionPane.showMessageDialog(null, "로그인 실패");
        }
    }
}
