package User;

public class User {
    private String id;
    private String password; // 암호화된 비밀번호 저장

    public User(String id, String password) {
        this.id = id;
        // 생성 시 비밀번호 암호화
        this.password = PasswordEncryption.encrypt(password);
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
    
    /**
     * 입력된 비밀번호와 저장된 암호화 비밀번호 비교
     * @param inputPassword 입력된 비밀번호
     * @return 일치 여부
     */
    public boolean verifyPassword(String inputPassword) {
        // 저장된 암호화 비밀번호 복호화
        String decryptedPassword = PasswordEncryption.decrypt(this.password);
        // 입력된 비밀번호와 복호화된 비밀번호 비교
        return decryptedPassword != null && decryptedPassword.equals(inputPassword);
    }
}