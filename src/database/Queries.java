package database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import general.Question;
import general.User;

import java.io.IOException;
import java.sql.*;

/**
 * This class contains PreparedStatements to be utilised by other classes within the program
 * to query the database.
 * @author Ashton Hills
 */
public class Queries implements QueriesInterface{
	
	private Connection conn;
	
	/**
	 * Constructor for the Queries class.
	 * @param conn is the connection to the database.
	 */
	public Queries() throws IOException {
		JDBC.connectToDatabase(); 
		conn = JDBC.getConn();
	}
	
	/**
	 * This method creates a quiz of 10 questions and their corresponding answer
	 * choices, along with an integer that indicates which answer is the correct
	 * answer.
	 * 
	 * @param topic
	 *            is the topic of questions that is desired by the user
	 * @param level
	 *            is the level of play that is desired by the user
	 * @return quiz is an ArrayList of QuestionObjects
	 */
	public List<Question> createQuiz(String topic, int level) {
		int quizLength = 3;
		List<Question> quiz = new ArrayList<Question>(quizLength);
		PreparedStatement pstmtQuestions = null;
		final String GET_QUESTIONS = "SELECT question_id, question FROM question_bank "
				+ "WHERE topic = ? AND level = ? ORDER BY RANDOM() LIMIT " + quizLength;
		try {
			pstmtQuestions = conn.prepareStatement(GET_QUESTIONS);
			pstmtQuestions.setString(1, topic);
			pstmtQuestions.setInt(2, level);
			ResultSet resultQuestions = pstmtQuestions.executeQuery();
			for (int i = 0; i < quizLength; i++) {
				resultQuestions.next();
				int questionID = resultQuestions.getInt(1);
				String selectedQuestion = resultQuestions.getString(2);
				PreparedStatement pstmtAnswers = null;
				final String GET_ANSWERS = "SELECT answer FROM question_bank JOIN answer_choices "
						+ "ON question_id = question_fk WHERE question_fk = ?";
				String[] answers = new String[4];
				int correctIndex = 0;
				try {
					pstmtAnswers = conn.prepareStatement(GET_ANSWERS);
					pstmtAnswers.setInt(1, questionID);
					ResultSet resultAnswers = pstmtAnswers.executeQuery();
					conn.commit();
					for (int j = 0; j < answers.length; j++) {
						resultAnswers.next();
						answers[j] = resultAnswers.getString(1);
					}
					int k = 0;
					while (k < answers.length) {
						if(isCorrectAnswer(selectedQuestion, answers[k])) {
							correctIndex = k;
							break; 
						} else
							k++;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (pstmtAnswers != null)
						pstmtAnswers.close();
				}
				Question quesObj = new Question(selectedQuestion, answers, correctIndex);
				quiz.add(quesObj);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmtQuestions != null)
				try {
					pstmtQuestions.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return quiz;
	}

	/**
	 * This method expresses whether or not an answer selected by the user is
	 * correct.
	 * 
	 * @param ans
	 *            is the answer selected by the user
	 * @return true if the answer is correct; false otherwise.
	 */
	public boolean isCorrectAnswer(String ques, String ans) {
		PreparedStatement pstmt = null;
		final String IS_CORRECT = "SELECT correct FROM answer_choices JOIN question_bank "
				+ "ON question_id = question_fk WHERE question = ? AND answer = ?";
		try {
			pstmt = conn.prepareStatement(IS_CORRECT);
			pstmt.setString(1, ques);
			pstmt.setString(2, ans);
			ResultSet result = pstmt.executeQuery();	
			conn.commit();
			if (result.next())
				return result.getBoolean(1);
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return false;
	}
	
	/**
	 * This method registers a user by inserting their chosen username and password
	 * into the database.
	 * 
	 * @param user
	 *            is the username that the user has selected
	 * @param pass
	 *            is the password that the user has selected
	 * @return true if the registration was successful; false otherwise.
	 */
	public boolean registerUser(String user, String pass) {
		boolean check = checkUsernameAvailable(user);
		if (check) {
			PreparedStatement pstmt = null;
			final String REGISTER_USER = "INSERT INTO users (username, password) VALUES (?, ?)";
			try {
				pstmt = conn.prepareStatement(REGISTER_USER);
				pstmt.setString(1, user);
				pstmt.setString(2, pass);
				int done = pstmt.executeUpdate();
				conn.commit();
				if (done > 0)
					return true;
				else
					return false;
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
		return false;
	}
	
	/**
	 * This method checks whether a desired username is available to be taken by a
	 * new user, or if it is already being used by an existing user. Usernames are
	 * not case-sensitive.
	 * 
	 * @param username
	 *            is the username desired by the user
	 * @return true if the username is indeed available for use; false if it already
	 *         exists.
	 */
	public boolean checkUsernameAvailable(String username) {
		PreparedStatement pstmt = null;
		final String USERNAME_AVAILABLE = "SELECT * FROM users WHERE LOWER(username) LIKE LOWER(?)";
		try {
			pstmt = conn.prepareStatement(USERNAME_AVAILABLE);
			pstmt.setString(1, username);
			ResultSet result = pstmt.executeQuery();
			conn.commit();
			if (result.next())
				return false;
			else
				return true;
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
		return false;
	}
	
	/**
	 * This method checks whether a desired username is valid or not. Usernames are
	 * restricted in the following ways: Can contain lower and upper case Latin
	 * letters. Can contain numbers. Cannot contain any whitespace or symbols.
	 * Cannot be shorter than 2 characters or longer than 15.
	 * 
	 * @param username
	 *            is the desired username chosen by the user
	 * @return true if the desired username matches the pattern defining what is a
	 *         valid username; false, otherwise.
	 */
	public static boolean checkValidUsername(String username) {
		final String USERNAME_PATTERN = "[a-zA-Z0-9]{2,15}$";
		Pattern pattern = Pattern.compile(USERNAME_PATTERN);
		Matcher matcher = pattern.matcher(username);
		boolean isValid = matcher.matches();
		return isValid;
	}
    
	/**
	 * This method checks whether a desired password is valid or not. Passwords are
	 * restricted in the following ways: Must contain at least one upper and one
	 * lower case letter. Must contain at least one number. Must have a minimum
	 * length of 5 and maximum length of 20 characters. Cannot be the same as the
	 * user's username. Passwords are case-sensitive.
	 * 
	 * @param username
	 *            is the username chosen by the user
	 * @param password
	 *            is the password desired by the user
	 * @return true if the desired password is not the same as the username, and
	 *         matches the pattern defining what is a valid password; false,
	 *         otherwise.
	 */
	public static boolean checkValidPassword(String username, String password) {
		if (password.toLowerCase().equals(username.toLowerCase()))
			return false;
		else {
			final String PASSWORD_PATTERN = "^[a-zA-Z0-9]{5,20}$";
			Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
			Matcher matcher = pattern.matcher(password);
			boolean isValid = matcher.matches();
			return isValid;
		}
	}

	/**
	 * This method checks a user's username and password against the database and
	 * verifies them during the log-in process. It first checks the username, and if
	 * that is verified, it then checks the password.
	 * 
	 * @param username
	 *            is the user's username
	 * @param password
	 *            is the user's password
	 * @return an true if the user's credentials are successfully verified; false
	 *         otherwise.
	 */
	public boolean verifyUser(String username, String password) {
		boolean doesUserExist = checkUsernameAvailable(username);
		// if checkUsernameAvailable() == FALSE, then that means username IS in the table
		if (!doesUserExist) {
			// check password
			PreparedStatement pstmt = null;
			final String GET_PASSWORD = "SELECT password FROM users WHERE LOWER(username) LIKE LOWER(?)";
			try {
				pstmt = conn.prepareStatement(GET_PASSWORD);
				pstmt.setString(1, username);
				ResultSet result = pstmt.executeQuery();
				conn.commit();
				if(result.next()) {
					if (result.getString(1).equals(password))
						return true;
					else // password does not match, cannot verify user
						return false;
				} else
					return false;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt != null)
					try {
						pstmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		} else // username does not exist, cannot verify user
			return false;
		return false;
	}

	/**
	 * This method changes a user's username by updating the appropriate object in
	 * the database.
	 * 
	 * @param oldUsername
	 *            is the user's current username
	 * @param newUsername
	 *            is the user's desired new username
	 * @return an int value of 0 if the update is successful; 1 if the desired new
	 *         username already exists (and is therefore unavailable); or 2 if the
	 *         desired username is not valid.
	 */
	public boolean changeUsername(String oldUsername, String newUsername) {
	
		// check if username is available
		boolean isAvailable = checkUsernameAvailable(newUsername);
		if (isAvailable) {
			// change username
			PreparedStatement pstmt = null;
			final String CHANGE_USERNAME = "UPDATE users SET username = ? WHERE LOWER(username) = LOWER(?)";
			try {
				pstmt = conn.prepareStatement(CHANGE_USERNAME);
				pstmt.setString(1, newUsername);
				pstmt.setString(2, oldUsername);
				int done = pstmt.executeUpdate();
				conn.commit();
				if (done > 0)
					return true;
				else
					return false;
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
			} else
				return false;
				// username is not available
		
		return false;
	}
	
	/**
	 * This method changes a user's password by updating the appropriate object in
	 * the database. It is a precondition that the password is valid in terms of its
	 * length and characters.
	 * 
	 * @param username
	 *            is the user's username
	 * @param newPassword
	 *            is the new password that the user wants
	 * @return true if the update is successful; false otherwise.
	 */
	public boolean changePassword(String username, String newPassword) {
		// change password
		PreparedStatement pstmt = null;
		final String CHANGE_PASSWORD = "UPDATE users SET password = ? WHERE LOWER(username) = LOWER(?)";
		try {
			pstmt = conn.prepareStatement(CHANGE_PASSWORD);
			pstmt.setString(1, newPassword);
			pstmt.setString(2, username);
			int done = pstmt.executeUpdate();
			conn.commit();
			if (done > 0)
				return true;
			else
				return false;
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
		return false;
	}

	/**
	 * This method changes a user's avatar by updating the appropriate object in the
	 * database. The avatar is selected by the user via the GUI from a choice of 
	 * ten different avatars.
	 * 
	 * @param username
	 *            is the user's username
	 * @param avatar
	 *            is integer that corresponds to the new avatar that the user wants
	 * @return true if the update is successful; false otherwise.
	 */
	public boolean changeAvatar(String username, int avatar) {
		PreparedStatement pstmt = null;
		final String CHANGE_AVATAR = "UPDATE users SET avatar = ? WHERE LOWER(username) = LOWER(?)";
		try {
			pstmt = conn.prepareStatement(CHANGE_AVATAR);
			pstmt.setInt(1, avatar);
			pstmt.setString(2, username);
			int done = pstmt.executeUpdate();
			conn.commit();
			if (done > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return false;
	}
	

	/**
	 * This method updates a user's total score in the database after they have just
	 * finished a quiz, and will also check to see if they have a new topic high
	 * score and/or a new rank, and update those if need be.
	 * 
	 * @param username
	 *            is the user's username
	 * @param score
	 *            is the score that user has just earned from the quiz they
	 *            completed.
	 * @param topic
	 *            is the topic of the quiz taken by the user
	 * @return true if the player has levelled up (i.e. if their rank has changed);
	 *         false otherwise.
	 */
	public boolean updateScoreAndRank(String username, int score, String topic) {

		String topicColumn = topicToColumn(topic);

		int currentTotalScore = getTotalScore(username);
		int currentHighScore = getTopicHighScore(username, topic);
		int currentRank = getRank(username);

		int newTotalScore = currentTotalScore + score;
		int newRank = User.convertScoreToRank(newTotalScore);

		// if both high score AND rank need updating
		if (score > currentHighScore && newRank > currentRank) {
			PreparedStatement pstmt1 = null;
			final String UPDATE_SCORES = "UPDATE users "
					+ "SET total_score = ?, " + topicColumn + " = ?, rank = ? WHERE LOWER(username) LIKE LOWER(?)";
			try {
				pstmt1 = conn.prepareStatement(UPDATE_SCORES);
				pstmt1.setInt(1, newTotalScore);
				pstmt1.setInt(2, score);
				pstmt1.setInt(3, newRank);
				pstmt1.setString(4, username);
				pstmt1.executeUpdate();
				conn.commit();
				return true; // rank has changed
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt1 != null)
					try {
						pstmt1.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		// if high score has changed but not rank
		} else if (score > currentHighScore && newRank <= currentRank) {
			PreparedStatement pstmt2 = null;
			final String UPDATE_SCORES = "UPDATE users "
					+ "SET total_score = ?, " + topicColumn + " = ? WHERE LOWER(username) LIKE LOWER(?)";
			try {
				pstmt2 = conn.prepareStatement(UPDATE_SCORES);
				pstmt2.setInt(1, newTotalScore);
				pstmt2.setInt(2, score);
				pstmt2.setString(3, username);
				pstmt2.executeUpdate();
				conn.commit();
				return false; // rank has not changed
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt2 != null)
					try {
						pstmt2.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		// if rank has changed but not high score
		} else if (score <= currentHighScore && newRank > currentRank) {
			PreparedStatement pstmt3 = null;
			final String UPDATE_SCORES = "UPDATE users "
					+ "SET total_score = ?, rank = ? WHERE LOWER(username) LIKE LOWER(?)";
			try {
				pstmt3 = conn.prepareStatement(UPDATE_SCORES);
				pstmt3.setInt(1, newTotalScore);
				pstmt3.setInt(2, newRank);
				pstmt3.setString(3, username);
				pstmt3.executeUpdate();
				conn.commit();
				return true; // rank has changed
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt3 != null)
					try {
						pstmt3.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		// if only the user's total (overall) score has changed
		} else {
			PreparedStatement pstmt4 = null;
			final String UPDATE_SCORES = "UPDATE users " + "SET total_score = ? WHERE LOWER(username) LIKE LOWER(?)";
			try {
				pstmt4 = conn.prepareStatement(UPDATE_SCORES);
				pstmt4.setInt(1, newTotalScore);
				pstmt4.setString(2, username);
				pstmt4.executeUpdate();
				conn.commit();
				return false; // rank has not changed
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt4 != null)
					try {
						pstmt4.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
		return false;
	}

	/**
	 * This method gets a user's total (overall) score.
	 * 
	 * @param username
	 *            is the user's username
	 * @return that user's total score.
	 */
	public int getTotalScore(String username) {
		int totalScore = 0;
		PreparedStatement pstmt = null;
		final String GET_TOTAL_SCORE = "SELECT total_score FROM users WHERE LOWER(username) LIKE LOWER(?)";
		try {
			pstmt = conn.prepareStatement(GET_TOTAL_SCORE);
			pstmt.setString(1, username);
			ResultSet result = pstmt.executeQuery();
			conn.commit();
			result.next();
			totalScore = result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return totalScore;
	}
	
	/**
	 * This method gets a user's current high score for a particular topic.
	 * 
	 * @param username
	 *            is the user's username
	 * @param topic
	 *            is the topic in question
	 * @return an int value representing that user's current high score for that
	 *         topic.
	 */
	public int getTopicHighScore(String username, String topic) {
		int highScore = 0;
		PreparedStatement pstmt = null;
		String topicColumn = topicToColumn(topic);
		final String GET_TOPIC_HS = "SELECT " + topicColumn + " FROM users WHERE LOWER(username) LIKE LOWER(?)";
		try {
			pstmt = conn.prepareStatement(GET_TOPIC_HS);
			pstmt.setString(1,  username);
			ResultSet result = pstmt.executeQuery();
			conn.commit();
			result.next();
			highScore = result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return highScore;
	}

	/**
	 * This method gets a user's current avatar as an integer.
	 * 
	 * @param username
	 *            is the user's username
	 * @return an int value representing that user's avatar.
	 */
	public int getAvatar(String username) {
		int avatar = 0;
		PreparedStatement pstmt = null;
		final String GET_AVATAR = "SELECT avatar FROM users WHERE LOWER(username) LIKE LOWER(?)";
		try {
			pstmt = conn.prepareStatement(GET_AVATAR);
			pstmt.setString(1, username);
			ResultSet result = pstmt.executeQuery();
			conn.commit();
			result.next();
			avatar = result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return avatar;
	}

	/**
	 * This method gets a user's current rank as an integer.
	 * 
	 * @param username
	 *            is the user's username
	 * @return an int value representing that user's rank.
	 */
	public int getRank(String username) {
		int rank = 0;
		PreparedStatement pstmt = null;
		final String GET_RANK = "SELECT rank FROM users WHERE LOWER(username) LIKE LOWER(?)";
		try {
			pstmt = conn.prepareStatement(GET_RANK);
			pstmt.setString(1, username);
			ResultSet result = pstmt.executeQuery();
			conn.commit();
			result.next();
			rank = result.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return rank;
	}

	/**
	 * This method creates a Map to be used in the construction of a leaderboard of
	 * a specific quiz topic's standings. The user can specify the number of rows
	 * he/she wants in the leaderboard.
	 * 
	 * @param is
	 *            the topic desired
	 * @param numberOfRows
	 *            is the number of rows of users desired on the board.
	 * @return a map with a key of type Integer (representing whether the user is
	 *         1st place, 2nd place, etc.) and a value of type User (which is an
	 *         object containing the user's avatar, username, rank and high score,
	 *         in that order).
	 */
	public Map<Integer, User> getTopicLeaderboard(String topic, int numberOfRows) {
		String topicColumn = topicToColumn(topic);
		System.out.println(topicColumn);
		Map<Integer, User> board = new HashMap<>();
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		final String GET_BOARD = "SELECT avatar, username, rank, " + topicColumn + " FROM users ORDER BY "
				+ topicColumn + " DESC LIMIT ?";
		try {
			pstmt1 = conn.prepareStatement(GET_BOARD);
			pstmt1.setInt(1, numberOfRows);
			ResultSet resultCheck = pstmt1.executeQuery();
			conn.commit();
			int counter = 0;
			while (resultCheck.next()) {
				counter++;
			}
			if (counter < numberOfRows) // if there aren't enough users in the database, just populate table with as
										// many as possible
				numberOfRows = counter;
			try {
				pstmt2 = conn.prepareStatement(GET_BOARD);
				pstmt2.setInt(1, numberOfRows);
				ResultSet result = pstmt2.executeQuery();
				conn.commit();
				for (int i = 0; i < numberOfRows; i++) {
					result.next();
					int avatar = result.getInt(1);
					String username = result.getString(2);
					int rank = result.getInt(3);
					int highScore = result.getInt(4);
					User userObj = new User(avatar, username, rank, highScore);
					board.put(i + 1, userObj);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt2 != null)
					try {
						pstmt2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt1 != null)
				try {
					pstmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return board;
	}
	
	/**
	 * This method creates a Map to be used in the construction of a leaderboard of
	 * the overall standings. The user can specify the number of rows he/she wants
	 * in the leaderboard.
	 * 
	 * @param numberOfRows
	 *            is the number of rows of users desired on the board.
	 * @return a map with a key of type Integer (representing whether the user is
	 *         1st place, 2nd place, etc.) and a value of type User (which is an
	 *         object containing the user's avatar, username, rank and total score,
	 *         in that order).
	 */
	public Map<Integer, User> getMainLeaderboard(int numberOfRows) {
		Map<Integer, User> board = new HashMap<>();
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		final String GET_BOARD = "SELECT avatar, username, rank, total_score FROM users ORDER BY total_score DESC LIMIT ?";
		try {
			pstmt1 = conn.prepareStatement(GET_BOARD);
			pstmt1.setInt(1, numberOfRows);
			ResultSet resultCheck = pstmt1.executeQuery();
			conn.commit();
			int counter = 0;
			while (resultCheck.next()) {
				counter++;
			}
			if (counter < numberOfRows) // if there aren't enough users in the database, just populate table with as
										// many as possible
				numberOfRows = counter;
			try {
				pstmt2 = conn.prepareStatement(GET_BOARD);
				pstmt2.setInt(1, numberOfRows);
				ResultSet result = pstmt2.executeQuery();
				conn.commit();
				for (int i = 0; i < numberOfRows; i++) {
					result.next();
					int avatar = result.getInt(1);
					String username = result.getString(2);
					int rank = result.getInt(3);
					int highScore = result.getInt(4);
					User userObj = new User(avatar, username, rank, highScore);
					board.put(i + 1, userObj);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (pstmt2 != null)
					try {
						pstmt2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt1 != null)
				try {
					pstmt1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return board;
	}

	/**
	 * Helper method. This method takes a String representing a topic from the
	 * question_bank table, and gets the abbreviation of that string from the topics
	 * table. The abbreviation has no white space and can be used to access a user's
	 * high score for that particular topic in the user table.
	 * 
	 * @param topic
	 *            is the String topic title
	 * @return a String abbreviation of that topic
	 */
	public String topicToColumn(String topic) {
		String columnName = null;
		PreparedStatement pstmt = null;
		final String GET_COLUMN = "SELECT abbreviation FROM topics WHERE topic = ?";
		try {
			pstmt = conn.prepareStatement(GET_COLUMN);
			pstmt.setString(1, topic);
			ResultSet result = pstmt.executeQuery();
			conn.commit();
			result.next();
			columnName = result.getString(1);
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
		return columnName;
	}

}
