package databaseTesting;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.JDBC;
import general.Question;
import general.User;

public class QueriesTests {
	
	private QueriesMock q = new QueriesMock();
	private String topicOS, topicCS, topicNISO;
	private String validUser, capsUser, existingUser, newUser, newUser2;
	private String validPass, capsPass, invalidPass, newPass;
	private int avatar1, avatar2;
	private String ques1, ans1False, ans1True;
	private String ques2, ans2False, ans2True;
	private String[] testQuiz;
	private Connection conn;
	
	private void truncateTable() {
		JDBC.connectToDatabase();
		conn = JDBC.getConn();
		PreparedStatement pstmt = null;
		final String DROP = "DROP TABLE usersTest";
		try {
			pstmt = conn.prepareStatement(DROP);
			pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	private void rebuildTable() {
		JDBC.connectToDatabase();
		conn = JDBC.getConn();
		PreparedStatement pstmt = null;
		final String BUILD_TABLE = "CREATE TABLE IF NOT EXISTS usersTest (" 
				+ "  user_id SERIAL PRIMARY KEY NOT NULL,"
				+ "  username VARCHAR(20) NOT NULL," 
				+ "  password VARCHAR(20) NOT NULL,"
				+ "  total_score INT NOT NULL DEFAULT 0," 
				+ "  rank INT NOT NULL DEFAULT 1,"
				+ "  avatar INT NOT NULL DEFAULT 1," 
				+ "  niso INT DEFAULT 0," 
				+ "  sw INT DEFAULT 0,"
				+ "  ds INT DEFAULT 0," 
				+ "  cs INT DEFAULT 0," 
				+ "  ai INT DEFAULT 0," 
				+ "  db INT DEFAULT 0,"
				+ "  os INT DEFAULT 0)";
		try {
			pstmt = conn.prepareStatement(BUILD_TABLE);
			pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	private void repopulateTable() {
		JDBC.connectToDatabase();
		conn = JDBC.getConn();
		PreparedStatement pstmt = null;
		final String POPULATE = "INSERT INTO usersTest VALUES "
				+ "(DEFAULT, ?, ?, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT),"
				+ "(DEFAULT, ?, ?, 1000, 3, 5, 6, 6, 3, 5, 7, 0, 1),"
				+ "(DEFAULT, ?, ?, 222, 2, 2, 5, 1, 7, 2, 7, 2, 1),"
				+ "(DEFAULT, ?, ?, 123, 9, 9, 4, 3, 2, 7, 7, 6, 9),"
				+ "(DEFAULT, ?, ?, 501, 5, 5, 600, 4, 1, 3, 7, 5, 5)";
		try {
			pstmt = conn.prepareStatement(POPULATE);
			pstmt.setString(1, "username1");
			pstmt.setString(2, "password1");
			pstmt.setString(3, "UserName2");
			pstmt.setString(4, "PassWord2");
			pstmt.setString(5, "username3");
			pstmt.setString(6, "password3");
			pstmt.setString(7, "username4");
			pstmt.setString(8, "password4");
			pstmt.setString(9, "username5");
			pstmt.setString(10, "password5");
			pstmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	@BeforeEach
	public void setUp() {
		
		truncateTable();
				
		topicOS = "Operating Systems and Networks";
		topicCS = "Computer Science";
		topicNISO = "Nature Inspired Search and Optimisation";
		
		validUser = "username1";
		capsUser = "UserName1";
		existingUser = "username4";
		newUser = "newUserTest";
		newUser2 = "changeUsernameTest";
		
		validPass = "password1";
		capsPass = "PassWord1";
		invalidPass = " ";
		newPass = "NewUserTest1";
		
		avatar1 = 1;
		avatar2 = 2;
		
		ques1 = "What is the time complexity of Breadth First Search?";
		ans1False = "O(b^m)";
		ans1True = "O(b^d)";
		
		ques2 = "Which of these statements about computer memory is NOT true?";
		ans2False = "All memory registers are the same size.";
		ans2True = "The maximum storage in any one location is 2 bytes.";
		
		rebuildTable();
		repopulateTable();
		
		System.out.println("Set up successful.");
	}
	
//	createQuiz() Tests =======================================================
	
//	@Test
//	public void testCreateQuiz() {
//		
//	}
	
//	isCorrectAnswer() Tests ===================================================
	
	@Test
	public void isCorrectAnswerTest1() {
		System.out.println(ques1);
		assertFalse(q.isCorrectAnswer(ques1, ans1False));
	}
	
	@Test
	public void isCorrectAnswerTest2() {
		assertTrue(q.isCorrectAnswer(ques1, ans1True));
	}
	
	@Test
	public void isCorrectAnswerTest3() {
		assertFalse(q.isCorrectAnswer(ques2, ans2False));
	}
	
	@Test
	public void isCorrectAnswerTest4() {
		assertTrue(q.isCorrectAnswer(ques2, ans2True));
	}
	
//	registerUser() tests ====================================================	

	@Test	
	public void registerUserTest1() { 						// valid credentials
		assertTrue(q.registerUser(newUser, newPass));
	}
	
	@Test
	public void registerUserTest2() {				 		// username already exists
		assertFalse(q.registerUser(existingUser, validPass));
	}
	
//	checkUsernameAvailable tests =======================================================
	
	@Test
	public void checkUsernameAvailableTest1() { 				// is available
		assertTrue(q.checkUsernameAvailable("lalalala"));
	}
	
	@Test
	public void checkUsernameAvailableTest2() {				// already exists
		assertFalse(q.checkUsernameAvailable(existingUser));
	}
	
	@Test
	public void checkUsernameAvailableTest3() {				// already exists (case insensitive)
		assertFalse(q.checkUsernameAvailable(validUser));
	}
	
//	verifyUser() tests ================================================================

	@Test
	public void verifyUserTest1() {							// credentials correct
		assertTrue(q.verifyUser(validUser, validPass));
	}
	
	@Test
	public void verifyUserTest2() {							// incorrect password
		assertFalse(q.verifyUser(validUser, invalidPass));
	}
	
	@Test
	public void verifyUserTest3() {							// incorrect username
		assertFalse(q.verifyUser("username4", validPass));
	}
	
	@Test
	public void verifyUserTest4() {							// incorrect username and password
		assertFalse(q.verifyUser("username4", invalidPass));
	}
	
	@Test
	public void verifyUserTest5() {							// username case-insensitive
		assertTrue(q.verifyUser(capsUser, validPass));
	}
	
	@Test
	public void verifyUserTest6() {							// password case sensitive
		assertFalse(q.verifyUser(validUser, capsPass));
	}
	
//	changeUsername() tests ==============================================================
	
	@Test
	public void changeUsernameTest1() {
		assertTrue(q.changeUsername(capsUser, newUser2));			// username is case insensitive
	}	
	
//	@Test
//	public void changeUsernameTest2() {
//		assertFalse(q.changeUsername(validUser, existingUser));	// username already exists
//	}
	
	@Test
	public void changeUsernameTest3() {
		assertTrue(q.changeUsername(validUser, "userNAME123"));
	}
	
	@Test
	public void changeUsernameTest4() {
		assertTrue(q.changeUsername(capsUser, "changeUsernameAgain"));		// normal case
	}
	
//	changePassword() tests ==============================================================

	@Test
	public void changePasswordTest1() {
		assertTrue(q.changePassword(newUser, newPass));			// normal case
	}
	
	@Test
	public void changePasswordTest2() {
		assertTrue(q.changePassword(capsUser, newPass));			// checks username case non sensitive
	}	
	
//	changeAvatar() tests ==============================================================
	
	@Test
	public void changeAvatarTest1() {
		assertTrue(q.changeAvatar(validUser, avatar2));			// normal case
	}
	
	@Test
	public void changeAvatarTest2() {
		assertTrue(q.changeAvatar(capsUser, avatar1));			// checks username case non sensitive
	}
	
//	updateScoreAndRank() tests =========================================================

	// update total score but not rank or high score (aalso checks username not case sensitive)
	@Test
	public void updateScoreAndRankTest1() {
		 assertFalse(q.updateScoreAndRank(capsUser, 5, topicOS));
	}
	 
	// update total and high score but not rank
	@Test
	public void updateScoreAndRankTest2() {
		 assertFalse(q.updateScoreAndRank(validUser, 1, topicCS));
	}
	
	// update total and rank but not high score
	@Test
	public void updateScoreAndRankTest3() {
		 assertTrue(q.updateScoreAndRank("username5", 500, topicNISO));
	}
	
	// update all three
	@Test
	public void updateScoreAndRankTest4() {
		 assertTrue(q.updateScoreAndRank(validUser, 5000, topicCS));
	}
	
//	getTotalScore() tests =========================================================

	@Test
	public void getTotalScoreTest1() {
		int expected = 123;
		int actual = q.getTotalScore("username4");
		assertEquals(expected, actual);
	}
	
	@Test
	public void getTotalScoreTest2() {				// checks username case non sensitive
		int expected = 0;
		int actual = q.getTotalScore(capsUser);
		assertEquals(expected, actual);
	}
	
//	getTopicHighScore() tests =========================================================

	@Test
	public void getTopicHighScoreTest1() {
		int expected = 5;
		int actual = q.getTopicHighScore("username5", topicOS);
		assertEquals(expected, actual);
	}
	
	@Test
	public void getTopicHighScoreTest2() {
		int expected = 5;
		int actual = q.getTopicHighScore("userNAME5", topicOS);
		assertEquals(expected, actual);
	}
	 
//	getAvatar() tests =========================================================

	@Test
	public void getAvatarTest1() {
		int expected = 2;
		int actual = q.getAvatar("username3");
		assertEquals(expected, actual);
	}
	
	@Test
	public void getAvatarTest2() {
		int expected = 1;
		int actual = q.getAvatar(capsUser);
		assertEquals(expected, actual);
	}
	
//	getRank() tests =========================================================

	@Test
	public void getRankTest1() {
		int expected = 1;
		int actual = q.getAvatar(validUser);
		assertEquals(expected, actual);
	}
	
	@Test
	public void getRankTest2() {
		int expected = 2;
		int actual = q.getAvatar("username3");
		assertEquals(expected, actual);
	}
	
//	topicToColumn() tests =========================================================

	@Test
	public void topicToColumnTest1() {
		String expected = "niso";
		String actual = q.topicToColumn(topicNISO);
		assertEquals(expected, actual);
	}
	 
//	getTopicLeaderboard() tests =========================================================

	@Test
	public void getTopicLeaderboardTest1() {								// normal case
		Map<Integer, User> actual = q.getTopicLeaderboard("Data Structures", 3);
		String actualString = "";
		int i = 1;
		while (actual.get(i) != null) {
			actualString += i + " | " + actual.get(i).printLeaderboard();
			i++;
		}
		String expected = "1 | 2 | username3 | 2 | 72 | 5 | UserName2 | 3 | 33 | 9 | username4 | 9 | 2";
		assertEquals(expected, actualString);
	}
	
	@Test																// if there are not enough users in table
	public void getTopicLeaderboardTest2() {
		Map<Integer, User> actual = q.getTopicLeaderboard("Data Structures", 10);
		String actualString = "";
		int i = 1;
		while (actual.get(i) != null) {
			actualString += i + " | " + actual.get(i).printLeaderboard();
			i++;
		}
		String expected = "1 | 2 | username3 | 2 | 72 | 5 | UserName2 | 3 | 33 | 9 | username4 | 9 | 24 | 5 | username5 | 5 | 15 | 1 | username1 | 1 | 0";
		assertEquals(expected, actualString);
	}
	
	
	
//	getTopicLeaderboard() tests =========================================================

	@Test
	public void getMainLeaderboardTest1() {
		Map<Integer, User> actual = q.getMainLeaderboard(3);
		String actualString = "";
		int i = 1;
		while (actual.get(i) != null) {
			actualString += i + " | " + actual.get(i).printLeaderboard();
			i++;
		}
		String expected = "1 | 5 | UserName2 | 3 | 10002 | 5 | username5 | 5 | 5013 | 2 | username3 | 2 | 222";
		assertEquals(expected, actualString);
	}
	
	@Test
	public void getMainLeaderboardTest2() {
		Map<Integer, User> actual = q.getMainLeaderboard(8);
		String actualString = "";
		int i = 1;
		while (actual.get(i) != null) {
			actualString += i + " | " + actual.get(i).printLeaderboard();
			i++;
		}
		String expected = "1 | 5 | UserName2 | 3 | 10002 | 5 | username5 | 5 | 5013 | 2 | username3 | 2 | 2224 | 9 | username4 | 9 | 1235 | 1 | username1 | 1 | 0";
		assertEquals(expected, actualString);
	}
//	
//	@Test
//	public void createQuizTest1() {
//		List<Question> actual = q.createQuiz("Data Structures", 1);
//		String actualString = "";
//		for (int i = 0; i < 3; i++) {
//			actualString += (i+1) + " | " + actual.get(i).toString();
//		}
//		String expected = "";
//		assertEquals(expected, actualString);
//	}
	 
	 
}
