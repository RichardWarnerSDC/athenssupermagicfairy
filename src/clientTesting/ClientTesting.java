/** This java file tests the client according to the test plan using JUnit5.
 *  
 *  @author Ed Wong, Team Athens
 *  @version 18/3/2018 
 */

package clientTesting;

import static java.lang.Thread.sleep;
import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import client.*;
import client.Client.ComProtocolClient;
import general.Message;
import general.User;
import server.Server;
import server.ServerThread.ComProtocolServer;

public class ClientTesting {

	private ObjectInputStream fromClient;
	private ObjectOutputStream toClient;
	private Socket soc;
	private Client c;
	private boolean flag;
	private Message m;
	private ExecutorService pool;
	private int port = 50000;
	private ServerSocket serverSocket;
	private User user = new User("IWantToConnect", "1234", 0,0);

	@BeforeEach
	public void setUp() {
		flag = false;
		pool = Executors.newCachedThreadPool();
		try{
			Runnable task1 = new Runnable() {
				@Override
				public void run() {
					try {
						serverSocket = new ServerSocket(port);
						while(true) {
							soc = serverSocket.accept();
							if(!soc.isClosed() || soc != null){
								break;
							}
						}
					} catch (IOException e) {
						System.out.println("error in accepting socket");
						e.printStackTrace();
					}	
				}
			};
			pool.execute(task1);
			Runnable task = new Runnable() {
				@Override
				public void run() {
					c = new Client("localhost");
					System.out.println("created client "+ c);
				}
			};
			pool.execute(task);
			sleep(500);
			while(true) {
				if(!soc.isClosed() || soc != null) {
					toClient = new ObjectOutputStream(soc.getOutputStream());
					fromClient = new ObjectInputStream(soc.getInputStream());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("error in setup");
			e.printStackTrace();
		}
	}

	@AfterEach
	public void close() {
		try {
			//c.finalize();
			toClient.close();
			fromClient.close();
			soc.close();
			serverSocket.close();
			port++;
		} catch (Exception e) {
			System.out.println("error in close");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the connect message
	 *  to it's object output stream.
	 */
	@Test	
	public void connectTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.CONNECT && m.getUser().equals(user)) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 1 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendConnectMessage(user);
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 1:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the connect message
	 *  to it's object output stream.
	 */
	@Test	
	public void registerTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.REGISTER && m.getUser().equals(user)) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 3 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendRegisterMessage(user);
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 3:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the log out message
	 *  to it's object output stream.
	 */
	@Test	
	public void logoutTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.LOGOUT && m.getUser().equals(user)) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 3 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendLogoutMessage(user);
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 4:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the disconnect message
	 *  to it's object output stream.
	 */
	@Test	
	public void disconnectTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message) fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.DISCONNECT && m.getUser().equals(user)) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 3 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendDisconnectMessage(user);
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 5:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the PLAY message
	 *  to it's object output stream.
	 */
	@Test	
	public void playTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.PLAY && m.getUser().equals(user) && m.getText().equals("gametype")) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 6 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendPlayMessage(user, "gametype");
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 6:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test	
	public void quitTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.QUIT && m.getUser().equals(user)) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 7 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendQuitLobbyMessage(user);
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 7:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the answer message
	 *  to it's object output stream.
	 */
	@Test	
	public void answerTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run() {
					try {
						while(true) {
							m = (Message)fromClient.readObject();
							if(m.getHeader() == ComProtocolServer.ANSWER && m.getUser().equals(user) && m.getAnswer() == 0) {
								flag = true;
								break;
							}
						}
					} catch(Exception e) {
						System.out.println("task in test 8 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendAnswerMessage(user, 0);
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 8:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void changeTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.CHANGE_INFO && m.getUser().equals(user)) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 3 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendChangeInfoMessage(user);
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 9:");
			e.printStackTrace();
		}
	}

	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void leaderboardTest() {
		try{
			Runnable task = new Runnable() {
				@Override
				public void run(){
					try{
						while(true){
							m = (Message)fromClient.readObject();
							if(m.getHeader()==ComProtocolServer.LEADERBOARD) {
								flag = true;
								break;
							}
						}
					}catch(Exception e){
						System.out.println("task in test 10 error:");
						e.printStackTrace();
					}
				}
			};
			pool.execute(task);
			sleep(500);
			c.sendLeaderboardMessage();
			sleep(500);
			assertTrue(flag);
		} catch (Exception e) {
			System.out.println("error in test 10:");
			e.printStackTrace();
		}
	}


	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void logInDisconnectTest() {
		try{
			sleep(500);
			soc.close();
			sleep(500);
			System.out.println("finished: "+c.getFinished());
			assertTrue(c.getFinished());
		} catch (Exception e) {
			System.out.println("error in test 11:");
			e.printStackTrace();
		}
	}	

	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void mainMenuDisconnectTest() {
		try{
			toClient.writeObject(new Message(ComProtocolClient.GOOD_SIGN_IN, user, "Successfully sign in", null, null, -1));
			sleep(500);
			soc.close();
			sleep(500);
			System.out.println("finished: "+c.getFinished());
			assertTrue(c.getFinished());
		} catch (Exception e) {
			System.out.println("error in test 11:");
			e.printStackTrace();
		}
	}
	
	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void lobbyDisconnectTest() {
		try{
			toClient.writeObject(new Message(ComProtocolClient.QUEUEING, null, "welcome", null, null, -1, new User[0]));
			sleep(500);
			soc.close();
			sleep(500);
			System.out.println("finished: "+c.getFinished());
			assertTrue(c.getFinished());
		} catch (Exception e) {
			System.out.println("error in test 11:");
			e.printStackTrace();
		}
	}
	
	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void gameDisconnectTest() {
		try{
			toClient.writeObject(new Message(ComProtocolClient.Q_AND_A, null, null, "q", new String[0], 0));
			sleep(500);
			soc.close();
			sleep(500);
			System.out.println("finished: "+c.getFinished());
			assertTrue(c.getFinished());
		} catch (Exception e) {
			System.out.println("error in test 11:");
			e.printStackTrace();
		}
	}
	
	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void scoringDisconnectTest() {
		try{
			toClient.writeObject(new Message(ComProtocolClient.UPDATE, new HashMap<User,Integer>()));
			sleep(500);
			soc.close();
			sleep(500);
			System.out.println("finished: "+c.getFinished());
			assertTrue(c.getFinished());
		} catch (Exception e) {
			System.out.println("error in test 11:");
			e.printStackTrace();
		}
	}
	
	/** This tests if the client successfully sends the quit lobby message
	 *  to it's object output stream.
	 */
	@Test
	public void resultsTest() {
		try{
			toClient.writeObject(new Message(Server.RESULTS, new HashMap<User,Integer>()));
			sleep(500);
			soc.close();
			sleep(500);
			System.out.println("finished: "+c.getFinished());
			assertTrue(c.getFinished());
		} catch (Exception e) {
			System.out.println("error in test 11:");
			e.printStackTrace();
		}
	}
}