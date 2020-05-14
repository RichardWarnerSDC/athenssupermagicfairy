package general;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This user object is used to send and receive information between the client
 * and the Server.
 * 
 * This class also adds an encryption function, which we are yet to implement in
 * this class. If we do this then the package "login" can be deleted as we need.
 * 
 * @author Ed Wong, Team Athens
 * @version 1/3/2018
 */

public class User implements Serializable {
	private String username;
	private String password;
	private int status;
	private int rank;
	private int avatar;
	public int totalScore;
	private Map<String, Integer> topicHighScores;

	public static final int IDLE = 0;
	public static final int MATCHING = 1;
	public static final int IN_LOBBY = 2;
	public static final int PLAYING = 3;

	public static final int WHATISCOMPUTER = 1;
	public static final int RAMIBLESS = 2;
	public static final int WHATISDATABASE = 3;
	public static final int NOOB = 4;
	public static final int INTERMIDIATE = 5;
	public static final int GETTINGGOOD = 6;
	public static final int EXPERIENCED = 7;
	public static final int GETTINGDISTINCTION = 8;
	public static final int PRO = 9;
	public static final int UDAY = 10;

	public static final int DATABASE_DATA = 10;

	public User(String username, String password, int status, int rank) {
		this.username = username;
		this.password = password;		// maybe = getHash(password);
		this.status = status;
		this.rank = rank;
	}

	public User(String username, String password, int status, int rank, int avatar, int totalScore) {
		this.username = username;
		this.password = password;		// maybe = getHash(password);
		this.status = status;
		this.rank = rank;
		this.avatar = avatar;
		this.totalScore = totalScore;
	}
	
//	// ASHTON IS NO LONGER USING THIS CONSTRUCTOR, please delete if nobody else is using it
//	public User(String username, int rank, int totalScore, Map<String, Integer> topicHighScores) {
//		this.username = username;
//		this.rank = rank;
//		this.totalScore = totalScore;
//		this.topicHighScores = topicHighScores;
//	}
	
	// constructor to get leaderboard, is used in Queries class
	public User(int avatar, String username, int rank, int boardScore) {
		this.avatar = avatar;
		this.username = username;
		this.rank = rank;
		this.totalScore = boardScore;
	}
	
	public String printLeaderboard() {
		return avatar + " | " + username + " | " + rank + " | " + totalScore;
	}
	
	public User(String username, String password, int globalScore, int avatar, boolean bool) {
		this.username = username;
		this.password = password;
		this.rank = globalScore;
		this.avatar = avatar;
	}

	public boolean equals(User usr){
		if(usr.getUsername().equals(this.username))
			return true;
		return false;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;			// maybe = getHash(password);
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status=status;
	}

	public void setTotalScore(int newTotalScore) { this.totalScore = newTotalScore; }

	public void setRank(int newRank) {this.rank = newRank; }

	public int getIntegerRank() { return this.rank; }

	public int getAvatar(){
		return this.avatar;
	}

	public void setAvatar(int avatar){
		this.avatar = avatar;
	}

	public int getTotalScore(){
		return this.totalScore;
	}

	
	public String toString() {
		return username + " " + password;
	}
	
	public String getStringRank() {
		String stringRank;
		if (this.rank == 1)
			stringRank = "whatiscomputer";
		else if (this.rank == 2)
			stringRank = "noob";
		else if (this.rank == 3)
			stringRank = "ramibless";
		else if (this.rank == 4)
			stringRank = "SQL: Still Quite Lame";
		else if (this.rank == 5)
			stringRank = "slightly less noobie";
		else if (this.rank == 6)
			stringRank = "De-bugsy Malone";
		else if (this.rank == 7)
			stringRank = "java is love";
		else if (this.rank == 8)
			stringRank = "freakin sick";
		else if (this.rank == 9)
			stringRank = "dat pro masters lyfe";
		else
			stringRank = "Manfred";
		return stringRank;
	}
	
	/**
	 * This method converts a user's overall score to an integer that represents
	 * their rank.
	 * 
	 * @param score
	 *            is the user's total score
	 * @return their rank as an int
	 */
	public static int convertScoreToRank(int score) {
		int intRank;
		if (score >= 50 && score < 150)
			intRank = 2;
		else if (score >= 150 && score < 300)
			intRank = 3;
		else if (score >= 300 && score < 500)
			intRank = 4;
		else if (score >= 500 && score < 1000)
			intRank = 5;
		else if (score >= 1000 && score < 1750)
			intRank = 6;
		else if (score >= 1750 && score < 3000)
			intRank = 7;
		else if (score >= 3000 && score < 5000)
			intRank = 8;
		else if (score >= 5000 && score < 8000)
			intRank = 9;
		else if (score >= 8000)
			intRank = 10;
		else
			intRank = 1;
		return intRank;
	}


	/**
	 * This is a method that we can use to create the hash of a password that is
	 * input by the client. We note that this method should be run on the
	 * client's machine and not on the Server. The algorithm that we choose to
	 * use is SHA-256.
	 * 
	 * @param password
	 *            is the password that we are to return the hash of.
	 * 
	 * @return hash which is an array of bytes which is 32 bytes long, as
	 *         SHA-256 returns 256 bits.
	 */
	public static String getHash(String password) {
		String algorithm = "SHA-256";
		byte[] hashArr = new byte[32];
		try {
			MessageDigest digest = MessageDigest.getInstance(algorithm);
			hashArr = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		} catch (NoSuchAlgorithmException e) {
			// WTF have you done if this happens
		}

		StringBuffer hash = new StringBuffer("");
		for (byte b : hashArr)
			hash.append(b);

		return hash.toString();
	}

	/**
	 * Here is a method that we run on the Server. We run this method to check
	 * whether the hash produced by the password is equal to the hash that is
	 * stored in our PSQL database.
	 * 
	 * @param hashProvided
	 *            is the String which is the username provided in the client
	 * 
	 * @return true if the hashes are equal, false otherwise
	 */
	public boolean compareHash(String hashProvided) {
		return getHash(this.getPassword()).equals(hashProvided);
	}
	
}
