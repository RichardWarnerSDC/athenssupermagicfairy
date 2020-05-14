package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.postgresql.util.PSQLException;

import general.Question;
import general.User;

/**
 * Prototype JDBC.
 * This class establishes a connection with the Team Athens database.
 * It utilises the PostgreSQL JDBC Driver 42.2.1.
 * 
 * @author Ashton Hills
 */
public class JDBC {

	private static Connection conn;
	/**
	 * This is a method that establishes a connection with the database.
	 * @throws SQLException
	 * @throws IOException 
	 */
	public static void connectToDatabase() throws IOException{
		try {
	        FileInputStream stream = new FileInputStream("database.properties");
	        Properties props = new Properties();
	        props.load(stream);
	        stream.close();
	        String url = props.getProperty("url");
	        String driver = props.getProperty("jdbc.drivers");
	        String user = props.getProperty("user");
	        String password = props.getProperty("password");
			Class.forName(driver);
			System.out.println("PostgreSQL driver successfully registered.");
			conn = DriverManager.getConnection(url, user, password);
			conn.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			System.out.println("Driver not found.");
//		} catch (PSQLException e) {
//			System.out.println("Failed to connect to the database.");
//		} catch (IOException e) {
//			System.out.println("Failed to connect to the database.");
//		} catch (SQLException e) {
//			System.out.println("Failed to connect to the database.");
		} catch (Exception e) {
			System.out.println("Failed to connect to the database.");
			throw new IOException();
		}
		if (conn != null)
			System.out.println("Connection successful.");
		else
			System.out.println("Connection failed.");
	}


	public static Connection getConn() {
		return conn;
	}

//	public static void main(String[] args) {
//		Queries queries = new Queries();
//		//connectToDatabase();
//		// System.out.println("\n" + getConn() + "\n");
//		// System.out.println(queries.isCorrectAnswer("What is the amortized complexity for an ArrayList?", "O(lala)"));
//		// System.out.println(queries.verifyUser("", ""));
//		// System.out.println(queries.checkUsernameAvailable("tom"));
//		// System.out.println(queries.checkValidUsername("ashtonH1lls"));
//		// System.out.println(queries.checkValidPassword("ashtonH1lls95",
//		// "1l0veJava!"));
////
//		Map<Integer, User> test = queries.getTopicLeaderboard("Data Structures", 3);
//		for (int i = 1; i <= 3; i++) {
//			System.out.println((i) + " | " + test.get(i).printLeaderboard());
//		}
//		
////		List<Question> testList = queries.createQuiz("Data Structures", 1);
////		for (int i = 0; i < 3; i++) {
////		System.out.println((i) + " | " + testList.get(i).toString());
////	}
//		
//	}
	
}