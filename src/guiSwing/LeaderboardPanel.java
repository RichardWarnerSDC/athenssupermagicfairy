package guiSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import general.User;

/**
 * Here is a class that we define to view a LeaderBoard of the top
 * 
 * @author parker, team athens
 * 
 * @version 13/03/2018
 */
@SuppressWarnings("serial")
public class LeaderboardPanel extends JPanel {

	private ViewDriver frame;
	private User[] leaders;

	/**
	 * Constructor for the LeaderboardPanel object.
	 */
	public LeaderboardPanel(ViewDriver frame) {
		this.frame = frame;
		setSize(1024, 768);
		setLayout(null);
		
		leaders = frame.getModel().leaders;
		for (User u : leaders) {
			if (u == null)
				continue;
			System.out.println(u.toString());
		}
		/*
		 * We receive an array of Users of Length 70. We split them up into the
		 * relevant parts for the modules: The order is: 1. Global, 2. SWW, 3.
		 * DSA, 4. Intro CS, 5. Databases, 6. AI, 7. OSN, 8. NISO
		 */
		JTabbedPane tabPane = new JTabbedPane();
		
		JPanel globalPanel = makeLeaderboard("Global", 0);
		JPanel swwPanel = makeLeaderboard("Software Workshop", 10);
		JPanel dsaPanel = makeLeaderboard("Data Structures", 20);
		JPanel introCSPanel = makeLeaderboard("Intro To CS", 30);
		JPanel dbPanel = makeLeaderboard("Databases", 40);
		JPanel aiPanel = makeLeaderboard("AI", 50);
		JPanel osnPanel = makeLeaderboard("OSN", 40);
		JPanel nisoPanel = makeLeaderboard("NISO", 50);
		
		//Maybe add icons here
		tabPane.addTab("Global", globalPanel);
		tabPane.setSelectedIndex(0);
		tabPane.addTab("SWW", swwPanel);
		tabPane.addTab("DSA", dsaPanel);
		tabPane.addTab("IntroToCS", introCSPanel);
		tabPane.addTab("DB", dbPanel);
		tabPane.addTab("AI", aiPanel);
		tabPane.addTab("OSN", osnPanel);
		tabPane.addTab("NISO", nisoPanel);
		
		setLayout(new GridLayout(1, 1));
		add(tabPane);
	}

	/**
	 * Makes a leaderboard for a specific type of game.
	 */
	private JPanel makeLeaderboard(String gameType, int startIndex) {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);
		
		JPanel topPanel = new JPanel();
		topPanel.setBounds(332, 0, 360, 80);
		topPanel.setBackground(new Color(127, 127, 127, 127));
		mainPanel.add(topPanel);
		topPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel topLabel1 = new JLabel("Top 10 players " + gameType + " :");
		topLabel1.setHorizontalAlignment(0);
		topPanel.add(topLabel1);
		topLabel1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		topLabel1.setForeground(Color.YELLOW);

		JLabel topLabel2 = new JLabel("How do they do it?!");
		topLabel2.setHorizontalAlignment(0);
		topPanel.add(topLabel2);
		topLabel2.setForeground(Color.YELLOW);
		topLabel2.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));

		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(null);
		playerPanel.setBounds(30, 110, 964, 540);
		playerPanel.setBackground(new Color(0, 0, 0, 0));
		mainPanel.add(playerPanel);

		Color panelColor = new Color(127, 127, 127, 127);

		JPanel no1Pan = new JPanel();
		no1Pan.setBounds(0, 0, 964, 92);
		no1Pan.setBackground(panelColor);
		playerPanel.add(no1Pan);

		JLabel no1 = new JLabel("1) THE CHAMPION : ");
		no1.setHorizontalAlignment(0);
		no1.setFont(new Font("Tahoma", Font.PLAIN, 32));
		no1.setForeground(Color.YELLOW);
		no1Pan.add(no1);

		JPanel no2Pan = new JPanel();
		no2Pan.setBounds(0, 112, 462, 92);
		no2Pan.setBackground(panelColor);
		playerPanel.add(no2Pan);

		JLabel no2 = new JLabel("2) THE CHALLENGER : ");
		no2.setHorizontalAlignment(0);
		no2.setFont(new Font("Tahoma", Font.PLAIN, 24));
		no2.setForeground(Color.YELLOW);
		no2Pan.add(no2);

		JPanel no3Pan = new JPanel();
		no3Pan.setBounds(502, 112, 462, 92);
		no3Pan.setBackground(panelColor);
		playerPanel.add(no3Pan);

		JLabel no3 = new JLabel("3) THE BEAST : ");
		no3.setHorizontalAlignment(0);
		no3.setFont(new Font("Tahoma", Font.PLAIN, 24));
		no3.setForeground(Color.YELLOW);
		no3Pan.add(no3);

		JPanel no4Pan = new JPanel();
		no4Pan.setBounds(0, 224, 462, 92);
		no4Pan.setBackground(panelColor);
		playerPanel.add(no4Pan);

		JLabel no4 = new JLabel("Player 4 : ");
		no4.setHorizontalAlignment(0);
		no4.setFont(new Font("Tahoma", Font.PLAIN, 22));
		no4.setForeground(Color.YELLOW);
		no4Pan.add(no4);

		JPanel no5Pan = new JPanel();
		no5Pan.setBounds(502, 224, 462, 92);
		no5Pan.setBackground(panelColor);
		playerPanel.add(no5Pan);

		JLabel no5 = new JLabel("Player 5 : ");
		no5.setHorizontalAlignment(0);
		no5.setFont(new Font("Tahoma", Font.PLAIN, 22));
		no5.setForeground(Color.YELLOW);
		no5Pan.add(no5);

		JPanel no6Pan = new JPanel();
		no6Pan.setBounds(0, 336, 462, 92);
		no6Pan.setBackground(panelColor);
		playerPanel.add(no6Pan);

		JLabel no6 = new JLabel("Player 6 : ");
		no6.setHorizontalAlignment(0);
		no6.setFont(new Font("Tahoma", Font.PLAIN, 18));
		no6.setForeground(Color.YELLOW);
		no6Pan.add(no6);

		JPanel no7Pan = new JPanel();
		no7Pan.setBounds(502, 336, 462, 92);
		no7Pan.setBackground(panelColor);
		playerPanel.add(no7Pan);

		JLabel no7 = new JLabel("Player 7 : ");
		no7.setHorizontalAlignment(0);
		no7.setFont(new Font("Tahoma", Font.PLAIN, 18));
		no7.setForeground(Color.YELLOW);
		no7Pan.add(no7);

		JPanel no8Pan = new JPanel();
		no8Pan.setBounds(0, 448, 294, 92);
		no8Pan.setBackground(panelColor);
		playerPanel.add(no8Pan);

		JLabel no8 = new JLabel("Player 8 : ");
		no8.setHorizontalAlignment(0);
		no8.setFont(new Font("Tahoma", Font.PLAIN, 18));
		no8.setForeground(Color.YELLOW);
		no8Pan.add(no8);

		JPanel no9Pan = new JPanel();
		no9Pan.setBounds(334, 448, 294, 92);
		no9Pan.setBackground(panelColor);
		playerPanel.add(no9Pan);

		JLabel no9 = new JLabel("Player 9 : ");
		no9.setHorizontalAlignment(0);
		no9.setFont(new Font("Tahoma", Font.PLAIN, 18));
		no9.setForeground(Color.YELLOW);
		no9Pan.add(no9);

		JPanel no10Pan = new JPanel();
		no10Pan.setBounds(668, 448, 294, 92);
		no10Pan.setBackground(panelColor);
		playerPanel.add(no10Pan);

		JLabel no10 = new JLabel("Player 10 : ");
		no10.setHorizontalAlignment(0);
		no10.setFont(new Font("Tahoma", Font.PLAIN, 18));
		no10.setForeground(Color.YELLOW);
		no10Pan.add(no10);

		JLabel[] labs = new JLabel [] {no1, no2, no3, no4, no5, no6, no7, no8, no9, no10};
		for (int i = 0; i < labs.length - 1; i++) {
			if (leaders[startIndex + i] != null) {
				labs[i].setText(labs[i].getText() + leaders[startIndex + i].getUsername() + " - " + leaders[startIndex + i].getStringRank());
			}
		}
		
		mainPanel.add(playerPanel);

		JButton backButton = new JButton("Go Back");
		backButton.setToolTipText("Click to go back to the Main Menu");
		backButton.setBounds(452, 665, 120, 31);
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getModel().enterMainMenu(frame.getModel().user);
			}
		});
		mainPanel.add(backButton);

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
		mainPanel.add(backgroundLabel);
		
		return mainPanel;
	}

}
