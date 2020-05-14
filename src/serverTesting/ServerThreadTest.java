package serverTesting;

import database.Queries;
import general.*;
import server.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerThreadTest {
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Socket soc;
	private ObjectOutputStream toServer2;
	private ObjectInputStream fromServer2;
	private Socket soc2;
	private ExecutorService pool;
	private Message m;
	private Message m2;
	private boolean flag;
	private boolean flag2;
	private boolean flag3;
	private boolean flag4;
	private boolean flag5;
	private boolean flag6;
	private boolean flag7;
	private boolean flag8;
	private boolean flag9;
	private boolean flag10;
	private boolean flag11;
	private boolean flag12;
	private boolean flag13;
	private boolean flag14;
	private boolean flag15;
	private boolean flag16;
	private boolean flag17;
	private boolean flag18;
	private boolean flag19;
	private boolean flag20;
	private boolean flag21;
	private boolean flag22;
	private boolean testFlag;
	private int result;
	private static int port = 50000;

	private static final int CONNECT 		= 0;
	private static final int REGISTER		= 1;
	private static final int DISCONNECT		= 2;
	private static final int PLAY			= 3;
	private static final int ANSWER			= 5;
	private static final int QUIT           = 9999;
	public static final int LEADERBOARD = 8888;


    //TEST LOBBY CONFIGURATION:
    //    private final int roundDuration = 3 //* 1000;
    //    private final int roundBreak = 1 //* 1000;
    //    private final int gameStartCountDown = 2 //* 1000;
	//   private final int lobbyMaxPlayers = 2;
    //    Total round: 3
     


	@BeforeEach
	public void setUp(){
		pool = Executors.newCachedThreadPool();
		flag = false;
		flag2 = false;
		flag3 = false;
		flag4 = false;
		flag5 = false;
		flag6 = false;
		flag7 = false;
		flag8 = false;
		flag9 = false;
		flag10 = false;
		flag11 = false;
		flag12 = false;
		flag13 = false;
		flag14 = false;
		flag15 = false;
		flag16 = false;
		flag17 = false;
		flag18 = false;
		flag19 = false;
		flag20 = false;
		flag21 = false;
		flag22 = false;
		testFlag = false;
		Runnable task = new Runnable() {
			@Override
			public void run() {
				Server server = new Server();
				server.startServer(port);
			}
		};
		pool.execute(task);
		try{
			sleep(500);
			soc = new Socket("localhost", port);
			toServer = new ObjectOutputStream(soc.getOutputStream());
			fromServer = new ObjectInputStream(soc.getInputStream());

			sleep(500);
			soc2 = new Socket("localhost", port);
			toServer2 = new ObjectOutputStream(soc2.getOutputStream());
			fromServer2 = new ObjectInputStream(soc2.getInputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@AfterEach
	public void clear(){
		port++;
		try{
			toServer.close();
			fromServer.close();
			soc.close();
			pool.shutdown();
			Server.lobbyMaxPlayer = 2;
			Server.roundDuration = 15 * 1000;
			Server.roundBreak = 5 * 1000;
			Server.gameStartCountDown = 5 * 1000;
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	// Normal Test Cases

//*
	 //* Send CONNECT message with correct information
	 //* Expect GOOD_SIGN_IN message
	 

	@Test
	public void loginSuccessfully(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.GOOD_SIGN_IN&&testFlag)
								flag = true;
						}
					}catch(Exception e){
						System.out.println("Connection closed.");
					}
				}
			};
			pool.execute(task);
			User u = new User("Alice", "Athens123", 0, 0);
			testFlag = true;
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			assertTrue(false);
		}
	}

//*
	 //* Send CONNECT message with correct username and wrong password
	 //* Expect BAD_SIGN_IN message
	 

	@Test
	public void loginUnsuccessfullyWrongPassword(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.BAD_SIGN_IN&&testFlag)
								flag = true;
						}
					}catch(Exception e){
						System.out.println("Connection closed.");
					}
				}
			};
			pool.execute(task);
			User u = new User("Bob", "WrongPassword", 0, 0);
			testFlag = true;
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//*
	 //* Send CONNECT message with correct password and wrong username
	 //* Expect BAD_SIGN_IN message
	 

	@Test
	public void loginUnsuccessfullyWrongUsername(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.BAD_SIGN_IN&&testFlag)
								flag = true;
						}
					}catch(Exception e){
						System.out.println("Connection closed.");
					}
				}
			};
			pool.execute(task);
			User u = new User("T0m", "Athens123", 0, 0);
			testFlag = true;
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//*
	 //* Send CONNECT message with correct information twice
	 //* Expect BAD_SIGN_IN message
	 

	@Test
	public void loginUnsuccessfullyAlreadyLogin(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.BAD_SIGN_IN&&testFlag)
								flag = true;
						}
					}catch(Exception e){
						System.out.println("Connection closed.");
					}
				}
			};
			pool.execute(task);
			User u = new User("Bob", "Athens123", 0, 0);
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			testFlag = true;
			sleep(100);
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

//*
	 //* Send REGISTER message with unavailable username
	 //* Expect BAD_SIGN_UP message
	 

	@Test
	public void signUpUnsuccessfullyUserExists(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.BAD_SIGN_UP&&testFlag)
								flag = true;
						}
					}catch(Exception e){
						System.out.println("Connection closed");
					}
				}
			};
			pool.execute(task);
			testFlag = true;
			User u = new User("Bob", "Athens123", 0, 0);
			toServer.writeObject(new Message(REGISTER, u, null, null, null, -1, null));
			toServer.flush();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			pool.shutdown();
		}
	}

//*
	 //* Send CONNECT message with correct information
	 //* Send DISCONNECT message
	 //* Expect message with text "logout successfully"
	 

	@Test
	public void logoutSuccessfully(){
		try{
			sleep(100);
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try{
						while(true){
							Message m = (Message)fromServer.readObject();
							if(m.getHeader()==DISCONNECT&&testFlag)
								flag =true;
						}
					}catch(Exception e){
						;
					}
				}
			};
			pool.execute(task);
			User u = new User("Bob", "Athens123", 0 ,0 );
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1, null));
			toServer.flush();
			testFlag = true;
			toServer.writeObject(new Message(DISCONNECT, u, null, null, null, -1, null));
			toServer.flush();
			sleep(100);
			assertTrue(flag);
		}catch (Exception e){
			assertTrue(false);
		}
	}

//*
	 //* Send CONNECT message with correct information
	 //* Send PLAY message
	 //* Expect QUEUEING message
	 

	@Test
	public void onePlayerJoinsLobby(){
		try{
			sleep(100);
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try{
						while(true){
							Message m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.QUEUEING&&testFlag)
								flag = true;
						}
					}catch (Exception e){
						System.out.println("Client disconnects");
					}
				}
			};
			pool.execute(task);
			User u = new User("Bob", "Athens123", 0 ,0 );
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1, null));
			toServer.flush();
			testFlag = true;
			sleep(50);
			toServer.writeObject(new Message(PLAY, u, "Data Structures", null, null, -1, null));
			toServer.flush();
			sleep(300);
			assertTrue(flag);
		}catch(Exception e){
			assertTrue(false);
		}
	}

//*
	 //* P1 sends CONNECT message with correct information
	 //* P2 sends CONNECT message with correct information
	 //* P1 sends PLAY message
	 //* P2 sends PLAY message
	 //* P1 expects PLAYER_JONIED message with user list
	 //* P2 expects QUEUEING message with user list
	 

	@Test
	public void twoPlayersJoinLobby(){
		try{
			sleep(100);
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try{
						while(true){
							Message m = (Message)fromServer.readObject();

							if(m.getHeader()==Server.QUEUEING&&testFlag){
								User[] users = m.getQueueing();
								if(users.length==1){
									if(users[0].getUsername().equals("Alice"))
										flag = true;
								}
							}

							if(m.getHeader()==Server.PLAYER_JOINED&&testFlag&&flag){
								User[] users = m.getQueueing();
								if(users.length==2){
									if(users[0].getUsername().equals("Alice")){
										if(users[1].getUsername().equals("Bob"))
											flag2 = true;
									}
									else if(users[0].getUsername().equals("Bob"))
										if(users[1].getUsername().equals("Alice"))
											flag2 = true;
								}
							}
						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};

			Runnable task2 = new Runnable() {
				@Override
				public void run() {
					try{
						while(true){
							Message m = (Message)fromServer2.readObject();
							if(m.getHeader()==Server.QUEUEING&&testFlag){
								User[] users = m.getQueueing();
								if(users.length==2){
									if(users[0].getUsername().equals("Alice")){
										if(users[1].getUsername().equals("Bob"))
											flag3 = true;
									}
									else if(users[0].getUsername().equals("Bob"))
										if(users[1].getUsername().equals("Alice"))
											flag3 = true;
								}
							}
						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};


			pool.execute(task);
			pool.execute(task2);
			User p1 = new User("Alice", "Athens123", 0 ,0 );
			User p2 = new User("Bob", "Athens123", 0, 0);

			toServer.writeObject(new Message(CONNECT, p1, null, null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(CONNECT, p2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			testFlag = true;
			toServer.writeObject(new Message(PLAY, p1, "Databases", null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(PLAY, p2, "Databases", null, null, -1, null));
			toServer2.flush();
			sleep(1000);

			System.out.println(flag);
			System.out.println(flag2);
			assertTrue(flag&&flag2&&flag3);
		}catch(Exception e){
			assertTrue(false);
		}
	}

//*
	 //* Send CONNECT message with correct information
	 //* Send PLAY message
	 //* Send QUIT message
	 //* Expect PLAYER_QUIT message
	 

	@Test
	public void onePlayerLeavesLobby(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try{
						while(true){
							Message m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.PLAYER_QUIT&&testFlag)
								flag = true;
						}
					}catch (Exception e){
						;
					}
				}
			};
			pool.execute(task);
			User u = new User("Alice", "Athens123", 0 ,0 );
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer.writeObject(new Message(PLAY, u, "Database", null, null, -1, null));
			toServer.flush();
			sleep(100);
			testFlag = true;
			toServer.writeObject(new Message(QUIT, u, null, null, null, -1, null));
			toServer.flush();;
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			assertTrue(false);
		}
	}


//*
	 //* P1 sends CONNECT message with correct information
	 //* P2 sends CONNECT message with correct information
	 //* P1 sends PLAY message
	 //* P2 sends PLAY message
	 //* P1 sends QUIT message
	 //* P2 sends QUIT message
	 //* P1 and P2 expect PLAYER_QUIT message with username "Alice"
	 //* P2 expects PLAYER_QUIT message with username "Bob"
	 

	@Test
	public void twoPlayersLeaveLobby(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int n = 0;
					try{
						while(true){
							Message m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.PLAYER_QUIT&&testFlag){
								if(m.getUser().getUsername().equals("Alice")){
									flag = true;
								}
							}

							if(m.getHeader()==Server.PLAYER_QUIT&&testFlag&&flag){
								if(m.getUser().getUsername().equals("Bob"))
									flag4 = true;
							}
						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};


			Runnable task2 = new Runnable() {
				@Override
				public void run() {
					int n = 0;
					try{
						while(true){
							Message m = (Message)fromServer2.readObject();
							if(m.getHeader()==Server.PLAYER_QUIT&&testFlag){
								if(m.getUser().getUsername().equals("Alice")){
									flag3 = true;
								}
							}

							if(m.getHeader()==Server.PLAYER_QUIT&&testFlag&&flag){
								if(m.getUser().getUsername().equals("Bob"))
									flag2 = true;
							}
						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};

			pool.execute(task);
			pool.execute(task2);
			User p1 = new User("Alice", "Athens123", 0 ,0 );
			User p2 = new User("Bob", "Athens123", 0, 0);
			Server.lobbyMaxPlayer = 3;
			sleep(100);

			toServer.writeObject(new Message(CONNECT, p1, null, null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(CONNECT, p2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			toServer.writeObject(new Message(PLAY, p1, "Databases", null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(PLAY, p2, "Databases", null, null, -1, null));
			toServer2.flush();
			sleep(100);

			testFlag = true;
			sleep(10);
			toServer.writeObject(new Message(QUIT, p1, "Databases", null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(QUIT, p2, "Databases", null, null, -1, null));
			toServer2.flush();
			sleep(100);

			assertTrue(flag&&flag2&&flag3&&!flag4);
		}catch(Exception e){
			assertTrue(false);
		}
	}


//*
	 //* P1 sends CONNECT message with correct information
	 //* P2 sends CONNECT message with correct information
	 //* P1 sends PLAY message
	 //* P2 sends PLAY message
	 //* P1 and P2 expect GAME_COUNTDOWN message immediately (timetick 0)
	 //* P2 and P2 expect Q_AND_A message after 2s(timetick 2)
	 //* P1 and P2 expect UPDATE message after 5s(timetick 5)
	 //* P1 and P2 expect Q_AND_A message after 6s(timetick 6)
	 //* P1 and P2 expect UPDATE message after 9s (timetick 9)
	 //* P1 and P2 expect Q_AND_A message after 10s (timetick 10)
	 //* P1 and P2 expect RESULT message after 13s (timetick 13)
	 

	@Test
	public void gameNoOneAnswers(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int n = 0;
					try{
						while(true){
							Message m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.GAME_COUNTDOWN&&testFlag)
								flag = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag)
								flag2 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag&&flag2)
								flag3 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag&&flag2&&flag3)
								flag4 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag&&flag2&&flag3&&flag4)
								flag5 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5)
								flag6 = true;

							if(m.getHeader()==Server.RESULTS&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5&&flag6)
								flag7 = true;

						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};

			Runnable task2 = new Runnable() {
				@Override
				public void run() {
					int n = 0;
					try{
						while(true){
							Message m = (Message)fromServer2.readObject();
							System.out.println(m);
							if(m.getHeader()==Server.GAME_COUNTDOWN&&testFlag)
								flag8 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag8)
								flag9 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag8&&flag9)
								flag10 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag8&&flag9&&flag10)
								flag11 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag8&&flag9&&flag10&&flag11)
								flag12 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag8&&flag9&&flag10&&flag11&&flag12)
								flag13 = true;

							if(m.getHeader()==Server.RESULTS&&testFlag&&flag8&&flag9&&flag10&&flag11&&flag12&&flag13)
								flag14 = true;

						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};

			testFlag = true;
			pool.execute(task);
			pool.execute(task2);
			User p1 = new User("Alice", "Athens123", 0 ,0 );
			User p2 = new User("Bob", "Athens123", 0, 0);
			sleep(100);

			toServer.writeObject(new Message(CONNECT, p1, null, null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(CONNECT, p2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			toServer.writeObject(new Message(PLAY, p1, "Data Structures", null, null, -1, null));
			toServer.flush();
			sleep(100);
			testFlag = true;
			sleep(50);
			toServer2.writeObject(new Message(PLAY, p2, "Data Structures", null, null, -1, null));
			toServer2.flush();

			// listening window opens for GAME_COUNTDOWN timetick 0.0
			sleep(200);

			// window closed timetick 0.2
			testFlag = false;
			sleep(4600);

			// listening window opens for Q_AND_A timetick 4.8
			testFlag = true;
			sleep(400); // timetick 5.2

			testFlag = false;
			sleep(14600); // timetick 19.8 

			// listening window opens for Q_AND_A timetick 4.8
			testFlag = true;
			sleep(400); // timetick 20.2

			testFlag = false;
			sleep(4600); //timetick 24.8 

			// listening window opens for RESULTS timetick 5.8
			testFlag = true;

			sleep(400); // timetick 25.2
			testFlag = false;
			sleep(14600); // timetick 39.8
			testFlag = true;
			sleep(400); // timetick 40.2
			testFlag = false;
			sleep(4600); // timetick 44.8  
			testFlag = true;
			sleep(400); // timetick 45.2
			testFlag = false;
			sleep(14600); // timetick 59.8
			testFlag = true;
			sleep(400); // timetick 60.2
			testFlag = false;
			sleep(1000); // timetick 61.2

			System.out.println(flag);
			System.out.println(flag2);
			System.out.println(flag3);
			System.out.println(flag4);
			System.out.println(flag5);
			System.out.println(flag6);
			System.out.println(flag7);
			System.out.println(flag8);
			System.out.println(flag9);
			System.out.println(flag10);
			System.out.println(flag11);
			System.out.println(flag12);
			System.out.println(flag13);
			System.out.println(flag14);

			assertTrue(flag&&flag2&&flag3&&flag4&&flag5&&flag6&&flag7&&flag8&&flag9&&flag10&&flag11&&flag12&&flag13&&flag14);
		}catch(Exception e){
			assertTrue(false);
		}
	}


//*
	 //* P1 sends CONNECT message with correct information
	 //* P2 sends CONNECT message with correct information
	 //* P1 sends PLAY message
	 //* P2 sends PLAY message
	 //* P1 and P2 expect GAME_COUNTDOWN message immediately (timetick 0)
	 //* P2 and P2 expect Q_AND_A message after 2s(timetick 2)
	 //* P1 sends ANSWER message after 3s (timetick 3)
	 //* P1 and P2 expect FIRST_PLAYER_CLAIMED message with username "Alice"
	 //* P1 and P2 expect UPDATE message after 5s(timetick 5)
	 //* P1 and P2 expect Q_AND_A message after 6s(timetick 6)
	 //* P2 sends ANSWER message after 8s (timetick 8)
	 //* P1 and P2 expect FIRST_PLAYER_CLAIMED message with username "Bob"
	 //* P1 and P2 expect UPDATE message after 9s (timetick 9)
	 //* P1 and P2 expect Q_AND_A message after 10s (timetick 10)
	 //* P1 sends ANSWER message after 11s (timetick 11)
	 //* P1 and P2 expect FIRST_PLAYER_CLAIMED message with username "Alice"
	 //* P2 sends ANSWER message after 12s (timetick 12)
	 //* P1 and P2 expect FIRST_PLAYER_CLAIMED message with username "Alice"
	 //* P1 and P2 expect RESULT message after 12s (timetick 12)
	 

	@Test
	public void gameSomePlayersAnswer(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run() {
					int n = 0;
					try{
						while(true){
							Message m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.GAME_COUNTDOWN&&testFlag)
								flag = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag)
								flag2 = true;

							if(m.getHeader()==Server.FIRST_PLAYER_CLAIMED&&m.getUser().getUsername().equals("Alice")&&testFlag&&flag&&flag2)
								flag3 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag&&flag2&&flag3)
								flag4 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag&&flag2&&flag3&&flag4)
								flag5 = true;

							if(m.getHeader()==Server.FIRST_PLAYER_CLAIMED&&m.getUser().getUsername().equals("Bob")&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5)
								flag6 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5&&flag6)
								flag7 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5&&flag6&&flag7)
								flag8 = true;

							if(m.getHeader()==Server.FIRST_PLAYER_CLAIMED&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5&&flag8)
								flag9 = true;

							if(m.getHeader()==Server.PLAYER_CLAIMED&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5&&flag8&&flag9)
								flag10 = true;

							if(m.getHeader()==Server.RESULTS&&testFlag&&flag&&flag2&&flag3&&flag4&&flag5&&flag8&&flag9&flag10)
								flag11 = true;
						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};

			Runnable task2 = new Runnable() {
				@Override
				public void run() {
					int n = 0;
					try{
						while(true){
							Message m = (Message)fromServer2.readObject();
							if(m.getHeader()==Server.GAME_COUNTDOWN&&testFlag)
								flag12 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag12)
								flag13 = true;

							if(m.getHeader()==Server.FIRST_PLAYER_CLAIMED&&m.getUser().getUsername().equals("Alice")&&testFlag&&flag&&flag2)
								flag14 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag12&&flag13&&flag14)
								flag15 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag12&&flag13&&flag14&&flag15)
								flag16 = true;

							if(m.getHeader()==Server.FIRST_PLAYER_CLAIMED&&m.getUser().getUsername().equals("Bob")&&testFlag&&flag12&&flag13&&flag14&&flag15&&flag16)
								flag17 = true;

							if(m.getHeader()==Server.UPDATE&&testFlag&&flag12&&flag13&&flag17&&flag14&&flag15&&flag16)
								flag18 = true;

							if(m.getHeader()==Server.Q_AND_A&&testFlag&&flag12&&flag13&&flag18&&flag14&&flag15&&flag16&&flag17)
								flag19 = true;

							if(m.getHeader()==Server.FIRST_PLAYER_CLAIMED&&testFlag&&flag12&&flag13&&flag14&&flag15&&flag16&&flag17&&flag18&&flag19)
								flag20 = true;

							if(m.getHeader()==Server.PLAYER_CLAIMED&&testFlag&&flag12&&flag13&&flag14&&flag15&&flag16&&flag17&&flag18&&flag19&&flag20)
								flag21 = true;

							if(m.getHeader()==Server.RESULTS&&testFlag&&flag12&&flag13&&flag14&&flag15&&flag16&&flag17&&flag18&flag19&&flag20&&flag21)
								flag22 = true;
						}
					}catch (Exception e){
						System.out.println("Connection closed");
					}
				}
			};



			testFlag = true;
			pool.execute(task);
			pool.execute(task2);
			User p1 = new User("Alice", "Athens123", 0 ,0 );
			User p2 = new User("Bob", "Athens123", 0, 0);
			sleep(100);

			toServer.writeObject(new Message(CONNECT, p1, null, null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(CONNECT, p2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			toServer.writeObject(new Message(PLAY, p1, "Data Structures", null, null, -1, null));
			toServer.flush();
			sleep(100);
			testFlag = true;
			toServer2.writeObject(new Message(PLAY, p2, "Data Structures", null, null, -1, null));
			toServer2.flush();

			testFlag = true;sleep(200); testFlag = false; //0.2
			sleep(4600);// 4.8
			testFlag = true;sleep(400); testFlag = false; //5.2
			sleep(4600);//9.8
			testFlag = true;
			toServer.writeObject(new Message(ANSWER, p1, "Data Structures", null, null, 1, null));
			toServer.flush();
			sleep(400); testFlag = false;// 10.2
			sleep(9600);//19.8
			testFlag = true;sleep(400); testFlag = false;// 20.2
			sleep(4600);//24.8
			testFlag = true;sleep(400); testFlag = false;// 25.2
			sleep(9600);// 34.8
			testFlag = true;
			toServer2.writeObject(new Message(ANSWER, p2, "Data Structures", null, null, 2, null));
			toServer2.flush();
			sleep(400); testFlag = false;//35.2
			sleep(4600);// 39.8
			testFlag = true;sleep(400); testFlag = false;//40.2
			sleep(4600);//44.8
			testFlag = true;sleep(400); testFlag = false;//45.2
			sleep(4600);//49.8
			testFlag = true;
			toServer.writeObject(new Message(ANSWER, p1, "Data Structures", null, null, 1, null));
			toServer.flush();
			sleep(400); testFlag = false;// 50.2
			sleep(4600);// 54.8
			testFlag = true;
			toServer2.writeObject(new Message(ANSWER, p2, "Data Structures", null, null, 2, null));
			toServer2.flush();
			sleep(400); testFlag = false;// 55.2
			sleep(4600);// 59.8
			testFlag = true;
			sleep(400); // 60.2
			testFlag = false;
			assertTrue(flag&&flag2&&flag3&&flag4&&flag5&&flag6&&flag7&&flag8&&flag9&&flag10&&flag11&&flag12&&flag13&&flag14&&flag15&&flag16&&flag17&&flag18&&flag19&&flag20&&flag21&&flag22&&flag22);
		}catch(Exception e){
			e.printStackTrace();
			assertTrue(false);
		}
	}

//*
	 //* Client sends LEADERBOARD message to the server
	 //* Client expects LEADERBOARD message with an user array of 80 size
	 

	@Test
	public void leaderBoard(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==LEADERBOARD&&testFlag)
								if(m.getQueueing().length==80)
									flag = true;
						}
					}catch(Exception e){
						System.out.println("Connection closed.");
					}
				}
			};
			pool.execute(task);
			testFlag = true;
			toServer.writeObject(new Message(LEADERBOARD, null, null, null, null, -1 ,null));
			toServer.flush();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	// Abnormal Test Cases

	@Test
	public void ClientInterruptsBeforeSignIn(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
						if(e.getMessage().equals("Socket closed"))
							flag = true;
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			testFlag = true;
			sleep(100);
			soc.close();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void ClientInterruptsAfterSignIn(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
						if(e.getMessage().equals("Socket closed"))
							flag = true;
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			User u = new User("Bob", "Athens123", 0, 0);
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			testFlag = true;
			sleep(100);
			soc.close();
			sleep(100);
			assertTrue(flag);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void ClientInterruptsInGame(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.GOOD_SIGN_IN)
								flag = true;
							if(m.getHeader()==Server.QUEUEING&&flag)
								flag3 = true;
							if(m.getHeader()==Server.PLAYER_QUIT&&flag3)
								flag6 = true;

						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};

			Runnable task2 = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m2 = (Message)fromServer2.readObject();
							if(m2.getHeader()==Server.GOOD_SIGN_IN)
								flag2 = true;
							if(m2.getHeader()==Server.QUEUEING&&flag)
								flag4 = true;
						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
						if(flag3&&flag4)
							flag5 = true;
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			pool.execute(task2);
			User u = new User("Bob", "Athens123", 0, 0);
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			User u2 = new User("Alice", "Athens123", 0, 0);
			toServer2.writeObject(new Message(CONNECT, u2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			toServer.writeObject(new Message(PLAY, u, "Data Structures", null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(PLAY, u2, "Data Structures", null, null, -1, null));
			toServer2.flush();
			sleep(7000);
			soc2.close();
			sleep(100);

			assertTrue(flag5&&flag6);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void ClientInterruptsInLobby(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.GOOD_SIGN_IN)
								flag = true;
							if(m.getHeader()==Server.QUEUEING&&flag)
								flag3 = true;
							if(m.getHeader()==Server.PLAYER_QUIT&&flag&&flag3)
								flag6 = true;

						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};

			Runnable task2 = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m2 = (Message)fromServer2.readObject();
							if(m2.getHeader()==Server.GOOD_SIGN_IN)
								flag2 = true;
							if(m2.getHeader()==Server.QUEUEING&&flag2)
								flag4 = true;
						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
						if(flag2&&flag4)
							flag5 = true;
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			pool.execute(task2);
			Server.lobbyMaxPlayer = 3;
			User u = new User("Bob", "Athens123", 0, 0);
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			User u2 = new User("Alice", "Athens123", 0, 0);
			toServer2.writeObject(new Message(CONNECT, u2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			toServer.writeObject(new Message(PLAY, u, "Data Structures", null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(PLAY, u2, "Data Structures", null, null, -1, null));
			toServer2.flush();
			sleep(3000);
			soc2.close();
			sleep(100);

			assertTrue(flag5&&flag6);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void ClientInterruptsDuringLobbyCountDown(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.GOOD_SIGN_IN)
								flag = true;
							if(m.getHeader()==Server.QUEUEING&&flag)
								flag3 = true;
							if(m.getHeader()==Server.PLAYER_JOINED&&flag&&flag3)
								flag6 = true;
							if(m.getHeader()==Server.GAME_COUNTDOWN&&flag&&flag3&&flag6)
								flag7 = true;
							if(m.getHeader()==Server.PLAYER_QUIT&&flag&&flag3&&flag6&&flag7)
								flag8 = true;
							if(m.getHeader()==Server.QUEUEING&&flag&&flag3&&flag6&&flag7&&flag8)
								flag9 = true;

						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};

			Runnable task2 = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m2 = (Message)fromServer2.readObject();
							if(m2.getHeader()==Server.GOOD_SIGN_IN)
								flag2 = true;
							if(m2.getHeader()==Server.QUEUEING&&flag2)
								flag4 = true;
							if(m2.getHeader()==Server.GAME_COUNTDOWN&&flag2&&flag4)
								flag10 = true;
						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
						if(flag3&&flag4&&flag10)
							flag5 = true;
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			pool.execute(task2);
			User u = new User("Bob", "Athens123", 0, 0);
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			User u2 = new User("Alice", "Athens123", 0, 0);
			toServer2.writeObject(new Message(CONNECT, u2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			toServer.writeObject(new Message(PLAY, u, "Data Structures", null, null, -1, null));
			toServer.flush();
			sleep(100);
			toServer2.writeObject(new Message(PLAY, u2, "Data Structures", null, null, -1, null));
			toServer2.flush();
			sleep(1000);
			soc2.close();
			sleep(100);
			assertTrue(flag&&flag2&&flag3&&flag4&&flag5&&flag6&&flag7&&flag8&&flag9&&flag10);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Test
	public void ClientsSendPlayMessageAtTheSameTime(){
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromServer.readObject();
							if(m.getHeader()==Server.GOOD_SIGN_IN)
								flag = true;
							if(m.getHeader()==Server.QUEUEING&&flag)
								flag3 = true;
							if(m.getHeader()==Server.GAME_COUNTDOWN&&flag&&flag3)
								flag6 = true;
						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};

			Runnable task2 = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m2 = (Message)fromServer2.readObject();
							if(m2.getHeader()==Server.GOOD_SIGN_IN)
								flag2 = true;
							if(m2.getHeader()==Server.QUEUEING&&flag2)
								flag4 = true;
							if(m2.getHeader()==Server.GAME_COUNTDOWN&&flag2)
								flag7 = true;
						}
					}catch(SocketException e){
						System.out.println("Connection closed.");
					}catch(IOException e){
						e.printStackTrace();
					}catch (ClassNotFoundException e){
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			pool.execute(task2);
			User u = new User("Bob", "Athens123", 0, 0);
			toServer.writeObject(new Message(CONNECT, u, null, null, null, -1 ,null));
			toServer.flush();
			User u2 = new User("Alice", "Athens123", 0, 0);
			toServer2.writeObject(new Message(CONNECT, u2, null, null, null, -1, null));
			toServer2.flush();
			sleep(100);

			toServer.writeObject(new Message(PLAY, u, "Data Structures", null, null, -1, null));
			toServer.flush();
			toServer2.writeObject(new Message(PLAY, u2, "Data Structures", null, null, -1, null));
			toServer2.flush();
			sleep(1000);
			assertTrue(flag6&&flag7);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//PLACEHOLDERR
	public void questionResultSetRandomnessTest(){
		try{
			Queries db = new Queries();

			List<Question> qs = db.createQuiz("Data Structures", 1);
			qs.forEach(q->{System.out.println(q.getQuestion());});

			List<Question> qs2 = db.createQuiz("Data Structures", 1);
			qs2.forEach(q->{System.out.println(q.getQuestion());});

			int question_num = qs.size();
			boolean flags[] = new boolean[question_num];
			for(int i = 0; i < question_num; i++){
				String q = qs.get(i).getQuestion();
				for(int j = 0; j < question_num; j++){
					if(q.equals(qs2.get(j).getQuestion())){
						flags[i] = true;
					}
				}
			}

			boolean result = true;
			for(int i = 0; i < question_num; i++){
				result = result&&flags[i];
				System.out.println(flags[i]);
			}
			assertFalse(result);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
