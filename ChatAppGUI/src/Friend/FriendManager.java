package Friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * FriendManager - Singleton class to manage friend relationships
 */
public class FriendManager {
    private static FriendManager instance;
    private Map<String, ArrayList<String>> friendships;
    
    private FriendManager() {
        friendships = new HashMap<>();
    }
    
    public static synchronized FriendManager getInstance() {
        if (instance == null) {
            instance = new FriendManager();
        }
        return instance;
    }
    
    /**
     * Add a friend relationship (bidirectional)
     */
    public void addFriend(String userId, String friendId) {
        // Add friend to user's list
        if (!friendships.containsKey(userId)) {
            friendships.put(userId, new ArrayList<>());
        }
        if (!friendships.get(userId).contains(friendId)) {
            friendships.get(userId).add(friendId);
        }
        
        // Add user to friend's list (bidirectional relationship)
        if (!friendships.containsKey(friendId)) {
            friendships.put(friendId, new ArrayList<>());
        }
        if (!friendships.get(friendId).contains(userId)) {
            friendships.get(friendId).add(userId);
        }
    }
    
    /**
     * Remove a friend relationship (bidirectional)
     */
    public void removeFriend(String userId, String friendId) {
        // Remove from user's list
        if (friendships.containsKey(userId)) {
            friendships.get(userId).remove(friendId);
        }
        
        // Remove from friend's list
        if (friendships.containsKey(friendId)) {
            friendships.get(friendId).remove(userId);
        }
    }
    
    /**
     * Check if two users are friends
     */
    public boolean isFriend(String userId, String friendId) {
        return friendships.containsKey(userId) && 
               friendships.get(userId).contains(friendId);
    }
    
    /**
     * Get a user's friend list
     */
    public ArrayList<String> getFriendList(String userId) {
        if (!friendships.containsKey(userId)) {
            friendships.put(userId, new ArrayList<>());
        }
        return friendships.get(userId);
    }
}