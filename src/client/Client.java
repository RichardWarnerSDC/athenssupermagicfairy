package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import general.Message;
import general.Question;
import general.User;
import guiSwing.ViewDriver;
import server.ServerThread.ComProtocolServer;

/**
 * Here is the class which manages communication between the server, on the
 * client side. We note that this Client class created a ClientListenThread
 * which we use to constantly listen to the Server class, and this Client class
 * is used to send information to the Server.
 * 
 * @author parker, Team Athens
 * 
 * @version 28/02/2018
 */
public class Client extends Thread {

	private static final int PORT = 50000; // Port can be changed as long as
	// server changes it too
	// boolean to be false.
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Message messageFrom;
	private Socket server;
	private boolean finished; // Should be false while running, true to end
	private ViewDriver gui;

	/**
	 * Here is a constructor for the Client Object, which opens a socket and
	 * created a connection between the server.
	 * 
	 * @param serverName
	 *            is the String of the server, then it opens a socket connection
	 *            to the server.
	 */
	public Client(String serverName) {
		super();
		try {
			finished = false;
			server = new Socket(serverName, PORT);
			toServer = new ObjectOutputStream(server.getOutputStream());
			fromServer = new ObjectInputStream(server.getInputStream());

			gui = new ViewDriver(this);

			this.start();// Thread
		} catch (UnknownHostException e) {
			System.out.println("There is an Unknown Host : " + serverName);
			System.out.println("************************");
			System.out.println("Correct usage is : java Client <servername>");
		} catch (IOException e) {
			System.out.println("There was no I/O for the connection to " + serverName);
		}
	}

	/**
	 * This is to listen to the server and convert what the Server sends to this
	 * class in the form of a Message Object.
	 */
	@Override
	public void run() {
		try {
			while (!finished) {
				try {
					messageFrom = (Message) fromServer.readObject(); // Get the
					// message
					ComProtocolClient cpc = new ComProtocolClient();
					cpc.incomingCom(messageFrom);
				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.out.println("Client disconnects from server.");
			gui.errorFrameCrash();
		} finally {
			finished = true;
			finalize();
		}
	}

	/**
	 * Setter in which we set the runningStatus to false.
	 */
	public void endClient() {
		finished = true;
	}

	/**
	 * This is a message that is sent to the Server when a User wishes to login
	 * to the Server.
	 * 
	 * @param user
	 *            is the User object with the information about the attempted
	 *            Connect from the user.
	 */
	public void sendConnectMessage(User user) {
		try {
			Message toSend = new Message(ComProtocolServer.CONNECT, user, null, null, null, -1, null);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This method sends a Message to the Server, so that the User can be added
	 * to the database, if the user is already in the database, then they cannot
	 * be added.
	 * 
	 * JComboBox Note we get a response from the server which says OK! if the
	 * user is successfully added to the database. We should get a message
	 * ERROR! if the user is already in the database.
	 * 
	 * The header of the message should ALWAYS be 1.
	 * 
	 * @param message
	 *            is the message that contains the information about the User
	 *            object that we want to register into our database.
	 */
	public void sendRegisterMessage(User user) {
		try {
			Message toSend = new Message(ComProtocolServer.REGISTER, user, null, null, null, -1, null);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This is the message that we send to the server to notify the serverThread
	 * that we have disconnected a user.
	 * 
	 * @param user
	 *            is the user that is logging out.
	 */
	public void sendDisconnectMessage(User user) {
		try {
			Message toSend = new Message(ComProtocolServer.DISCONNECT, user, null, null, null, -1, null);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			finished = true;
		}
	}

	/**
	 * This is the message that we send to the server to notify them that we
	 * wish to join a lobby.
	 * 
	 * @param user
	 *            is the user that wishes to join a lobby.
	 */
	public void sendPlayMessage(User user, String gameType) {
		try {
			Message toSend = new Message(ComProtocolServer.PLAY, user, gameType, null, null, -1, null);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This is the message that we send to the server to notify them that we
	 * have left the lobby.
	 * 
	 * @param user
	 *            is the User that is leaving the lobby.
	 */
	public void sendQuitLobbyMessage(User user) {
		try {
			Message toSend = new Message(ComProtocolServer.QUIT, user, null, null, null, -1, null);
			toServer.writeObject(toSend);
			toServer.flush();
			System.out.println("WE SEND QUIT LOBBY MESSAGE");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This is the method that we call to notify the server that the user wishes
	 * to change their information.
	 * 
	 * @param user
	 *            is the user that wishes to change their info.
	 */
	public void sendChangeInfoMessage(User user) {
		try {
			Message toSend = new Message(ComProtocolServer.CHANGE_INFO, user, null, null, null, -1, null);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This is the method called to notify the server that the user has answered
	 * a question.
	 * 
	 * @param user
	 *            is the user that answers a question.
	 * @param answer
	 *            is the answer provided by the user.
	 */
	public void sendAnswerMessage(User user, int answer) {
		try {
			Message toSend = new Message(ComProtocolServer.ANSWER, user, null, null, null, answer);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This is a message that we send to the Server to request information about
	 * the Leaderboards so they can be populated and displayed on the GUI.
	 */
	public void sendLeaderboardMessage() {
		try {
			Message toSend = new Message(ComProtocolServer.LEADERBOARD, null, null, null, null, -1);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This is the message that is sent to the Client when the User user
	 * requests to logout from the Server.
	 * 
	 * @param user
	 *            is the user wishing to logout.
	 */
	public void sendLogoutMessage(User user) {
		try {
			Message toSend = new Message(ComProtocolServer.LOGOUT, user, null, null, null, -1);
			toServer.writeObject(toSend);
			toServer.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public boolean getFinished() {
		return this.finished;
	}

	/**
	 * Here is a method that we use to destroy the connection at the client
	 * side. Thus, this method should be called when the client is closed.
	 * 
	 * Firstly, we let the server know that we are finished using it.
	 */
	public void finalize() {
		try {
			/*
			 * Let the server know that we are closing the connection.
			 */
			toServer.writeInt(0);

			/*
			 * Next we close the streams
			 */
			toServer.close();
			fromServer.close();

			/*
			 * Close the Connection
			 */
			server.close();
		} catch (IOException e) {
			System.out.println("The client has not ended correctly, please look into this.");
		}
	}

	public class ComProtocolClient {
		/**
		 * Beneath are all of the possible protocols that could be called.
		 */
		// Player successfully signed in
		public static final int GOOD_SIGN_IN = 0;
		// Player unsuccessful sign in e.g. password is wrong or username does
		// not exist if(this.isStarted)
		public static final int BAD_SIGN_IN = 1;
		// successful sign up e.g. username exists
		public static final int GOOD_SIGN_UP = 22;
		// Unsuccessful sign up e.g. username exists
		public static final int BAD_SIGN_UP = 2;
		// Successfully updated player information
		public static final int GOOD_UPDATE = 3;
		// Unsuccessfully updated player information e.g. username exists
		public static final int BAD_UPDATE = 4;
		// Player is queueing
		public static final int QUEUEING = 5;
		// Player joined the lobby
		public static final int PLAYER_JOINED = 6;
		// Another player quit while waiting in the lobby
		public static final int PLAYER_QUIT = 7;
		// Start one minute timer (initiates when a second player has joined).
		public static final int LOBBY_COUNTDOWN = 8;
		// Lobby was disbanded as all other players left
		public static final int GAME_ENDED = 9;
		// Lobby begins count down from 5 to start the game
		public static final int GAME_COUNTDOWN = 10;
		// Lobby has started
		// private static final int START_GAME = 11;
		// Player is send question, the four options and the right answer
		public static final int Q_AND_A = 12;
		// When any player first claims an option. This initiates a countdown
		public static final int FIRST_PLAYER_CLAIMED = 13;
		// When any player claims an option (who's not the first to do so)
		public static final int PLAYER_CLAIMED = 14;
		// Update the game kernel information after every round such as client
		// score
		public static final int OPTION_TAKEN = 18;
		// ADD LATER
		public static final int UPDATE = 15;
		// When some interruption occurs like a client unexpectedly disconnects
		public static final int INTERRUPTION = 16;
		// Player is send the results to be displayed (including bonus points).
		public static final int RESULTS = 17;

		public static final int LEADERBOARD = 8888;

		public static final int LOGOUT = 4040;

		/**
		 * The processInput method takes a String, checks if it is in the
		 * correct form and processes the request according to it's header (the
		 * header is just the first two characters of the string).
		 * 
		 * @param message
		 *            - This is the request which should start with digits
		 *            between 00 and 14.
		 * @return some string, depending on the GOOD_SIGN_INrequest. If the
		 *         client requests some information, then we return some string.
		 *         Otherwise, we return an empty string.
		 */
		public Message incomingCom(Message message) {
			int header = message.getHeader();
			System.out.println(header);
			if (header == GOOD_SIGN_IN) {
				gui.getModel().loginUser(message.getUser());
				return message;
			} else if (header == BAD_SIGN_IN) {
				gui.userWrongError();
				return new Message(0, null, "Sign in unsuccessful", null, null, -1, null);
			} else if (header == GOOD_SIGN_UP) {
				gui.userSignUpGood();
				gui.getModel().enterLoginMenu();
				return new Message(0, null, "Sign up successful", null, null, -1, null);
			} else if (header == BAD_SIGN_UP) {
				gui.userExistsError();
				return new Message(0, null, "User already exists", null, null, -1, null);
			} else if (header == GOOD_UPDATE) {
				gui.getModel().enterMainMenu(message.getUser());
				return new Message(0, new User("Ed", "", User.IDLE, User.EXPERIENCED), "some message", null, null, -1,
						null);
			} else if (header == BAD_UPDATE) {
				gui.userExistsError();
				return new Message(0, null, "some message", null, null, -1, null);
			} else if (header == QUEUEING) {
				System.out.println("WE RECEIVE A QUEUEING METHOD");
				if (message.getText().equals("Someone quits in the countdown")) {
					gui.getModel().deleteObservers();
					gui.addThisObserver();
					User[] tmp = message.getQueueing();
					for (int i = 0; i < tmp.length - 1; i++) {
						if (tmp[i].getUsername().equals(gui.getModel().getUser().getUsername())) {
							tmp[i] = null;
						}
					}
					gui.getModel().enterLobbyQueue(tmp);
				} else
					gui.getModel().enterLobbyQueue(message.getQueueing());
				return new Message(0, null, "You are waiting in the lobby!", null, null, -1, null);
			} else if (header == PLAYER_JOINED) {
				gui.addThisObserver();
				gui.getModel().userJoinedLobby(message.getQueueing());
				return new Message(0, new User(null, null, User.IDLE, User.EXPERIENCED), "Player joined lobby!", null,
						null, -1, null);
			} else if (header == PLAYER_QUIT) {
				if (message.getUser().equals(gui.getModel().getUser())) {
					gui.getModel().deleteObservers();
					gui.addThisObserver();
					gui.getModel().enterMainMenu(gui.getModel().getUser());
				} else
					gui.getModel().removeUserFromLobby(message.getUser());
				return new Message(0, message.getUser(),
						"Player " + message.getUser().getUsername() + " has left the lobby!", null, null, -1, null);
			} else if (header == LOBBY_COUNTDOWN) {
				
				return new Message(0, null, "some message", null, null, -1, null);
			} else if (header == GAME_ENDED) {
				gui.addThisObserver();
				gui.getModel().endGameLobby();
				return new Message(0, null, "All players left :(", null, null, -1, null);
			} else if (header == GAME_COUNTDOWN) {
				gui.getModel().startCountdown();
				return new Message(0, null, "Lobby starting in...", null, null, -1, null);
			} else if (header == Q_AND_A) {
				gui.addThisObserver();
				gui.getModel().enterGameScreen();
				String question = message.getQuestion();
				String[] options = message.getOptions();
				int answer = message.getAnswer();
				Question q = new Question(question, options, answer);
				gui.getModel().updateQuestion(q);
				return new Message(0, null, null, "How large am I", null, 0, null);
			} else if (header == FIRST_PLAYER_CLAIMED) {
				gui.getModel().removeButton(message.getAnswer(), message.getUser());
				return new Message(0, null, "First player has claimed! Timer will start", null, null, -1, null);
			} else if (header == PLAYER_CLAIMED) {
				gui.getModel().removeButton(message.getAnswer(), message.getUser());
				return new Message(0, null, "Player has claimed!", null, null, -1, null);
			} else if (header == OPTION_TAKEN) {
				// This header is also used if a user tries to claim more than
				// one option. Perhaps we should remove all buttons once a user
				// selects an option?
				return new Message(0, null, "This option has already been claimed!", null, null, -1, null);
			} else if (header == UPDATE) {
				gui.addThisObserver(); // make it an observer again.
				gui.getModel().updatePlayerScores(message.getScores(), false);
				return new Message(0, null, "Update of scores so far", null, null, -1, null);
			} else if (header == INTERRUPTION) {
				// TODO When some interruption occurs like a client unexpectedly
				// disconnects
				return new Message(0, null, "Interruption from other players!", null, null, -1, null);
			} else if (header == RESULTS) {
				gui.addThisObserver();
				gui.getModel().updatePlayerScores(message.getScores(), true);
				return new Message(0, null, "The results are in...", null, null, -1, null);
			} else if (header == LEADERBOARD) {
				gui.getModel().enterLeaderboards(message.getQueueing());
				return new Message(0, null, "We get leaderboard information " + message, null, null, -1, null);
			} else if (header == LOGOUT) {
				gui.getModel().enterWelcomePage();
				System.out.println("LOGOUT MESSAGE RECIEVED");
				return new Message(0, null, "we logout from the page " + message, null, null, -1, null);
			}
			return new Message(0, null, "Invalid input from server: " + message, null, null, -1, null);
		}
	}

	/**
	 * Main method simply for testing , NOT how it is to be actually
	 * implemented.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Client("localhost");// "147.188.195.139");
	}

}