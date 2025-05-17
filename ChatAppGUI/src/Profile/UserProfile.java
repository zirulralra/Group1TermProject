package Profile;

/**
 * 사용자 프로필 정보를 저장하는 클래스
 */
public class UserProfile {
    private String userId;
    private String nickname;
    private String bio;
    
    public UserProfile(String userId) {
        this.userId = userId;
        this.nickname = userId; // 기본값으로 userId 사용
        this.bio = ""; // 기본 자기소개는 빈 문자열
    }
    
    public UserProfile(String userId, String nickname, String bio) {
        this.userId = userId;
        this.nickname = nickname;
        this.bio = bio;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
}