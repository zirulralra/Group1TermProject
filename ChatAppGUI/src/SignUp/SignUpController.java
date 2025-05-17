package SignUp;

import javax.swing.*;

import User.User;
import User.UserDatabase;

import java.awt.event.*;

public class SignUpController implements ActionListener {
    private JTextField idField;
    private JPasswordField pwField;

    public SignUpController(JTextField idField, JPasswordField pwField) {
        this.idField = idField;
        this.pwField = pwField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String id = idField.getText();
        String pw = new String(pwField.getPassword());

        if (id.isEmpty() || pw.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID와 비밀번호를 모두 입력하세요.");
            return;
        }
        
        UserDatabase db = UserDatabase.shared();

        if (db.isDuplicateId(id)) {
            JOptionPane.showMessageDialog(null, "이미 존재하는 ID입니다.");
            return;
        }
        
        UserDatabase.shared().addUser(new User(id, pw));
        JOptionPane.showMessageDialog(null, "회원가입 완료!");
    }
}
