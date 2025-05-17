package Profile;

import javax.swing.*;

/**
 * 프로필 컨트롤러 클래스
 */
public class ProfileController {
    
    private ProfileUI profileUI;
    
    public ProfileController(String userId) {
        SwingUtilities.invokeLater(() -> {
            profileUI = new ProfileUI(userId);
            profileUI.setVisible(true);
        });
    }
    
    /**
     * 프로필 창 열기
     */
    public static void openProfile(String userId) {
        new ProfileController(userId);
    }
}