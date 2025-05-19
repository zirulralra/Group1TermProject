package MainView;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

import com.google.gson.Gson;

import User.User;
import User.UserDatabase;
import multi.Message;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    private FriendPanel friendPanel;
    private ProfilePanel profilePanel;
    private ChatPanel chatPanel;
    private JLabel greetingLabel;

    private final String userId;
    private final User myUser;

    private final Socket socket;
    private final PrintWriter out;

    public MainFrame(String userId, Socket socket, PrintWriter out, BufferedReader in) {
        this.userId = userId;
        this.socket = socket;
        this.out = out;

        User origin = UserDatabase.shared().getUserById(userId);
        this.myUser = new User(origin.getId(), origin.getPassword());
        this.myUser.setNickname(origin.getNickname());
        this.myUser.setIntro(origin.getIntro());

        setTitle("OOPTalk - " + userId);
        setSize(420, 620);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(102, 204, 204));
        topBar.setPreferredSize(new Dimension(getWidth(), 80));

        JPanel innerPanel = new JPanel(new BorderLayout());
        innerPanel.setOpaque(false);

        String nickname = (myUser != null && myUser.getNickname() != null) ? myUser.getNickname() : userId;
        greetingLabel = new JLabel("좋은 하루 되세요, " + userId + "님");
        greetingLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        greetingLabel.setForeground(Color.WHITE);
        greetingLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 10));
        innerPanel.add(greetingLabel, BorderLayout.NORTH);

        JPanel tabButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tabButtons.setOpaque(false);

        JButton friendBtn = new JButton("친구목록");
        JButton chatBtn = new JButton("채팅방");
        JButton profileBtn = new JButton("프로필");

        JButton[] buttons = { friendBtn, chatBtn, profileBtn };

        styleTabButton(friendBtn, true);
        styleTabButton(chatBtn, false);
        styleTabButton(profileBtn, false);

        tabButtons.add(friendBtn);
        tabButtons.add(chatBtn);
        tabButtons.add(profileBtn);

        innerPanel.add(tabButtons, BorderLayout.SOUTH);
        topBar.add(innerPanel, BorderLayout.CENTER);
        add(topBar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        friendPanel = new FriendPanel(userId);
        profilePanel = new ProfilePanel(userId, socket, out, in, this);
        chatPanel = new ChatPanel(userId, socket, out, in);

        friendPanel.setProfilePanel(profilePanel);
        contentPanel.add(friendPanel, "FRIEND");
        contentPanel.add(chatPanel, "CHAT");
        contentPanel.add(profilePanel, "PROFILE");

        add(contentPanel, BorderLayout.CENTER);

        friendBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "FRIEND");
            styleTabButton(friendBtn, true);
            styleTabButton(chatBtn, false);
            styleTabButton(profileBtn, false);
        });

        chatBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "CHAT");
            styleTabButton(friendBtn, false);
            styleTabButton(chatBtn, true);
            styleTabButton(profileBtn, false);
        });

        profileBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "PROFILE");
            styleTabButton(friendBtn, false);
            styleTabButton(chatBtn, false);
            styleTabButton(profileBtn, true);
        });

        cardLayout.show(contentPanel, "FRIEND");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Gson gson = new Gson();
                    Message logoutMsg = new Message(userId, "", "logout", "all", "", null, 0);
                    out.println(gson.toJson(logoutMsg));
                    out.flush();
                    Thread.sleep(300);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("로그아웃 메시지 전송 후 종료");
                }
            }
        });

        setResizable(false);
        setVisible(true);
    }

    private void styleTabButton(JButton btn, boolean isActive) {
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(51, 51, 51));
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(90, 30));

        if (isActive) {
            btn.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, new Color(200, 200, 200)));
        } else {
            btn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)));
        }
    }

    public User getMyUser() {
        return myUser;
    }

    public void updateGreeting() {
        String nickname = (myUser != null && myUser.getNickname() != null) ? myUser.getNickname() : userId;
        greetingLabel.setText("좋은 하루 되세요, " + nickname + "님");
    }

    public FriendPanel getFriendPanel() {
        return friendPanel;
    }

    public ProfilePanel getProfilePanel() {
        return profilePanel;
    }

    public ChatPanel getChatPanel() {
        return chatPanel;
    }

    public String getUserId() {
        return userId;
    }
    
    
}