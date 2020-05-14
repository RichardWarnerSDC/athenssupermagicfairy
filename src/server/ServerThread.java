package server;

import java.net.*;
import java.util.Iterator;
import database.Queries;
import general.Message;
import general.User;
import java.io.*;
import java.util.Map;
import client.Client.ComProtocolClient;

public class ServerThread implements Runnable {
	private Socket client = null;
	private ObjectOutputStream toClient;
	private ObjectInputStream fromClient;
	private User currentUser = null;
	private Queries db;
	private ComProtocolServer proto = new ComProtocolServer();
	private Message message;
	private static boolean running;

	public ServerThread(Socket client) throws IOException {
		this.client = client;
		running = true;
		this.db = new Queries();
	}

	public void run() {
		try {
			toClient = new ObjectOutputStream(client.getOutputStream());
			fromClient = new ObjectInputStream(client.getInputStream());
			listen();
			toClient.close();
			fromClient.close();
			client.close();
		} catch (SocketException e) {
			System.out.println("Client has disconnects from Server.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		try {
			while (running) {
				try {
					message = (Message) fromClient.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if (message == null)
					continue;
				Message toSend = proto.incomingCom(message);
				if (toSend == null)
					continue;
				toClient.writeObject(toSend);
				message = null;
			}
			System.out.println("Client disconnects from Server.(Logout)");
		} catch (IOException e) {
			System.out.println("Client disconnects from Server.(Break)");
		} finally {
			if (this.currentUser == null)
				return;
			synchronized (Server.userLock) {
				Server.removeUser(this.currentUser);
			}
			synchronized (Server.gameLock) {
				for (Iterator<Lobby> it = Server.gameList.iterator(); it.hasNext();) {
					Lobby l = it.next();
					if (l.containUser(this.currentUser))
						l.removeUser(this.currentUser);
				}
			}
		}
	}

	/**
	 * Register handling function
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Message register(String username, String password) {
		try {
			if (!db.checkUsernameAvailable(username)) {
				return new Message(Server.BAD_SIGN_UP, null, "ERROR! Username already exists", null, null, -1);
			} else {
				synchronized (Server.userLock) {
					if (db.registerUser(username, password)) {
						return new Message(ComProtocolClient.GOOD_SIGN_UP, null,
								"OK! New user " + username + " successfully registered.", null, null, -1);
					} else {
						return new Message(ComProtocolClient.BAD_SIGN_UP, null, "Something is wrong", null, null, -1);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Exception 1 has been thrown!");
		}
		return new Message(-9999, null, null, null, null, -1);
	}

	/**
	 * CONNECT message handling function
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Message login(String username, String password) {
		try {
			if (db.verifyUser(username, password)) {
				synchronized (Server.userLock) {
					User newUsr = new User(username, password, User.IDLE, db.getRank(username), db.getAvatar(username),
							db.getTotalScore(username));
					if (Server.containsUser(newUsr)) {
						return new Message(ComProtocolClient.BAD_SIGN_IN, null, "Already Logged in", null, null, -1);
					}
					Server.activeUserList.put(newUsr);
					this.currentUser = newUsr;
					return new Message(ComProtocolClient.GOOD_SIGN_IN, newUsr, "Successfully sign in", null, null, -1);
				}
			} else {
				return new Message(ComProtocolClient.BAD_SIGN_IN, null, "Username or password incorrect", null, null,
						-1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception 2 has been thrown");
		}
		return new Message(ComProtocolClient.BAD_SIGN_IN, null, "Username or password incorrect", null, null, -1);
	}

	/**
	 * DISCONNECT message handling function
	 * 
	 * @param user
	 * @return
	 */
	public String logout(User user) {
		try {
			synchronized (Server.userLock) {
				for (Iterator<User> it = Server.activeUserList.iterator(); it.hasNext();) {
					User u = it.next();
					if (user.equals(u)) {
						it.remove();
					}
				}
			}

			synchronized (Server.gameLock) {
				for (Iterator<Lobby> it = Server.gameList.iterator(); it.hasNext();) {
					Lobby g = it.next();
					if (g.containUser(user))
						g.removeUser(user);
				}
			}

			running = false;
			return "Logout Successfully.";
		} catch (Exception e) {
			;
		}
		return "Something is wrong";
	}

	/**
	 * LOGOUT message handling function
	 * 
	 * @param user
	 *            contains the user's info who sends this request
	 * @return the message text
	 */
	public String logout2(User user) {
		try {
			synchronized (Server.userLock) {
				for (Iterator<User> it = Server.activeUserList.iterator(); it.hasNext();) {
					User u = it.next();
					if (user.equals(u)) {
						it.remove();
					}
				}
			}

			synchronized (Server.gameLock) {
				for (Iterator<Lobby> it = Server.gameList.iterator(); it.hasNext();) {
					Lobby g = it.next();
					if (g.containUser(user))
						g.removeUser(user);
				}
			}
			return "Logout Successfully.";
		} catch (Exception e) {
			;
		}
		return "Something is wrong";
	}

	/**
	 * CHANGE_INFO message handling function
	 * 
	 * @return the message text
	 */
	public String changeInfo(Message message) {
		try {
			db.changeAvatar(currentUser.getUsername(), message.getUser().getAvatar());
			db.changePassword(currentUser.getUsername(), message.getUser().getPassword());
			db.changeUsername(currentUser.getUsername(), message.getUser().getUsername());
			Server.setAvatar(currentUser, message.getUser().getAvatar());
			Server.setPassword(currentUser, message.getUser().getPassword());
			Server.setUsername(currentUser, message.getUser().getUsername());
			currentUser = message.getUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SUCCESSFULLY CHANGE" + message.toString();
	}

	/**
	 * PLAY message handling function
	 * 
	 * @param message
	 *            is the message that contains the request
	 * @return the message text
	 */
	public String play(Message message) throws IOException {
		String topic = message.getText();
		User user = null;
		user = Server.findUser(message.getUser());

		if (user.getStatus() == User.MATCHING)
			return "You are already in the queue.";

		synchronized (Server.gameLock) {
			// Try to let this user join a lobby
			for (Iterator<Lobby> it = Server.gameList.iterator(); it.hasNext();) {
				if (it.next().joinUser(user, topic, this.toClient)) {
					Server.setUserStatus(user, User.MATCHING);
					return "Joined.";
				}
			}

			// If no suitable lobby is found, then create a new one
			Lobby g = new Lobby(topic, Server.roundDuration, Server.roundBreak, Server.gameStartCountDown,
					Server.lobbyMaxPlayer);
			g.joinUser(user, topic, this.toClient);
			Server.gameList.add(g);
			Server.setUserStatus(user, User.MATCHING);

			return "A new Lobby starts.";
		}
	}

	/**
	 * ANSWER message handling function
	 * 
	 * @param message
	 *            is the message that contains the request
	 */
	public void answer(Message message) {
		// User user = message.getUser();
		User user = Server.findUser(message.getUser());
		synchronized (Server.gameLock) {
			Server.gameList.forEach(l -> {
				if (l.containUser(user))
					l.acceptChoice(user, message.getAnswer());
			});
		}
	}

	/**
	 * QUIT message handling function
	 * 
	 * @param message
	 *            is the message that contains the request
	 * @return the message text
	 */
	public String quit(Message message) {
		Server.setUserStatus(message.getUser(), User.IDLE);
		synchronized (Server.gameLock) {
			Server.gameList.forEach(l -> {
				l.removeUser(message.getUser());
			});
		}
		return "User removed";
	}

	/**
	 * LEADERBOARD message handling function
	 * 
	 * @return the message that contains the leaderboard information
	 */
	public Message leaderBoard() {
		Map<Integer, User> global_lb = db.getMainLeaderboard(10);
		Map<Integer, User> SW_lb = db.getTopicLeaderboard("Software Workshop", 10);
		Map<Integer, User> DS_lb = db.getTopicLeaderboard("Data Structures", 10);
		Map<Integer, User> CS_lb = db.getTopicLeaderboard("Computer Science", 10);
		Map<Integer, User> DB_lb = db.getTopicLeaderboard("Databases", 10);
		Map<Integer, User> AI_lb = db.getTopicLeaderboard("Artificial Intelligence", 10);
		Map<Integer, User> OS_lb = db.getTopicLeaderboard("Operating Systems and Networks", 10);
		Map<Integer, User> NI_lb = db.getTopicLeaderboard("Nature Inspired Search and Optimisation", 10);

		User[] users = new User[80];
		for (int i = 1; i <= 10; i++) {
			users[i - 1] = global_lb.get(i);
		}
		for (int i = 11; i <= 20; i++) {
			users[i - 1] = SW_lb.get(i - 10);
		}
		for (int i = 21; i <= 30; i++) {
			users[i - 1] = DS_lb.get(i - 20);
		}
		for (int i = 31; i <= 40; i++) {
			users[i - 1] = CS_lb.get(i - 30);
		}
		for (int i = 41; i <= 50; i++) {
			users[i - 1] = DB_lb.get(i - 40);
		}
		for (int i = 51; i <= 60; i++) {
			users[i - 1] = AI_lb.get(i - 50);
		}
		for (int i = 61; i <= 70; i++) {
			users[i - 1] = OS_lb.get(i - 60);
		}
		for (int i = 71; i <= 80; i++) {
			users[i - 1] = NI_lb.get(i - 70);
		}
		return new Message(ComProtocolServer.LEADERBOARD, null, null, null, null, 0, users);
	}

	/**
	 * Below is an inner class for the communication protocol for the sending
	 * information to the Server.
	 * 
	 * 
	 */

	public class ComProtocolServer {

		// Client tries to sign in
		public static final int CONNECT = 0;
		// Client wants to sign up.
		public static final int REGISTER = 1;
		// Client disconnects with the Server
		public static final int DISCONNECT = 2;
		// Client wants to find a match and sends the topic they have chosen
		// (which are preset)
		public static final int PLAY = 3;
		// Client wants to modify the information (Avatar, password, username)
		// Note this will send a full user
		public static final int CHANGE_INFO = 4;
		// Answer header. Followed by the choice of client choice.
		public static final int ANSWER = 5;
		// Quit lobby
		public static final int QUIT = 9999;
		// Ask for leaderboard
		public static final int LEADERBOARD = 8888;
		// Logout
		public static final int LOGOUT = 4040;

		/**
		 * The processInput method takes a String, checks if it is in the
		 * correct form and processes the request according to it's header (the
		 * header is just the first two characters of the string).
		 * 
		 * @param message
		 *            - This is the request which should start with digits
		 *            between 00 and 14.
		 * @return some string, depending on the request. If the client requests
		 *         some information, then we return some string. Otherwise, we
		 *         return an empty string.
		 * @throws IOException 
		 */
		public Message incomingCom(Message message) throws IOException {
			int header = message.getHeader();
			if (header == CONNECT) {
				return login(message.getUser().getUsername(), message.getUser().getPassword());
			} else if (header == REGISTER) {
				return register(message.getUser().getUsername(), message.getUser().getPassword());
			} else if (header == DISCONNECT) {
				return new Message(DISCONNECT, null, logout(message.getUser()), null, null, -1);
			} else if (header == PLAY) {
				play(message);
				return null;
			} else if (header == CHANGE_INFO) {
				return new Message(0, null, changeInfo(message), null, null, -1);
			} else if (header == ANSWER) {
				answer(message);
				return null;
			} else if (header == QUIT) {
				quit(message);
				return null;
			} else if (header == LEADERBOARD) {
				return leaderBoard();
			} else if (header == LOGOUT) {
				return new Message(LOGOUT, null, logout2(message.getUser()), null, null, -1);
			}
			return new Message(0, null, "Invalid input from client: " + message, null, null, -1);
		}
	}

}
