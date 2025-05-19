package User;

public class User {
    private String id;
    private String password; // 암호화된 비밀번호 저장
    private String nickname;
    private String intro;

    public User(String id, String password) {
        this.id = id;
        this.password = PasswordEncryption.encrypt(password);
        this.nickname = id != null ? id : "사용자";  // 기본 닉네임 = ID
        this.intro = "";
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname != null ? nickname : id;
    }

    public void setNickname(String nickname) {
        this.nickname = (nickname != null && !nickname.trim().isEmpty()) ? nickname : this.id;
    }

    public String getIntro() {
        return intro != null ? intro : "";
    }

    public void setIntro(String intro) {
        this.intro = (intro != null) ? intro : "";
    }

    /**
     * 입력된 비밀번호와 저장된 암호화 비밀번호 비교
     */
    public boolean verifyPassword(String inputPassword) {
        String decryptedPassword = PasswordEncryption.decrypt(this.password);
        return decryptedPassword != null && decryptedPassword.equals(inputPassword);
    }
}
