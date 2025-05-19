package MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import multi.Message;
import User.User;
import User.UserDatabase;

public class ProfilePanel extends JPanel {
    private String userId;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MainFrame mainFrame;
    private Gson gson = new Gson();

    private JTextField nicknameField;
    private JTextField introField;

    public ProfilePanel(String userId, Socket socket, PrintWriter out, BufferedReader in, MainFrame mainFrame) {
        this.userId = userId;
        this.socket = socket;
        this.out = out;
        this.in = in;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(20, 20));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("프로필 설정", JLabel.CENTER);
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // 입력 폼
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBackground(Color.WHITE);

        nicknameField = new JTextField();
        introField = new JTextField();

        formPanel.add(new JLabel("닉네임:"));
        formPanel.add(nicknameField);
        formPanel.add(new JLabel("소개:"));
        formPanel.add(introField);

        add(formPanel, BorderLayout.CENTER);

        // 저장 버튼
        JButton saveButton = new JButton("저장");
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        saveButton.setBackground(new Color(102, 204, 204));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(100, 40));

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameField.getText().trim();
                String intro = introField.getText().trim();

                if (nickname.isEmpty()) {
                    JOptionPane.showMessageDialog(ProfilePanel.this, "닉네임을 입력하세요.");
                    return;
                }

                // 서버에 저장 요청 전송
                Message msg = new Message();
                msg.setType("PROFILE_SAVE");
                msg.setId(userId);
                msg.setProfile("{\"nickname\":\"" + nickname + "\", \"intro\":\"" + intro + "\"}");
                out.println(gson.toJson(msg));

                // 로컬 User에도 적용
                User me = UserDatabase.shared().getUserById(userId);
                if (me != null) {
                    me.setNickname(nickname);
                    me.setIntro(intro);
                }

                // 메인 UI 업데이트
                mainFrame.updateGreeting();
                mainFrame.getFriendPanel().updateMyNickname();

                JOptionPane.showMessageDialog(ProfilePanel.this, "프로필이 저장되었습니다.");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 처음 열 때 자기 프로필 요청
        requestMyProfile();
    }

    // 서버에 내 프로필 요청
    private void requestMyProfile() {
        Message request = new Message();
        request.setType("PROFILE_REQUEST");
        request.setId(userId);
        request.setRcvid(userId);
        out.println(gson.toJson(request));
    }

    // 서버에서 프로필 응답 받았을 때 UI 반영
    public void displayUserProfile(String nickname, String intro, String targetId) {
        if (!targetId.equals(userId)) return;

        SwingUtilities.invokeLater(() -> {
            nicknameField.setText(nickname);
            introField.setText(intro);
        });
    }

    // 서버에서 호출 시 이 메서드가 불려야 함
    public void showUserProfile(String targetId) {
        Message request = new Message();
        request.setType("PROFILE_REQUEST");
        request.setId(userId);
        request.setRcvid(targetId);
        out.println(gson.toJson(request));
    }
}
