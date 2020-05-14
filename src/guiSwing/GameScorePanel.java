package guiSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import general.User;

/**
 * Here is a dummy class that we can use for the between questions attempt at
 * writing a GameScorePanel which allows us to view the scores of each player
 * between rounds.
 * 
 * @author parker, Athens
 *
 * @version 09/03/2018
 */
@SuppressWarnings("serial")
public class GameScorePanel extends JPanel {

	private ViewDriver gui;
	private JPanel player1, player2, player3, player4;
	private JLabel player1Lab, player2Lab, player3Lab, player4Lab;

	/**
	 * Constructor for the LobbyQueue panel.
	 */
	public GameScorePanel(ViewDriver frame) {
		this.gui = frame;

		setLayout(null);
		this.setBounds(0, 0, 460, 768);

		JPanel panel = new JPanel();
		panel.setBounds(212, 0, 600, 120);
		panel.setBackground(new Color(127, 127, 127, 127));
		add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblTitle = new JLabel();
		if (!frame.getModel().gameOver) {
			lblTitle.setText("Scores So Far...");
			lblTitle.setHorizontalAlignment(0);
			panel.add(lblTitle);
			lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
			lblTitle.setForeground(Color.YELLOW);
		} else {
			lblTitle.setText("The Results are in!");
			lblTitle.setHorizontalAlignment(0);
			panel.add(lblTitle);
			lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 18));
			lblTitle.setForeground(Color.YELLOW);
		}

		JLabel playersInQueue = new JLabel("");
		playersInQueue.setHorizontalAlignment(0);
		panel.add(playersInQueue);
		playersInQueue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		playersInQueue.setForeground(Color.YELLOW);

		JPanel panelPlayers = new JPanel();
		panelPlayers.setBounds(40, 200, 944, 488);
		panelPlayers.setBackground(new Color(0, 0, 0, 0));
		panelPlayers.setLayout(null);
		add(panelPlayers);

		player1 = new JPanel();
		player1.setBounds(50, 20, 844, 97);
		player1.setBackground(new Color(127, 127, 127, 127));
		panelPlayers.add(player1);

		player2 = new JPanel();
		player2.setBounds(50, 138, 844, 98);
		player2.setBackground(new Color(127, 127, 127, 127));
		panelPlayers.add(player2);

		player3 = new JPanel();
		player3.setBounds(50, 254, 844, 98);
		player3.setBackground(new Color(127, 127, 127, 127));
		panelPlayers.add(player3);

		player4 = new JPanel();
		player4.setBounds(50, 372, 844, 98);
		player4.setBackground(new Color(127, 127, 127, 127));
		panelPlayers.add(player4);

		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/pcb.jpg")));
		backgroundLabel.setBounds(0, 0, 1024, 768);
		add(backgroundLabel);

		Map<User, Integer> players = gui.getModel().usersInLobby;
		String[] arr = new String[4];
		int i = 0;
		for (Map.Entry<User, Integer> u : players.entrySet()) {
			arr[i] = u.getKey().getUsername() + " : " + u.getValue();
			i++;
		}

		player1Lab = new JLabel(arr[0]);
		player1Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		player1Lab.setForeground(Color.YELLOW);
		player1.add(player1Lab);

		player2Lab = new JLabel(arr[1]);
		player2Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		player2Lab.setForeground(Color.YELLOW);
		player2.add(player2Lab);

		player3Lab = new JLabel(arr[2]);
		player3Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		player3Lab.setForeground(Color.YELLOW);
		player3.add(player3Lab);

		player4Lab = new JLabel(arr[3]);
		player4Lab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		player4Lab.setForeground(Color.YELLOW);
		player4.add(player4Lab);

		playersInQueue.setHorizontalAlignment(0);
		panel.add(playersInQueue);
		playersInQueue.setFont(new Font("Tahoma", Font.PLAIN, 15));
		playersInQueue.setForeground(Color.YELLOW);

		JPanel leaveBtnPan = new JPanel();
		leaveBtnPan.setBounds(362, 690, 300, 50);
		leaveBtnPan.setBackground(new Color(0, 0, 0, 0));
		add(leaveBtnPan);
		
		if (frame.getModel().gameOver) {
			JButton leaveLobby = new JButton("Back to Main Menu"); 
			leaveLobby.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					frame.addThisObserver();
					frame.getModel().enterMainMenu(frame.getModel().user);;
				}
			});
			leaveBtnPan.add(leaveLobby);
		}
	}

}