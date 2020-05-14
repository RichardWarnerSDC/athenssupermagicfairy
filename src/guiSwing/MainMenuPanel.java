/**
 * The main menu of the gui, allows access to the game, leaderboard, player options and the help menu.
 * Player also has the option to logout or exit the game.
 * @author Parker, Richard.
 */
package guiSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.Client;
import general.User;

@SuppressWarnings("serial")
public class MainMenuPanel extends JPanel implements Observer {

	private ViewDriver frame;
	private JLabel backgroundLabel;
	String gameTypeSelected = "Software Workshop";
	int newBackground = 0;

	public MainMenuPanel(ViewDriver frame) {
		this.frame = frame;

		setLayout(null);

		// Semi-transparent header panel that displays the panel's title more visibly.
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(0, 1, 0, 0));
		headerPanel.setBounds(0, 0, 1024, 120); 
		headerPanel.setBackground(new Color(127, 127, 127, 127));
		add(headerPanel);

		// Main Menu's title.
		JLabel mainMenuLabel = new JLabel("Main Menu");
		mainMenuLabel.setHorizontalAlignment(0);
		headerPanel.add(mainMenuLabel);
		mainMenuLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainMenuLabel.setForeground(Color.YELLOW);

		// The panel for which the menu buttons sit on.
		JPanel menuPanel = new JPanel();
		menuPanel.setBounds(412, 284, 180, 200);
		menuPanel.setLayout(new GridLayout(0, 1, 0, 0));
		menuPanel.setBackground(new Color(127, 127, 127, 0));
		add(menuPanel);

		// Panel for the JComboBox to sit on.
		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new GridLayout());
		comboPanel.setBounds(592, 284, 240, 40);
		add(comboPanel);

		//  JComboBox contains the topics which the player can select to play.
		String[] gameTypes = { "Software Workshop", "Data Structures", "Computer Science", "Databases",
				"Artificial Intelligence", "Operating Systems and Networks",
				"Nature Inspired Search and Optimisation" };
		JComboBox<String> gameType = new JComboBox<String>(gameTypes);
		gameType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				gameTypeSelected = gameType.getSelectedItem().toString();
				frame.getModel().gameType = gameTypeSelected;
			}
		});
		comboPanel.add(gameType);

		JButton btnPlay = new JButton("Play");
		menuPanel.add(btnPlay);
		btnPlay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.addThisObserver();
				frame.getClient().sendPlayMessage(frame.getModel().user, gameTypeSelected);
			}
		});

		JButton btnLeaderboard = new JButton("Leaderboard");
		btnLeaderboard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.addThisObserver();
				frame.getClient().sendLeaderboardMessage();
			}
		});
		menuPanel.add(btnLeaderboard);

		JButton btnEdit = new JButton("Edit Player");
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.addThisObserver();
				frame.getModel().enterEditInfoMenu();
			}
		});
		menuPanel.add(btnEdit);

		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.addThisObserver();
				frame.getModel().enterHelpMenu();
			}
		});
		menuPanel.add(btnHelp);

		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// MAYBE CALL client.finalize here?!?! / Maybe CALL
				// client.endClient here also??????
				System.exit(1);
			}
		});
		menuPanel.add(btnQuit);

		JButton logoutButton = new JButton("Logout");
		logoutButton.setSize(120, 32);
		logoutButton.setLocation(452, 600);
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.addThisObserver();
				frame.getClient().sendLogoutMessage(frame.getModel().user);
			}
		});
		add(logoutButton);

		JButton btnBG = new JButton("Change Background");
		btnBG.setBounds(30, 680, 200, 40);
		add(btnBG);

		btnBG.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeBackgroundFrame();
			}
		});

		backgroundLabel = new JLabel("");

		int newBG = frame.getModel().background;
		if (newBG == 0)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/pcb.jpg")));
		else if (newBG == 1)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background1.jpg")));
		else if (newBG == 2)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background2.jpg")));
		else if (newBG == 3)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background3.jpg")));

		backgroundLabel.setBounds(0, 0, 1024, 768);
		add(backgroundLabel);

	}

	public void changeBackgroundFrame() {
		JFrame changeFrame = new JFrame("Change the background");
		changeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		changeFrame.setLayout(null);
		changeFrame.setLocationRelativeTo(null);
		changeFrame.setSize(400, 250);
		changeFrame.setResizable(false);
		JPanel topPanel = new JPanel();
		topPanel.setBounds(0, 20, 400, 50);
		JLabel errorMessage = new JLabel();
		errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
		errorMessage.setForeground(Color.RED);
		String text = "Here you can change the background!";
		errorMessage.setText(text);
		errorMessage.setForeground(Color.RED);
		topPanel.add(errorMessage);
		changeFrame.add(topPanel);

		JPanel comboBG = new JPanel();
		comboBG.setBounds(100, 80, 200, 60);
		changeFrame.add(comboBG);

		String[] backgrounds = { "Motherboard", "Blocks", "Binary", "Code" };
		JComboBox<String> bg = new JComboBox<String>(backgrounds);
		bg.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				newBackground = bg.getSelectedIndex();
			}
		});
		comboBG.add(bg);

		JPanel changeBtnPan = new JPanel();
		changeBtnPan.setBounds(130, 150, 150, 200);
		changeFrame.add(changeBtnPan);

		JButton changeBtn = new JButton("Apply");
		changeBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getModel().setBackground(newBackground);
				changeFrame.dispose();
			}
		});
		changeBtnPan.add(changeBtn);
		changeFrame.setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		int bg = frame.getModel().background;
		if (bg == 0)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/pcb.jpg")));
		else if (bg == 1)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background1.jpg")));
		else if (bg == 2)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background2.jpg")));
		else if (bg == 3)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background3.jpg")));
		
		validate();
	}

}
