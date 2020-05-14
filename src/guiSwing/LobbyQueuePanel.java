package guiSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import general.User;

/**
 * Here is a class which is the LobbyQueue, this is the GUI frame which displays
 * information about the users that are currently in the queue for their
 * specific game type.
 * 
 * @author parker, Athens
 *
 * @version 09/03/2018
 */
@SuppressWarnings("serial")
public class LobbyQueuePanel extends JPanel implements Observer {
	
	private ViewDriver gui;
	private PlayerPanel player1, player2, player3, player4;
	private JLabel player1Lab, player2Lab, player3Lab, player4Lab;
	private ArrayList<PlayerPanel> playerPanels;
	private JPanel titlePanel;
	private JLabel titleLabel, playersInQueue;
	private JLabel titleLabel2;
	private JButton backButton;

	/**
	 * Constructor for the LobbyQueue panel.
	 */
	public LobbyQueuePanel(ViewDriver frame) {
		this.gui = frame;

		setLayout(null);
		this.setBounds(0, 0, 460, 768);

		titlePanel = new JPanel();
		titlePanel.setBounds(212, 0, 600, 120);
		titlePanel.setBackground(new Color(127, 127, 127, 127));
		add(titlePanel);
		titlePanel.setLayout(new GridLayout(3, 1, 0, 0));

		titleLabel = new JLabel("Waiting for players to join...");
		titleLabel.setHorizontalAlignment(0);
		titlePanel.add(titleLabel);
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		titleLabel.setForeground(Color.YELLOW);
		
		titleLabel2 = new JLabel();

		playersInQueue = new JLabel("These are the rivals who dare challenge your CompSci knowledge!");
		playersInQueue.setHorizontalAlignment(0);
		titlePanel.add(playersInQueue);
		playersInQueue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		playersInQueue.setForeground(Color.YELLOW);

		/*
		 * Set the gameType Panel and Label.
		 */
		JPanel typePanel = new JPanel();
		typePanel.setBackground(new Color(127, 127, 127, 127));
		typePanel.setBounds(212, 145, 600, 40);
		add(typePanel);
		
		JLabel typeLabel = new JLabel(frame.getModel().gameType);
		typeLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		typeLabel.setForeground(Color.YELLOW);
		typePanel.add(typeLabel);
		
		JPanel panelPlayers = new JPanel();
		panelPlayers.setBounds(40, 200, 944, 488);
		panelPlayers.setBackground(new Color(0, 0, 0, 0));
		panelPlayers.setLayout(null);
		add(panelPlayers);

		player1 = new PlayerPanel();
		player1.setLayout(new GridBagLayout());
		player1.setBounds(50, 20, 844, 98);
		panelPlayers.add(player1);

		player2 = new PlayerPanel();
		player2.setLayout(new GridBagLayout());
		player2.setBounds(50, 138, 844, 98);
		panelPlayers.add(player2);

		player3 = new PlayerPanel();
		player3.setLayout(new GridBagLayout());
		player3.setBounds(50, 254, 844, 98);
		panelPlayers.add(player3);

		player4 = new PlayerPanel();
		player4.setLayout(new GridBagLayout());
		player4.setBounds(50, 372, 844, 98);
		panelPlayers.add(player4);

		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getClient().sendQuitLobbyMessage(frame.getModel().user);
			}
		});
		backButton.setBounds(452, 680, 120, 32);
		add(backButton);

		JLabel backgroundLabel = new JLabel("");
		int bg = frame.getModel().background;
		if (bg == 0)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/pcb.jpg")));
		else if (bg == 1)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background1.jpg")));
		else if (bg == 2)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background2.jpg")));
		else if (bg == 3)
			backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background3.jpg")));

		backgroundLabel.setBounds(0, 0, 1024, 768);
		add(backgroundLabel);

		playerPanels = new ArrayList<>();

		playerPanels.add(player1);
		playerPanels.add(player2);
		playerPanels.add(player3);
		playerPanels.add(player4);
	}

	public void updateLobby() {
		titleLabel.setVisible(true);
		titleLabel2.setVisible(false);
		playersInQueue.setVisible(true);
		backButton.setVisible(true);
		if (!gui.getModel().countdown) {
			Map<User, Integer> players = gui.getModel().usersInLobby;
			Map<User, Integer> temp = players;

			// Iterator<PlayerPanel> playerIterator = playerPanels.iterator();

			User ourUser = gui.getModel().user;

			player1Lab = new JLabel();
			player1Lab.setText(ourUser.getUsername().toLowerCase());
			player1Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
			player1Lab.setForeground(Color.YELLOW);
			player1.add(player1Lab);

				players.remove(ourUser);
			/*
			 * We do this so that the first user to appear in the Lobby Queue is
			 * the player associated with that client.
			 */
			int i = 0;
			String[] arr = new String[3];
			Map<User, Integer> tempMap = new HashMap<>();
			for(Map.Entry<User, Integer> u : players.entrySet()){
				if(u.getKey().equals(ourUser))
					continue;
				tempMap.put(u.getKey(), u.getValue());
			}


			for (Map.Entry<User, Integer> u : tempMap.entrySet()) {
				arr[i] = "Challenger " + (i + 1) + " : " + u.getKey().getUsername().toLowerCase();
				i++;
				// new method for setting playerPanel data
				// playerPanels.get(0).setRankLabelText(text);;

				player2Lab = new JLabel();
				player2Lab.setText(arr[0]);
				player2Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
				player2Lab.setForeground(Color.YELLOW);
				player2.add(player2Lab);

				player3Lab = new JLabel();
				player3Lab.setText(arr[1]);
				player3Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
				player3Lab.setForeground(Color.YELLOW);
				player3.add(player3Lab);

				player4Lab = new JLabel();
				player4Lab.setText(arr[2]);
				player4Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
				player4Lab.setForeground(Color.YELLOW);
				player4.add(player4Lab);
			}
		} else { // When the countdown is true we remove the back button and
					// notify the players
			
			titleLabel.setVisible(false);
			playersInQueue.setVisible(false);
			backButton.setVisible(false);
			
			titleLabel2 = new JLabel();
			titleLabel2.setFont(new Font("Tahoma", Font.PLAIN, 18));
			titleLabel2.setForeground(Color.YELLOW);
			titleLabel2.setText("The game is starting, Get Ready to Rumble!");
			titleLabel2.setHorizontalAlignment(0);
			titlePanel.add(titleLabel2);
		}
		validate();
	}

	@Override
	public void update(Observable o, Object arg) {
		updateLobby();
	}

}