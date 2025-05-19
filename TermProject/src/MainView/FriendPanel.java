package MainView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import User.User;
import User.UserDatabase;

public class FriendPanel extends JPanel {
    private DefaultListModel<String> friendListModel = new DefaultListModel<>();
    private JList<String> friendList = new JList<>(friendListModel);
    private ProfilePanel profilePanel;

    private String userId;
    private JLabel nicknameLabel;
    private JLabel introLabel;

    public FriendPanel(String userId) {
        this.userId = userId;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        User me = UserDatabase.shared().getUserById(userId);
        String nickname = (me != null && me.getNickname() != null) ? me.getNickname() : userId;

        JPanel myProfilePanel = new JPanel(new BorderLayout(10, 0));
        myProfilePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        myProfilePanel.setBackground(Color.WHITE);

        ImageIcon icon = new ImageIcon(getClass().getResource("/img/logo.png"));
        Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JLabel profileImg = new JLabel(new ImageIcon(img));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);

        nicknameLabel = new JLabel(nickname);
        nicknameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        nicknameLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        introLabel = new JLabel(" ");
        introLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        introLabel.setForeground(Color.DARK_GRAY);
        introLabel.setHorizontalAlignment(SwingConstants.LEFT);
        introLabel.setVerticalAlignment(SwingConstants.TOP);
        introLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));

        textPanel.add(nicknameLabel);
        textPanel.add(introLabel);

        myProfilePanel.add(profileImg, BorderLayout.WEST);
        myProfilePanel.add(textPanel, BorderLayout.CENTER);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(200, 200, 200));
        separator.setPreferredSize(new Dimension(1, 1));

        JPanel profileContainer = new JPanel(new BorderLayout());
        profileContainer.setBackground(Color.WHITE);
        profileContainer.add(myProfilePanel, BorderLayout.CENTER);
        profileContainer.add(separator, BorderLayout.SOUTH);

        friendList.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        friendList.setSelectionBackground(new Color(220, 240, 255));
        friendList.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(friendList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        friendList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedUser = friendList.getSelectedValue();
                    if (selectedUser != null && profilePanel != null) {
                        profilePanel.showUserProfile(selectedUser);
                    }
                }
            }
        });

        add(profileContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void updateFriendList(List<String> users) {
        SwingUtilities.invokeLater(() -> {
            friendListModel.clear();
            for (String user : users) {
                friendListModel.addElement(user);
            }
            friendList.setModel(friendListModel);
        });
    }

    public void updateMyNickname() {
        User me = UserDatabase.shared().getUserById(userId);
        String nickname = (me != null && me.getNickname() != null) ? me.getNickname() : userId;
        nicknameLabel.setText(nickname);
        introLabel.setText(" ");
    }

    public void setProfilePanel(ProfilePanel profilePanel) {
        this.profilePanel = profilePanel;
    }

    public void displayUserInfo(String nickname, String intro, boolean isMine) {
        nicknameLabel.setText(nickname);

        String text;
        if (isMine) {
            text = "This is your profile.";
        } else {
            text = (intro != null && !intro.isEmpty()) ? intro : "(No introduction)";
        }

        introLabel.setText(text);
        introLabel.setVisible(true);
        introLabel.revalidate();
        introLabel.repaint();
        this.revalidate();
        this.repaint();
    }

}
