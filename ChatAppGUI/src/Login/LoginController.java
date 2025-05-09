package Login;

import User.UserDatabase;


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

            // 현재 로그인 창 종료
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(idField);
            topFrame.dispose();

            SwingUtilities.invokeLater(() -> {
                MultiChatData data = new MultiChatData();
                MultiChatUI ui = new MultiChatUI(id); // 사용자 이름 전달
                MultiChatController app = new MultiChatController(data, ui);
                app.appMain(); // 메신저 시작
            });

        } else {
            JOptionPane.showMessageDialog(null, "로그인 실패");
        }
    }

}
