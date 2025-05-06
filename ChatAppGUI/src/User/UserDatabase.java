package User;

import java.util.ArrayList;

public class UserDatabase {
    private static UserDatabase instance = new UserDatabase();
    private ArrayList<User> users;

    private UserDatabase() {
        users = new ArrayList<>();
        users.add(new User("test", "1234")); 
    }

    public static UserDatabase shared() {
        return instance;
    }

    public boolean isValidUser(String id, String password) {
        for (User user : users) {
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        users.add(user);
    }
    
    public boolean isDuplicateId(String id) { //id 중복이면 회원가입 불가
        for (User user : users) {
            if (user.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    
}
