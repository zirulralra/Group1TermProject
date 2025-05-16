package Friend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import User.User;
import User.UserDatabase;
import Chat.ChatFrame;

public class FriendListUI extends JFrame {
    private JPanel contentPane;
    private JList<String> friendList;
    private DefaultListModel<String> friendListModel;
    private JList<String> onlineUsersList;
    private DefaultListModel<String> onlineUsersModel;
    private JButton addFriendButton;
    private JButton removeFriendButton;
    private JButton chatButton;
    private JButton refreshButton;
    
    private String currentUserId;
    private FriendManager friendManager;
    private Map<String, ChatFrame> activeChats = new HashMap<>();

    private final Color THEME_COLOR = new Color(102, 204, 204);

    public FriendListUI(String userId) {
        this.currentUserId = userId;
        this.friendManager = FriendManager.getInstance();
        
        setTitle(userId + "님의 친구 목록");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);
        
        initUI();
        loadFriends();
        loadOnlineUsers();
    }
    
    private void initUI() {
        // Top panel with user info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel userLabel = new JLabel("사용자: " + currentUserId);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        userLabel.setForeground(THEME_COLOR);
        topPanel.add(userLabel, BorderLayout.WEST);
        
        refreshButton = new JButton("새로고침");
        stylizeButton(refreshButton);
        refreshButton.addActionListener(e -> {
            loadFriends();
            loadOnlineUsers();
        });
        topPanel.add(refreshButton, BorderLayout.EAST);
        
        contentPane.add(topPanel, BorderLayout.NORTH);
        
        // Center panel with friend list and online users
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        centerPanel.setBackground(Color.WHITE);
        
        // Friend list panel
        JPanel friendListPanel = new JPanel(new BorderLayout(5, 5));
        friendListPanel.setBackground(Color.WHITE);
        friendListPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(THEME_COLOR),
                "내 친구",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                THEME_COLOR));
        
        friendListModel = new DefaultListModel<>();
        friendList = new JList<>(friendListModel);
        friendList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        friendList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        friendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    openChatWithSelectedFriend();
                }
            }
        });
        
        JScrollPane friendScrollPane = new JScrollPane(friendList);
        friendListPanel.add(friendScrollPane, BorderLayout.CENTER);
        
        JPanel friendButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        friendButtonPanel.setBackground(Color.WHITE);
        
        removeFriendButton = new JButton("친구 삭제");
        stylizeButton(removeFriendButton);
        removeFriendButton.addActionListener(e -> removeFriend());
        
        chatButton = new JButton("대화하기");
        stylizeButton(chatButton);
        chatButton.addActionListener(e -> openChatWithSelectedFriend());
        
        friendButtonPanel.add(removeFriendButton);
        friendButtonPanel.add(chatButton);
        friendListPanel.add(friendButtonPanel, BorderLayout.SOUTH);
        
        centerPanel.add(friendListPanel);
        
        // Online users panel
        JPanel onlineUsersPanel = new JPanel(new BorderLayout(5, 5));
        onlineUsersPanel.setBackground(Color.WHITE);
        onlineUsersPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(THEME_COLOR),
                "접속자 목록",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                THEME_COLOR));
        
        onlineUsersModel = new DefaultListModel<>();
        onlineUsersList = new JList<>(onlineUsersModel);
        onlineUsersList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        onlineUsersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane onlineScrollPane = new JScrollPane(onlineUsersList);
        onlineUsersPanel.add(onlineScrollPane, BorderLayout.CENTER);
        
        JPanel onlineButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        onlineButtonPanel.setBackground(Color.WHITE);
        
        addFriendButton = new JButton("친구 추가");
        stylizeButton(addFriendButton);
        addFriendButton.addActionListener(e -> addFriend());
        
        onlineButtonPanel.add(addFriendButton);
        onlineUsersPanel.add(onlineButtonPanel, BorderLayout.SOUTH);
        
        centerPanel.add(onlineUsersPanel);
        
        contentPane.add(centerPanel, BorderLayout.CENTER);
    }
    
    private void stylizeButton(JButton button) {
        button.setBackground(THEME_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
    }
    
    private void loadFriends() {
        friendListModel.clear();
        ArrayList<String> friends = friendManager.getFriendList(currentUserId);
        if (friends != null) {
            for (String friend : friends) {
                friendListModel.addElement(friend);
            }
        }
    }
    
    private void loadOnlineUsers() {
        onlineUsersModel.clear();
        // This is a placeholder - in real implementation, you'd get this from your server
        // For now, we'll just load all users from the UserDatabase except the current user
        for (User user : UserDatabase.shared().getAllUsers()) {
            if (!user.getId().equals(currentUserId) && 
                !friendManager.isFriend(currentUserId, user.getId())) {
                onlineUsersModel.addElement(user.getId());
            }
        }
    }
    
    private void addFriend() {
        String selectedUser = onlineUsersList.getSelectedValue();
        if (selectedUser != null) {
            friendManager.addFriend(currentUserId, selectedUser);
            JOptionPane.showMessageDialog(this, selectedUser + "님을 친구로 추가했습니다.");
            loadFriends();
            loadOnlineUsers();
        } else {
            JOptionPane.showMessageDialog(this, "친구로 추가할 사용자를 선택해주세요.");
        }
    }
    
    private void removeFriend() {
        String selectedFriend = friendList.getSelectedValue();
        if (selectedFriend != null) {
            int option = JOptionPane.showConfirmDialog(this, 
                    selectedFriend + "님을 친구 목록에서 삭제하시겠습니까?",
                    "친구 삭제", JOptionPane.YES_NO_OPTION);
            
            if (option == JOptionPane.YES_OPTION) {
                friendManager.removeFriend(currentUserId, selectedFriend);
                loadFriends();
                loadOnlineUsers();
            }
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 친구를 선택해주세요.");
        }
    }
    
    private void openChatWithSelectedFriend() {
        String selectedFriend = friendList.getSelectedValue();
        if (selectedFriend != null) {
            // Check if chat window is already open
            if (activeChats.containsKey(selectedFriend)) {
                activeChats.get(selectedFriend).toFront(); // Bring to front if already open
            } else {
                // Create a new chat window
                ChatFrame chatFrame = new ChatFrame(currentUserId);
                chatFrame.setTitle(currentUserId + " ↔ " + selectedFriend);
                activeChats.put(selectedFriend, chatFrame);
                
                // Remove from active chats when closed
                chatFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        activeChats.remove(selectedFriend);
                    }
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "대화할 친구를 선택해주세요.");
        }
    }
}