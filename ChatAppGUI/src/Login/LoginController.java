package Login;

import User.UserDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import multi.*; // MultiChat 관련 클래스 import
import Friend.FriendListController; // Friend List 관련 클래스 import
import Profile.ProfileController; // Profile 관련 클래스 import

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

            // 로그인 성공 후 선택 패널 표시
            JFrame optionFrame = new JFrame("메뉴 선택");
            optionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            optionFrame.setSize(300, 250); // 높이 증가
            optionFrame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(3, 1, 10, 10)); // 행 수 증가
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            JButton chatButton = new JButton("채팅방 입장");
            chatButton.setBackground(new Color(102, 204, 204));
            chatButton.setForeground(Color.WHITE);
            chatButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            JButton friendButton = new JButton("친구 목록");
            friendButton.setBackground(new Color(102, 204, 204));
            friendButton.setForeground(Color.WHITE);
            friendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            JButton profileButton = new JButton("내 프로필");
            profileButton.setBackground(new Color(102, 204, 204));
            profileButton.setForeground(Color.WHITE);
            profileButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            
            chatButton.addActionListener(event -> {
                SwingUtilities.invokeLater(() -> {
                    MultiChatData data = new MultiChatData();
                    MultiChatUI ui = new MultiChatUI(id);
                    MultiChatController app = new MultiChatController(data, ui);
                    app.appMain();
                });
            });
            
            friendButton.addActionListener(event -> {
                FriendListController.openFriendList(id);
            });
            
            profileButton.addActionListener(event -> {
                ProfileController.openProfile(id);
            });
            
            panel.add(chatButton);
            panel.add(friendButton);
            panel.add(profileButton); // 프로필 버튼 추가
            
            optionFrame.add(panel);
            optionFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(null, "로그인 실패");
        }
    }
}