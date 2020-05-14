package guiTesting;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import general.Question;
import general.User;

/**
 * Here are the tests for the GameModel class found in the guiSwing package. As
 * the fields are protected, we must add the latest GameModel.java file into the
 * guiTesting package. Due to the nature of this class we only test the methods
 * once in most cases as they simply set field variables and will be the same
 * every run of the program.
 * 
 * This has been a slightly awkward class to test, we chose to use a flag and an
 * if statement to check if the field are the same, we do this because we are
 * otherwise unable to set the values of the fields and use a .equals method
 * without adding setters and a .equals method.
 * 
 * @author richard and parker
 *
 * @version 18/03/2018
 */
class GameModelTest {

	private GameModelDummy model;
	private boolean flag;

	@BeforeEach
	public void setUp() {
		model = new GameModelDummy();
		flag = false;
	}

	/*
	 * Test the constructor
	 */
	@Test
	public void test1() {

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the setBooleansFalse()
	 */
	@Test
	public void test2() {
		model.setBooleansFalse();

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterLoginMenu
	 */
	@Test
	public void test3() {
		model.enterLoginMenu();

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterWelcomePage
	 */
	@Test
	public void test4() {
		model.enterWelcomePage();

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterMainMenu
	 */
	@Test
	public void test5() {
		User newUsr = new User("Bill", "Smith", 0, 0);
		model.enterMainMenu(newUsr);

		if (model.user.equals(newUsr) && model.playerClaimed == null
				&& model.usersInLobby.equals(new HashMap<User, Integer>()) && model.currentQuestion == null
				&& model.leaders == null && !model.welcomeMenu && !model.loginMenu && !model.registerMenu
				&& model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu && !model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterRegisterMenu
	 */
	@Test
	public void test6() {
		model.enterRegisterMenu(true);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterRegisterMenu
	 */
	@Test
	public void test7() {
		model.enterRegisterMenu(false);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterEditInfo
	 */
	@Test
	public void test8() {
		model.enterEditInfoMenu();

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterHelpMenu()
	 */
	@Test
	public void test9() {
		model.enterHelpMenu();

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the loginUserMenu()
	 */
	@Test
	public void test10() {
		User newUser = new User("dave", "bottle", 17, 11);
		model.loginUser(newUser);

		if (model.user.equals(newUser) && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterLobbyQueue() - Empty array case
	 */
	@Test
	public void test11() {
		model.enterLobbyQueue(new User[] {});

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(new HashMap<User, Integer>())
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterLobbyQueue() - Single array case
	 */
	@Test
	public void test12() {
		User u1 = new User("a", "b", 0, 11);

		HashMap<User, Integer> mapExpected = new HashMap<User, Integer>();
		mapExpected.put(u1, 0);

		model.enterLobbyQueue(new User[] { u1 });
		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(mapExpected)
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterLobbyQueue() - 3 users array
	 */
	@Test
	public void test13() {
		User u1 = new User("a", "b", 0, 11);
		User u2 = new User("bob", "borris", 76, 66);
		User u3 = new User("Sarah", "Soldering Iron", 0, 57);

		HashMap<User, Integer> mapExpected = new HashMap<User, Integer>();
		mapExpected.put(u1, 0);
		mapExpected.put(u2, 0);
		mapExpected.put(u3, 0);
		model.enterLobbyQueue(new User[] { u1, u2, u3 });
		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(mapExpected)
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the removeUserFromLobby()
	 */
	@Test
	public void test14() {
		User u1 = new User("a", "b", 0, 11);
		User u2 = new User("bob", "borris", 76, 66);
		User u3 = new User("Sarah", "Soldering Iron", 0, 57);

		HashMap<User, Integer> mapExpected = new HashMap<User, Integer>();
		// Remove user u1 from the map
		mapExpected.put(u2, 0);
		mapExpected.put(u3, 0);

		model.enterLobbyQueue(new User[] { u1, u2, u3 });

		model.removeUserFromLobby(u1);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(mapExpected)
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the userJoinedLobby() - empty array
	 */
	@Test
	public void test15() {
		User u1 = new User("a", "b", 0, 11);
		User u2 = new User("bob", "borris", 76, 66);
		User u3 = new User("Sarah", "Soldering Iron", 0, 57);

		HashMap<User, Integer> mapExpected = new HashMap<User, Integer>();
		mapExpected.put(u1, 0);
		mapExpected.put(u2, 0);

		model.enterLobbyQueue(new User[] { u1, u2 });

		model.userJoinedLobby(new User[] {});

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(mapExpected)
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the userJoinedLobby() - single element array
	 */
	@Test
	public void test16() {
		User u1 = new User("a", "b", 0, 11);
		User u2 = new User("bob", "borris", 76, 66);
		User u3 = new User("Sarah", "Soldering Iron", 0, 57);

		HashMap<User, Integer> mapExpected = new HashMap<User, Integer>();
		mapExpected.put(u1, 0);
		mapExpected.put(u2, 0);
		mapExpected.put(u3, 0);

		model.enterLobbyQueue(new User[] { u1, u2 });

		model.userJoinedLobby(new User[] { u3 });

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(mapExpected)
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the userJoinedLobby() - multiple element array.
	 */
	@Test
	public void test17() {
		User u1 = new User("a", "b", 0, 11);
		User u2 = new User("bob", "borris", 76, 66);
		User u3 = new User("Sarah", "Soldering Iron", 0, 57);

		HashMap<User, Integer> mapExpected = new HashMap<User, Integer>();
		mapExpected.put(u1, 0);
		mapExpected.put(u2, 0);
		mapExpected.put(u3, 0);

		model.enterLobbyQueue(new User[] { u2 });

		model.userJoinedLobby(new User[] { u1, u3 });

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(mapExpected)
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && model.lobbyMenu
				&& !model.gameMenu && !model.midRoundMenu && !model.leaderboard && model.removedButton == -1
				&& !model.invalidUsername && !model.countdown && !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterGameScreen
	 */
	@Test
	public void test18() {
		model.enterGameScreen();

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the endGameLobby()
	 */
	@Test
	public void test19() {
		model.endGameLobby();

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(new HashMap<User, Integer>())
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterLeaderboards() - empty array
	 */
	@Test
	public void test20() {
		User[] leaders = new User[] {};
		User[] expected = new User[] {};
		model.enterLeaderboards(leaders);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && Arrays.deepEquals(leaders, expected) && !model.welcomeMenu
				&& !model.loginMenu && !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu
				&& !model.lobbyMenu && !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the enterLeaderboards() - multiple elements array
	 */
	@Test
	public void test21() {
		User u1 = new User("a", "b", 0, 11);
		User u2 = new User("bob", "borris", 76, 66);
		User u3 = new User("Sarah", "Soldering Iron", 0, 57);

		User[] leaders = new User[] { u1, u2, u3 };
		User[] expected = new User[] { u1, u2, u3 };
		model.enterLeaderboards(leaders);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && Arrays.deepEquals(leaders, expected) && !model.welcomeMenu
				&& !model.loginMenu && !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu
				&& !model.lobbyMenu && !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the updatePlayerScores
	 */
	@Test
	public void test22() {
		HashMap<User, Integer> scores = new HashMap<User, Integer>();
		boolean b = true;

		model.updatePlayerScores(scores, b);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(new HashMap<User, Integer>())
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the updatePlayerScores
	 */
	@Test
	public void test23() {
		HashMap<User, Integer> scores = new HashMap<User, Integer>();
		boolean b = false;
		User u1 = new User("a", "b", 0, 11);
		User u2 = new User("bob", "borris", 76, 66);
		User u3 = new User("Sarah", "Soldering Iron", 0, 57);
		scores.put(u1, -34);
		scores.put(u2, 437);
		scores.put(u3, 76);

		HashMap<User, Integer> expected = new HashMap<User, Integer>();
		expected.put(u1, -34);
		expected.put(u2, 437);
		expected.put(u3, 76);

		model.updatePlayerScores(scores, b);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby.equals(expected)
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the updateQuestion()
	 */
	@Test
	public void test24() {
		Question q = new Question("What is life", new String[] { "Shrek", "Rocket League" }, 1);

		Question expected = new Question("What is life", new String[] { "Shrek", "Rocket League" }, 1);

		model.updateQuestion(q);

		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion.toString().equals(expected.toString()) && model.leaders == null
				&& !model.welcomeMenu && !model.loginMenu && !model.registerMenu && !model.mainMenu && !model.editMenu
				&& !model.helpMenu && !model.lobbyMenu && !model.lobbyMenu && model.gameMenu && !model.midRoundMenu
				&& !model.leaderboard && model.removedButton == -1 && !model.invalidUsername && !model.countdown
				&& !model.gameOver && model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}

	/*
	 * Test the removeButton()
	 */
	@Test
	public void test25() {
		model.removeButton(2, new User("Billy", "Bob", 0, 0));

		User playerExpected = new User("Billy", "Bob", 0, 0);
		
		
		if (model.user == null && model.playerClaimed.equals(playerExpected) && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == 2 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}
	
	/*
	 * Test the startCountdown()
	 */
	@Test
	public void test26() {
		model.startCountdown();
		
		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && model.countdown && !model.gameOver
				&& model.background == 0) {
			flag = true;
		}

		assertTrue(flag);
	}
	
	/*
	 * Test the setBackground()
	 */
	@Test
	public void test27() {
		int actual = 6;
		int expected = 6;
		
		model.setBackground(actual);
		
		if (model.user == null && model.playerClaimed == null && model.usersInLobby == null
				&& model.currentQuestion == null && model.leaders == null && !model.welcomeMenu && !model.loginMenu
				&& !model.registerMenu && !model.mainMenu && !model.editMenu && !model.helpMenu && !model.lobbyMenu
				&& !model.lobbyMenu && !model.gameMenu && !model.midRoundMenu && !model.leaderboard
				&& model.removedButton == -1 && !model.invalidUsername && !model.countdown && !model.gameOver
				&& model.background == expected) {
			flag = true;
		}

		assertTrue(flag);
	}
	

}