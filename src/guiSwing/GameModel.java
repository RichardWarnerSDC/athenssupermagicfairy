package guiSwing;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import general.Question;
import general.User;

import static java.lang.Thread.sleep;

/**
 * Here we implement a Model Class for our game which we use to alter the
 * current GUI.
 */
public class GameModel extends Observable {

	protected User user;
	protected User playerClaimed;
	protected Map<User, Integer> usersInLobby;
	protected Question currentQuestion;
	protected User[] leaders;
	protected String gameType = "Software Workshop";

	/*
	 * These variables describe what screen you are in.
	 */
	protected boolean welcomeMenu;
	protected boolean loginMenu;
	protected boolean registerMenu;
	protected boolean mainMenu;
	protected boolean editMenu;
	protected boolean helpMenu;
	protected boolean lobbyMenu;
	protected boolean gameMenu;
	protected boolean midRoundMenu;
	protected boolean leaderboard;

	protected int removedButton;
	protected boolean invalidUsername;
	protected boolean countdown;
	protected boolean gameOver;
	protected int background;


	/**
	 * Constructor for the Model Object. We set the WelcomeMenu to be true here.
	 */
	public GameModel() {
		setBooleansFalse();
		welcomeMenu = true;
		removedButton = -1;
		background = 3;
	}

	/**
	 * This method sets all of the booleans to false for convenience so we have
	 * less to type.
	 */
	public void setBooleansFalse() {
		welcomeMenu = false;
		loginMenu = false;
		registerMenu = false;
		mainMenu = false;
		editMenu = false;
		helpMenu = false;
		lobbyMenu = false;
		invalidUsername = false;
		gameMenu = false;
		midRoundMenu = false;
		leaderboard = false;
		countdown = false;
		gameOver = false;
	}

	/**
	 * This method is for getting the user into the Login menu.
	 */
	public void enterLoginMenu() {
		setBooleansFalse();
		user = null;
		loginMenu = true;
		setChanged();
		notifyObservers();
	}

	/**
	 * This method is for returning the user to the Welcome page.
	 */
	public void enterWelcomePage() {
		setBooleansFalse();
		welcomeMenu = true;
		user = null;
		setChanged();
		notifyObservers();
	}

	public void enterMainMenu(User user) {
		this.user = user;
		usersInLobby = new HashMap<User, Integer>();
		setBooleansFalse();
		mainMenu = true;
		setChanged();
		notifyObservers();
	}

	/**
	 * This method is for getting the user into the Register menu.
	 * 
	 * IF badRegister is true then a JFrame will pop up telling the user that
	 * they have used an invalid username that already exists
	 */
	public void enterRegisterMenu(boolean badRegister) {
		if (badRegister) {
			setBooleansFalse();
			registerMenu = true;
			setChanged();
			notifyObservers();
		} else {
			setBooleansFalse();
			registerMenu = true;
			invalidUsername = true;
			setChanged();
			notifyObservers();
		}
	}

	/**
	 * Method for allowing access to the Edit Player Info Menu
	 */
	public void enterEditInfoMenu() {
		setBooleansFalse();
		editMenu = true;
		setChanged();
		notifyObservers();
	}

	public void enterHelpMenu() {
		setBooleansFalse();
		helpMenu = true;
		setChanged();
		notifyObservers();
	}

	/**
	 * This method is for changing the loginMenu to false and the MainMenu to
	 * true.
	 */
	public void loginUser(User newUser) {
		user = newUser;
		setBooleansFalse();
		mainMenu = true;
		setChanged();
		notifyObservers();
	}

	/**
	 * Method allowing the user to join a lobby queue
	 */
	public void enterLobbyQueue(User[] queueing) {
		setBooleansFalse();
		lobbyMenu = true;
	
		usersInLobby = new HashMap<User, Integer>();
		for (int i = 0; i < queueing.length; i++){ 
			if (queueing[i] != null)
				usersInLobby.put(queueing[i], 0);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * This is for removing the user from the lobby.
	 * 
	 * THIS IS WHERE THE PROBLEM IS: YOU DONT REMOVE THE USE ADEQUATELY I THINK.
	 */
	public void removeUserFromLobby(User user) {
		usersInLobby.remove(user);
		
		setChanged();
		notifyObservers();
	}

//	public void joinGame() {
//		this.loginMenu = false;
//		this.registerMenu = false;
//		this.mainMenu = false;
//		this.lobbyMenu = true;
//		this.usersInLobby = new HashMap<User, Integer>();
//		setChanged();
//		notifyObservers();
//	}

	/**
	 * This method joins users to the list of players in the lobby.
	 */
	public void userJoinedLobby(User[] users) {
		usersInLobby = null;
		usersInLobby = new HashMap<User, Integer>();
		for (User u : users) {
			usersInLobby.put(u, 0);
		}
		setChanged();
		notifyObservers();
	}
	
	/**
	 * This method ensures that the user goes to the GameScreen page.
	 */
	public void enterGameScreen() {
		setBooleansFalse();
		removedButton = -1;
		gameMenu = true;
		setChanged();
		notifyObservers();
	}

	/**
	 * This is a method that we can call in order to end a gameLobby and take
	 * the User back to the main page, perhaps in the future this will take us
	 * to a "scores" page which lists all of the scores from that current game.
	 */
	public void endGameLobby() {
		setBooleansFalse();
		this.mainMenu = true;
		usersInLobby = null;
		setChanged();
		notifyObservers();
	}

	public void enterLeaderboards(User[] leaders) {
		setBooleansFalse();
		leaderboard = true;
		this.leaders = leaders;
		setChanged();
		notifyObservers();
	}

	/**
	 * This is a method that we can call to update the HashMap that we have
	 * defined above to store the information about the players in the lobby and
	 * their scores.
	 */
	public void updatePlayerScores(Map<User, Integer> usersInLobby, boolean gameOver) {
		this.usersInLobby = usersInLobby;
		setBooleansFalse();
		midRoundMenu = true;
		this.gameOver = gameOver;
		setChanged();
		notifyObservers();
	}

	/**
	 * This is a method that updates the current question.
	 */
	public void updateQuestion(Question question) {
		setBooleansFalse();
		gameMenu = true;
		currentQuestion = question;
		setChanged();
		notifyObservers();
	}

	/**
	 * Remove the button with the corresponding character in the gamescreen
	 * 
	 * @param ch
	 */
	public void removeButton(int b, User user) {
		setBooleansFalse();
		removedButton = b;
		playerClaimed = user;
		setChanged();
		notifyObservers();
	}

	/**
	 * Starts the countdown on the GUI saying a game is starting and removes the
	 * button allowing the users to leave.
	 */
	public void startCountdown() {
		setBooleansFalse();
		countdown = true;
		setChanged();
		notifyObservers();
	}

	/**
	 * This is a setter of the new background to be used by the User that they
	 * choose from the ComboBox. The index is the number that each background
	 * appears in the comboBox.
	 * 
	 * @param background
	 *            is the corresponding integer to the backgroujnd that the user
	 *            has chosen.
	 */
	public void setBackground(int background) {
		setBooleansFalse();
		this.background = background;
		setChanged();
		notifyObservers();
	}
	
	public User getUser() {
		return user;
	}

}