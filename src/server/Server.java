package server;

import java.net.*;
import java.util.Iterator;
import java.util.concurrent.*;

import database.Queries;
import general.User;

import java.io.*;

import static java.lang.Thread.sleep;


public class Server {
	private int userID = 0;
	public static LinkedBlockingQueue<User> activeUserList;
	public static LinkedBlockingQueue<Lobby> gameList;
	public static Object userLock= new Object();
	public static Object gameLock = new Object();
	public static ExecutorService pool;

	// Server Configuration

	public static int roundDuration = 15 * 1000;
	public static int roundBreak = 5 * 1000;
	public static int gameStartCountDown = 5 * 1000;
	public static int lobbyMaxPlayer = 3;

	// Server Configuration

	//Player successfully signed inr
	public static final int GOOD_SIGN_IN			= 0;
	//Player unsuccessful sign in e.g. password is wrong or username does not exist
	public static final int BAD_SIGN_IN			= 1;
	//Successful sign up
	public static final int GOOD_SIGN_UP = 22;
	//Unsuccessful sign up e.g. username exists
	public static final int BAD_SIGN_UP			= 2;
	//Successfully updated player information
	public static final int GOOD_UPDATE			= 3;
	//Unsuccessfully updated player information e.g. username exists
	public static final int BAD_UPDATE				= 4;
	//Player is queueing
	public static final int QUEUEING				= 5;
	//Player joined the lobby
	public static final int PLAYER_JOINED			= 6;
	//Another player quit while waiting in the lobby
	public static final int PLAYER_QUIT			= 7;
	//Start one minute timer (initiates when a second player has joined).
	public static final int LOBBY_COUNTDOWN		= 8;
	//Lobby was disbanded as all other players left
	public static final int GAME_ENDED				= 9;
	//Lobby begins count down from 5 to start the game
	public static final int GAME_COUNTDOWN			= 10;
	//Lobby has started
	//private static final int START_GAME				= 11;
	//Player is send question, the four options and the right answer
	public static final int Q_AND_A				= 12;
	//When any player first claims an option. This initiates a countdown
	public static final int FIRST_PLAYER_CLAIMED	= 13;
	//When any player claims an option (who's not the first to do so)
	public static final int PLAYER_CLAIMED			= 14;
	//Update the game kernel information after every round such as client score
	public static final int OPTION_TAKEN			= 18;
	//ADD LATER
	public static final int UPDATE					= 15;
	//When some interruption occurs like a client unexpectedly disconnects
	public static final int INTERRUPTION			= 16;
	//Player is send the results to be displayed (including bonus points).
	public static final int RESULTS				= 17;
	
	public Server() {
		this.gameList = new LinkedBlockingQueue<>();
		this.activeUserList = new LinkedBlockingQueue<>();
		this.pool = Executors.newCachedThreadPool();
	}

	/**
	 * Server starting method
	 * @param port is the server port
	 */
	public void startServer(int port){
		pool.execute(new ServerMonitor());
		try (ServerSocket serverSocket = new ServerSocket(port)){
			while (true)
				pool.execute(new ServerThread(serverSocket.accept()));
		} catch (IOException e) {
			System.err.println("Couldn't listen on port: 50000 or disconnection from DB.");
			System.exit(-1);
		}
	}

	/**
	 * Check if the user is in the Server's onlineUser List
	 * @param user is the user being checked
	 * @return a boolean value indicates if the user is in the list
	 */
	public static boolean containsUser(User user){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();)
				if(it.next().equals(user))
					return true;
		}
		return false;
	}

	/**
	 * Check if the user is in the Server's onLineUser List
	 * if exists, return that user object, otherwise return null
	 * @param user is the user being checked
	 * @return the user object which might be null
	 */
	public static User findUser(User user){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();){
				User u = it.next();
				if(u.equals(user))
					return u;
			}
		}
		return null;
	}

	/**
	 * Set a user's status in the online user list
	 * @param user is the user whose status will be changed
	 * @param newStatus is the new status
	 */
	public static void setUserStatus(User user, int newStatus){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();){
				User u = it.next();
				if(u.equals(user))
					u.setStatus(newStatus);
			}
		}
	}

	/**
	 * Set a user's rank in the online user list
	 * @param user is the user whose rank will be changed
	 * @param newRank is the new rank
	 */
	public static void setUserRank(User user, int newRank){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();){
				User u = it.next();
				if(u.equals(user))
					u.setRank(newRank);
			}
		}
	}

	/**
	 * Set a user's score in the online user list
	 * @param user is the user whose score will be changed
	 * @param newScore is the new score
	 */
	public static void setTotalScore(User user, int newScore){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();){
				User u = it.next();
				if(u.equals(user))
					u.setTotalScore(newScore);
			}
		}
	}

	/**
	 * Set a user's avatar in the online user list
	 * @param user is the user whose avatar will be changed
	 * @param newAvatar is the new avatar
	 */
	public static void setAvatar(User user, int newAvatar){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();){
				User u = it.next();
				if(u.equals(user))
					u.setAvatar(newAvatar);
			}
		}
	}

	/**
	 * Set a user's password in the online user list
	 * @param user is the user whose password will be changed
	 * @param newPass is the new password
	 */
	public static void setPassword(User user, String newPass){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();){
				User u = it.next();
				if(u.equals(user))
					u.setPassword(newPass);
			}
		}
	}

	/**
	 * Set a user's username in the online user list
	 * @param user is the user whose username will be changed
	 * @param newUsername is the new username
	 */
	public static void setUsername(User user, String newUsername){
		synchronized (userLock){
			for(Iterator<User> it = activeUserList.iterator(); it.hasNext();){
				User u = it.next();
				if(u.equals(user))
					u.setUsername(newUsername);
			}
		}
	}

	/**
	 * Remove a use from onlineUserList
	 * @param user that is being removed
	 */
	public static void removeUser(User user) { activeUserList.removeIf(u->u.equals(user)); }

	public static void main(String[] args){
		Server s = new Server();
		System.out.println("Server starts...");
		s.startServer(50000);
	}
}
