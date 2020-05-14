package guiSwing;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GridLayout;

/**
 * PlayerPanel allows us to more easily manipulate and display player information between screens
 * Bsically a placeholder for displaying player information.
 * @author Richard
 *
 */
@SuppressWarnings("serial")
public class PlayerPanel extends JPanel{

	private String username;
	private String rank;
	private String hiScore;
	private String score;
	private JLabel avatar;
	private JPanel infoPane;
	private JLabel usernameLabel = new JLabel("");
	private JLabel rankLabel = new JLabel("");
	private JLabel scoreLabel = new JLabel("");
	
	public PlayerPanel()
	{
		setLayout(new GridLayout(2, 0, 0, 0));
		setSize(256, 128);
		setBackground(new Color(127, 127, 127, 127));
		
//		avatar = new JLabel("");
//		avatar.setBounds(0, 0, 128, 128);
//		add(avatar);
		
		infoPane = new JPanel();
		infoPane.setBounds(128, 0, 128, 128);
		usernameLabel = new JLabel(username);
		infoPane.add(usernameLabel);
		rankLabel = new JLabel(rank);
		infoPane.add(rankLabel);
		scoreLabel = new JLabel(score);
		infoPane.add(rankLabel);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getHiScore() {
		return hiScore;
	}

	public void setHiScore(String hiScore) {
		this.hiScore = hiScore;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}



	public JLabel getAvatar() {
		return avatar;
	}



	public void setAvatar(JLabel avatar) {
		this.avatar = avatar;
	}

	public JPanel getInfoPane() {
		return infoPane;
	}

	public JLabel getUsernameLabel() {
		return usernameLabel;
	}

	public JLabel getRankLabel() {
		return rankLabel;
	}

	public JLabel getScoreLabel() {
		return scoreLabel;
	}

	public void setUsernameLabelText(String text)
	{
		usernameLabel.setText(text);
	}
	
	public void setRankLabelText(String text)
	{
		rankLabel.setText(text);
	}
	
	public void setScoreLabelTexzt(String text)
	{
		scoreLabel.setText(text);
	}
}
