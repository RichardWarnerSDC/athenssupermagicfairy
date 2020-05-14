package guiSwing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import client.Client;

/**
 * Main class of the MagicFairyGui Package. Creates gui instances.
 * 
 * @author Parker, Richard.
 *
 */
@SuppressWarnings("serial")
public class ViewDriver extends JFrame implements Observer {

	public Client client;
	private GameModel model;
	private WelcomePanel welcomePanel;
	private LoginPanel loginPanel;
	private MainMenuPanel mainMenuPanel;
	private LobbyQueuePanel lobbyQueuePanel;
	private RegisterPanel registerPanel;
	private HelpMenuPanel helpMenuPanel;
	private GamePanel gamePanel;
	private GameScorePanel gameScorePanel;
	private EditInfoPanel editInfoPanel;
	private LeaderboardPanel leaderboardPanel;

	private static final String TITLE = "Super Magic Fiary Fun Quiz!!";
	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;

	/**
	 * Default ViewDriver constructor for standalone gui testing.
	 */
	 public ViewDriver() {
	    super(TITLE);

	    setSize(WIDTH, HEIGHT);
	    setLocationRelativeTo(null);
	    setResizable(false);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    model = new GameModel();
	    model.addObserver(this);
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	    welcomePanel = new WelcomePanel(this);

	    this.add(welcomePanel);
	    setVisible(true);
	  }
	
	/**
	 * Here is a constructor for the View Class which creates a View Object.
	 */
	public ViewDriver(Client client) {
		super(TITLE);
		this.client = client;

		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		model = new GameModel();
		model.addObserver(this);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		welcomePanel = new WelcomePanel(this);

		this.add(welcomePanel);
		setVisible(true);
	}

	/**
	 * Getter for the Client
	 * 
	 * @return the Client associated with this GUI. Hello
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * This is a method that we call when the Server stops/crashes, notifying
	 * the user that the system is having problems.
	 */
	public void errorFrameCrash() {
	//	model.deleteObservers();
		JFrame errorFrame = new JFrame("Connection Problem =[");
		errorFrame.setSize(550, 250);
		errorFrame.setLocationRelativeTo(this);
		JPanel errorPanel = new JPanel();
	//	errorPanel.setBounds(50, 25, 450, 150);
		JTextArea errorMessage = new JTextArea(2, 30);
		errorMessage.setWrapStyleWord(true);
		errorMessage.setLineWrap(true);
		errorMessage.setOpaque(false);
		errorMessage.setEditable(false);
		errorMessage.setFocusable(false);
		errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorMessage.setForeground(Color.RED);
		String text = "We are so sorry but we have lost connection to the server."
				+ " This is our fault and there is no issue with your client =]."
				+ "Please press quit to end this program and try again later!";
		errorMessage.setText(text);
		errorMessage.setForeground(Color.RED);

		JButton exitBtn = new JButton("Exit");
		exitBtn.setBounds(225, 130, 100, 50);
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				client.endClient();
				System.exit(1);
			}
		});
		errorFrame.add(exitBtn);

		errorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		errorPanel.add(errorMessage);
		errorFrame.add(errorPanel);
		errorFrame.setVisible(true);
		add(errorFrame);
	}

	/**
	 * When registering, if the username is already in the database, this will
	 * trigger notifying the user that they cannot register with that username.
	 */
	public void userExistsError() {
		JFrame errorFrame = new JFrame("Registration Error");
		errorFrame.setLocationRelativeTo(this);
		errorFrame.setSize(450, 200);
		errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel errorPanel = new JPanel();
		errorPanel.setBounds(0, 0, 500, 50);
		JLabel errorMessage = new JLabel();
		errorMessage.setHorizontalAlignment(0);
		errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorMessage.setForeground(Color.RED);
		errorMessage.setText("That username allready exists!");
		errorPanel.add(errorMessage);
		errorFrame.add(errorPanel);
		
		JPanel btn = new JPanel();
		btn.setBounds(175, 150, 100, 50);
		errorFrame.add(btn);
		
		JButton exitBtn = new JButton("Got it");
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				errorFrame.dispose();	
			}		
		});
		btn.add(exitBtn);
		
		errorFrame.setVisible(true);
	}

	/**
	 * When logging in, if the user types in an incorrect username or password,
	 * this will trigger to notify them that they have typed in an incorrect
	 * username or password.
	 */
	public void userWrongError() {
		JFrame errorFrame = new JFrame("Login Error");
		errorFrame.setLocationRelativeTo(this);
		errorFrame.setSize(450, 100);
		errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel errorPanel = new JPanel();
		errorPanel.setBounds(0, 0, 500, 50);
		JLabel errorMessage = new JLabel();
		errorMessage.setHorizontalAlignment(0);
		errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorMessage.setForeground(Color.RED);
		errorMessage.setText("Invalid username or password =[");
		errorPanel.add(errorMessage);
		errorFrame.add(errorPanel);
		errorFrame.setVisible(true);
	}

	public void userSignUpGood() {
		JFrame goodFrame = new JFrame("Good sign up!");
		goodFrame.setLocationRelativeTo(this);
		goodFrame.setSize(450, 100);
		goodFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel goodPanel = new JPanel();
		goodPanel.setBounds(0, 0, 500, 50);
		JLabel errorMessage = new JLabel();
		errorMessage.setHorizontalAlignment(0);
		errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorMessage.setForeground(Color.RED);
		errorMessage.setText("Successfully signed up!");
		goodPanel.add(errorMessage);
		goodFrame.add(goodPanel);
		goodFrame.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (model.welcomeMenu) {
			model.deleteObservers();
			addThisObserver();
			this.getContentPane().removeAll();
			WelcomePanel newPane = new WelcomePanel(this);
			this.add(newPane);
			validate();
		} else if (model.loginMenu) {
			model.deleteObservers();
			addThisObserver();
			this.getContentPane().removeAll();
			loginPanel = new LoginPanel(this);
			this.add(loginPanel);
			validate();
		} else if (model.registerMenu) {
			model.deleteObservers();
			addThisObserver();
			this.getContentPane().removeAll();
			registerPanel = new RegisterPanel(this);
			this.add(registerPanel);
			validate();
		} else if (model.mainMenu) {
			model.deleteObservers();
			this.getContentPane().removeAll();
			mainMenuPanel = new MainMenuPanel(this);
			this.add(mainMenuPanel);
			model.addObserver(mainMenuPanel);
			validate();
		} else if (model.lobbyMenu) {
			model.deleteObservers();
			System.out.println("Update to lobby queue");
			this.getContentPane().removeAll();
			lobbyQueuePanel = new LobbyQueuePanel(this);
			lobbyQueuePanel.updateLobby();
			model.addObserver(lobbyQueuePanel);
			this.add(lobbyQueuePanel);
			validate();
		} else if (model.helpMenu) {
			model.deleteObservers();
			addThisObserver();
			this.getContentPane().removeAll();
			helpMenuPanel = new HelpMenuPanel(client, this);
			this.add(helpMenuPanel);
			validate();
		} else if (model.editMenu) {
			model.deleteObservers();
			addThisObserver();
			this.getContentPane().removeAll();
			editInfoPanel = new EditInfoPanel(this);
			this.add(editInfoPanel);
			validate();
		} else if (model.gameMenu) {
			model.deleteObservers();
			this.getContentPane().removeAll();
			gamePanel = new GamePanel(this);
			model.addObserver(gamePanel);
			this.add(gamePanel);
			validate();
		} else if (model.midRoundMenu) {
			model.deleteObservers();
			this.getContentPane().removeAll();
			gameScorePanel = new GameScorePanel(this);
			this.add(gameScorePanel);
			validate();
		} else if (model.leaderboard) {
			model.deleteObservers();
			addThisObserver();
			this.getContentPane().removeAll();
			leaderboardPanel = new LeaderboardPanel(this);
			this.add(leaderboardPanel);
			validate();
		}
	}

	// Make this an observer again
	public void addThisObserver() {
		model.addObserver(this);
	}

	// GameModel Getter
	public GameModel getModel() {
		return this.model;
	}
	
	public static void main(String[] args) {
	  ViewDriver view = new ViewDriver();
	}

}