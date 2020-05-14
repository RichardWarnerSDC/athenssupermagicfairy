package database;

import java.util.List;
import java.util.Map;

import general.Question;
import general.User;

/**
 * Interface to access methods in the Queries class.
 * @author Ashton Hills
 */
public interface QueriesInterface {
	
	// method to create a Quiz of 10 questions
	// returns list of Question objects
	public List<Question> createQuiz(String topic, int level);
	
	// method to check if the answer selected is the correct answer
	public boolean isCorrectAnswer(String ques, String ans);
	
	// method to check if a username is available or if it already exists in the database
	public boolean checkUsernameAvailable(String user);
	
	// method to register a new user
	public boolean registerUser(String user, String pass);
//	
//	// method to check if a desired username is valid (i.e. doesn't violate the restrictions
//	// placed on what a username can be)
//    public boolean checkValidUsername(String username);
//    
//	// method to check if a desired password is valid (i.e. doesn't violate the restrictions
//	// placed on what a password can be)
//    public boolean checkValidPassword(String username, String password);
    
    // method to change a username (updates the database, returns true if update was successful)
    public boolean changeUsername(String oldUsername, String newUsername);
    
    // method to change a password (updates the database, returns true if update was successful)
    public boolean changePassword(String username, String newPassword);
    
    // method to change an avatar (updates the database, returns true if update was successful)
    public boolean changeAvatar(String username, int avatar);
    
    // method to verify a user during the log-in process (returns true if both username and
    // password match what is in the database, false otherwise)
    public boolean verifyUser(String username, String password);
    
    // method to update database with new total score and possible change in high score/ranking
    // returns a boolean for whether the player has levelled-up or not (whether their rank has changed)
    public boolean updateScoreAndRank(String username, int score, String topic);
    
    // method to get total score of particular user
    public int getTotalScore(String username);
    
    // method to get topic high score of particular user
    public int getTopicHighScore(String username, String topic);
    
    // method to get user's avatar
    public int getAvatar(String username);
    
    // method to get user's rank
    public int getRank(String username);
    
    // method to get leaderboard for certain topic
    public Map<Integer, User> getTopicLeaderboard(String topic, int numberOfRows);
    
    // method to get overall leaderboard
    public Map<Integer, User> getMainLeaderboard(int numberOfRows);
    
    // helper method to convert topic names to column names in the database
    public String topicToColumn(String topic);
	
}
