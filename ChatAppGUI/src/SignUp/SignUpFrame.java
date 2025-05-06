package SignUp;

import javax.swing.*;

public class SignUpFrame extends JFrame {
    public SignUpFrame() {
        setTitle("회원가입");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new SignUpPanel());

        setVisible(true);
    }
}
