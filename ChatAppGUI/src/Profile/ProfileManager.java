package Profile;

import java.util.HashMap;
import java.util.Map;

/**
 * 프로필 정보를 관리하는 싱글톤 클래스
 */
public class ProfileManager {
    private static ProfileManager instance;
    private Map<String, UserProfile> profiles;
    
    private ProfileManager() {
        profiles = new HashMap<>();
    }
    
    public static synchronized ProfileManager getInstance() {
        if (instance == null) {
            instance = new ProfileManager();
        }
        return instance;
    }
    
    /**
     * 사용자 프로필 정보 가져오기
     */
    public UserProfile getProfile(String userId) {
        if (!profiles.containsKey(userId)) {
            // 프로필이 없으면 새로 생성
            profiles.put(userId, new UserProfile(userId));
        }
        return profiles.get(userId);
    }
    
    /**
     * 사용자 프로필 정보 업데이트
     */
    public void updateProfile(String userId, String nickname, String bio) {
        UserProfile profile = getProfile(userId);
        profile.setNickname(nickname);
        profile.setBio(bio);
    }
}