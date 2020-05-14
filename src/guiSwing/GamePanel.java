/**
 * It gets real from here on in!
 * Grabbing a bacground piccy for use here 
 * @author Richard
 *
 */
package guiSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import general.User;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Observer {

	private static final int WRAP_SIZE = 35;

	private ViewDriver frame;
	private JButton answerA, answerB, answerC, answerD;
	private JLabel question;
	private JPanel answerPanel;

	public GamePanel(ViewDriver frame) {
		this.frame = frame;

		setLayout(null);

		setSize(1024, 768);

		JLabel uobLabel = new JLabel("");
		uobLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/uob.png")));
		uobLabel.setBounds(0, 0, 256, 306);
		add(uobLabel);

		JPanel playerPanel = new JPanel();
		playerPanel.setLayout(new GridLayout(4, 1, 0, 0));
		playerPanel.setBounds(0, 306, 256, 462);
		playerPanel.setBackground(Color.GREEN);

		JPanel player1Space = new JPanel();
		playerPanel.add(player1Space);
		JPanel player2Space = new JPanel();
		playerPanel.add(player2Space);
		JPanel player3Space = new JPanel();
		playerPanel.add(player3Space);
		JPanel player4Space = new JPanel();
		playerPanel.add(player4Space);
		add(playerPanel);

		User[] users = new User[4];

		Iterator iter = frame.getModel().usersInLobby.entrySet().iterator();
		for(int playerIndex = 0; playerIndex<4 ; playerIndex++) {
			if(iter.hasNext()) {
				Map.Entry<User, Integer> pair = (Map.Entry) iter.next();
				users[playerIndex]= pair.getKey();
			} else {
				users[playerIndex] = new User(" ", "", 0, 0);
			}
		}

		JLabel player1Lab = new JLabel(users[0].getUsername());
		JLabel player2Lab = new JLabel(users[1].getUsername());
		JLabel player3Lab = new JLabel(users[2].getUsername());
		JLabel player4Lab = new JLabel(users[3].getUsername());

				player1Space.add(player1Lab);
				player2Space.add(player2Lab);
				player3Space.add(player3Lab);
				player4Space.add(player4Lab);
		JPanel questionPanel = new JPanel();
		questionPanel.setBounds(256, 0, 768, 306);
		add(questionPanel);

		question = new JLabel();
		question.setFont(new Font("Tahoma", Font.PLAIN, 18));
		questionPanel.add(question);

		answerPanel = new JPanel();
		answerPanel.setBounds(256, 306, 768, 462);
		answerPanel.setLayout(new GridLayout(2, 2, 0, 0));

		answerA = new JButton();
		answerA.setHorizontalAlignment(0);
		answerA.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getClient().sendAnswerMessage(frame.getModel().user, 0);
			}
		});
		answerPanel.add(answerA);

		answerB = new JButton();
		answerB.setHorizontalAlignment(0);
		answerB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getClient().sendAnswerMessage(frame.getModel().user, 1);
			}
		});
		answerPanel.add(answerB);

		answerC = new JButton();
		answerC.setHorizontalAlignment(0);
		answerC.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getClient().sendAnswerMessage(frame.getModel().user, 2);
			}
		});
		answerPanel.add(answerC);

		answerD = new JButton();
		answerD.setHorizontalAlignment(0);
		answerD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getClient().sendAnswerMessage(frame.getModel().user, 3);
			}
		});
		answerPanel.add(answerD);
		
		add(answerPanel);

	}

	public static ArrayList<JButton> shuffleAnswers(ArrayList<JButton> buttons) {
		ArrayList<JButton> answersArray = new ArrayList<JButton>();
		ArrayList<JButton> workList = new ArrayList<JButton>();
		for (int i = 0; i < buttons.size(); i++) {
			workList.add(buttons.get(i));
		}

		int j = 0;
		while (workList.size() > 0) {
			int randomNum = (int) (Math.random() * workList.size());
			answersArray.add(workList.get(randomNum));
			workList.remove(randomNum);
			j++;
		}
		return answersArray;
	}

	/**
	 * This method updates the GameScreen with the current questions.
	 */
	@Override
	public void update(Observable o, Object arg) {
		int removeButton = frame.getModel().removedButton;

		if (removeButton == -1) {

			String[] options = frame.getModel().currentQuestion.getAnswers();

			question.setText(wrapText(frame.getModel().currentQuestion.getQuestion(), 70));

			answerA.setText(wrapText(options[0], 30));

			answerB.setText(wrapText(options[1], 30));

			answerC.setText(wrapText(options[2], 30));

			answerD.setText(wrapText(options[3], 30));

			ArrayList<JButton> buttons = new ArrayList<JButton>();
			buttons.add(answerA);
			buttons.add(answerB);
			buttons.add(answerC);
			buttons.add(answerD);

			buttons = shuffleAnswers(buttons);
			answerPanel.removeAll();
			answerPanel.add(buttons.get(0));
			answerPanel.add(buttons.get(1));
			answerPanel.add(buttons.get(2));
			answerPanel.add(buttons.get(3));

		} else {
			if (frame.getModel().playerClaimed.equals(frame.getModel().user)) {
				if (removeButton == 0) {
					answerA.setText(answerA.getText() + " claimed by YOU!");
					answerA.setBackground(Color.YELLOW);
				} else if (removeButton == 1) {
					answerB.setText(answerB.getText() + " claimed by YOU!");
					answerB.setBackground(Color.YELLOW);
				} else if (removeButton == 2) {
					answerC.setText(answerC.getText() + " claimed by YOU!");
					answerC.setBackground(Color.YELLOW);
				} else {
					answerD.setText(answerD.getText() + " claimed by YOU!");
					answerD.setBackground(Color.YELLOW);
				}
			} else {
				if (removeButton == 0) {
					answerA.setText(answerA.getText() + " claimed by : " + frame.getModel().playerClaimed.getUsername());
					answerA.setBackground(Color.CYAN);
				} else if (removeButton == 1) {
					answerB.setText(
							answerB.getText() + " claimed by : " + frame.getModel().playerClaimed.getUsername());
					answerB.setBackground(Color.CYAN);
				} else if (removeButton == 2) {
					answerC.setText(
							answerC.getText() + " claimed by : " + frame.getModel().playerClaimed.getUsername());
					answerC.setBackground(Color.CYAN);
				} else {
					answerD.setText(
							answerD.getText() + " claimed by : " + frame.getModel().playerClaimed.getUsername());
					answerD.setBackground(Color.CYAN);
				}
			}
		}
		validate();
	}

	/**
	 * This method wraps the text in the JButtons so that the text will not fall
	 * off of the JButton.
	 * 
	 * @param text is the String to be wrapped into the button
	 * @param size is the length of the word/string you want to wrap. normal length is 35.
	 * 
	 * @return a String which is the text but in a format so that if input into a button the text will wrap
	 */
	public static String wrapText(String text, int size) {
		int strLength = text.length();

		StringBuffer sb = new StringBuffer("");
		sb.append("<html><center>");
		while (strLength > 0) {
			if(size<strLength){
				sb.append(text.substring(0, size));
				sb.append("<br>");
				text = text.substring(size);
				strLength = text.length();
			} else {
				sb.append(text);
				sb.append("<br>");
				break;
			}
		}
		sb.append("</center></html>");
		System.out.println(sb.toString());
		return sb.toString();
	}

}
