package Friend;

import javax.swing.*;

/**
 * Controller class for managing the Friend List functionality
 */
public class FriendListController {
    private FriendListUI friendListUI;
    
    public FriendListController(String userId) {
        SwingUtilities.invokeLater(() -> {
            friendListUI = new FriendListUI(userId);
            friendListUI.setVisible(true);
        });
    }
    
    /**
     * Static method to launch the friend list window
     */
    public static void openFriendList(String userId) {
        new FriendListController(userId);
    }
}