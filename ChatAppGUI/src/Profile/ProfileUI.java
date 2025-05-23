package Profile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * 프로필 화면 UI 클래스
 */
public class ProfileUI extends JFrame {
    private JPanel contentPane;
    private JTextField nicknameField;
    private JTextArea bioArea;
    private JButton saveButton;
    private JButton cancelButton;
    
    private String userId;
    private ProfileManager profileManager;
    private final Color THEME_COLOR = new Color(102, 204, 204);
    
    public ProfileUI(String userId) {
        this.userId = userId;
        this.profileManager = ProfileManager.getInstance();
        
        setTitle(userId + "님의 프로필");
        setSize(400, 400);  // 이미지 영역이 제거되어 높이 감소
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        
        initUI();
        loadProfileData();
    }
    
    private void initUI() {
        // Top panel with user ID
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel userIdLabel = new JLabel("사용자 ID: " + userId);
        userIdLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        userIdLabel.setForeground(THEME_COLOR);
        topPanel.add(userIdLabel, BorderLayout.WEST);
        
        contentPane.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with profile info
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        
        // Nickname panel
        JPanel nicknamePanel = new JPanel();
        nicknamePanel.setLayout(new BoxLayout(nicknamePanel, BoxLayout.Y_AXIS));
        nicknamePanel.setBackground(Color.WHITE);
        nicknamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nicknamePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(THEME_COLOR),
                "닉네임",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                THEME_COLOR));
        
        nicknameField = new JTextField(20);
        nicknameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        nicknameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        nicknamePanel.add(nicknameField);
        
        centerPanel.add(nicknamePanel);
        centerPanel.add(Box.createVerticalStrut(15));
        
        // Bio panel
        JPanel bioPanel = new JPanel();
        bioPanel.setLayout(new BoxLayout(bioPanel, BoxLayout.Y_AXIS));
        bioPanel.setBackground(Color.WHITE);
        bioPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bioPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(THEME_COLOR),
                "자기소개",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                THEME_COLOR));
        
        bioArea = new JTextArea(5, 20);
        bioArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        JScrollPane bioScroll = new JScrollPane(bioArea);
        bioPanel.add(bioScroll);
        
        centerPanel.add(bioPanel);
        
        contentPane.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        
        saveButton = new JButton("저장");
        stylizeButton(saveButton);
        saveButton.addActionListener(e -> saveProfile());
        
        cancelButton = new JButton("취소");
        stylizeButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void stylizeButton(JButton button) {
        button.setBackground(THEME_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
    }
    
    private void loadProfileData() {
        UserProfile profile = profileManager.getProfile(userId);
        nicknameField.setText(profile.getNickname());
        bioArea.setText(profile.getBio());
    }
    
    private void saveProfile() {
        String nickname = nicknameField.getText().trim();
        String bio = bioArea.getText().trim();
        
        if (nickname.isEmpty()) {
            JOptionPane.showMessageDialog(this, "닉네임을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        profileManager.updateProfile(userId, nickname, bio);
        JOptionPane.showMessageDialog(this, "프로필이 저장되었습니다.", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    /**
     * 프로필 창 열기
     */
    public static void openProfile(String userId) {
        SwingUtilities.invokeLater(() -> {
            ProfileUI profileUI = new ProfileUI(userId);
            profileUI.setVisible(true);
        });
    }
}