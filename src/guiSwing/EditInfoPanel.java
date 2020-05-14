package guiSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import database.Queries;
import general.User;

/**
 * Here is a Panel that we use in order to allow the user to change their
 * information.
 * 
 * @author parker
 * 
 * @version 14/08/2018
 */
@SuppressWarnings("serial")
public class EditInfoPanel extends JPanel {

	private JTextField username;
	private JPasswordField password, verify;
	private JLabel avatarPictureLabel;
	private User newUser;
	private String newAvatar = "Charles Babbage";
	private static int newAv = 0;
	private static ImageIcon icon;
	
	public EditInfoPanel(ViewDriver frame) {
		setLayout(null);
		this.setBounds(0, 0, 1024, 768);

		JPanel topPanel = new JPanel();
		topPanel.setBounds(332, 0, 360, 120);
		topPanel.setBackground(new Color(127, 127, 127, 127));
		add(topPanel);
		topPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblWelcomeTo = new JLabel("Edit your information here");
		lblWelcomeTo.setHorizontalAlignment(0);
		topPanel.add(lblWelcomeTo);
		lblWelcomeTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWelcomeTo.setForeground(Color.YELLOW);

		JPanel picturePanel = new JPanel();
		picturePanel.setBounds(352, 200, 120, 40);
		picturePanel.setLayout(new GridLayout(0, 1, 0, 0));
		picturePanel.setBackground(new Color(127, 127, 127, 0));
		add(picturePanel);

		JPanel labelPanel = new JPanel();
		labelPanel.setBackground(new Color(127, 127, 127, 127));
		JLabel lblAvatar = new JLabel("Your Avatar:");
		lblAvatar.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblAvatar.setForeground(Color.YELLOW);

		labelPanel.add(lblAvatar);
		picturePanel.add(labelPanel);

		JPanel avatarPicturePanel = new JPanel();
		avatarPicturePanel.setBounds(450, 280, 100, 100);
		avatarPicturePanel.setBackground(new Color(0, 0, 0, 0));

		icon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/babbage.jpg"));
		avatarPictureLabel = new JLabel(icon);
		avatarPicturePanel.add(avatarPictureLabel);
		add(avatarPicturePanel);

		JPanel comboPanel = new JPanel();
		comboPanel.setLayout(new GridLayout());
		comboPanel.setBounds(472, 200, 200, 40);
		add(comboPanel);

		String[] avatars = { "Charles Babbage", "Justin Bieber", "Alonzo Church", "Edsgar W. Dijkstra",
				"Shafi Goldwasser", "Grace Hopper", "Donald Knuth", "Ada Lovelace", "John Von Neumann",
				"Claude Shannon", "Alan Turing" };
		JComboBox<String> avatarType = new JComboBox<String>(avatars);
		avatarType.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				newAvatar = avatarType.getSelectedItem().toString();
				if (newAvatar.equals("Charles Babbage")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(0);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 0;
				} else if (newAvatar.equals("Justin Bieber")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(1);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 1;
				} else if (newAvatar.equals("Alonzo Church")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(2);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 2;
				} else if (newAvatar.equals("Edsgar W. Dijkstra")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(3);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 3;
				} else if (newAvatar.equals("Shafi Goldwasser")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(4);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 4;
				} else if (newAvatar.equals("Grace Hopper")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(5);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 5;
				} else if (newAvatar.equals("Donald Knuth")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(6);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 6;
				} else if (newAvatar.equals("Ada Lovelace")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(7);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 7;
				} else if (newAvatar.equals("John Von Neumann")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(8);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 8;
				} else if (newAvatar.equals("Claude Shannon")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(9);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 9;
				} else if (newAvatar.equals("Alan Turing")) {
					avatarPicturePanel.removeAll();
					icon = getAvatar(10);
					avatarPictureLabel = new JLabel(icon);
					avatarPicturePanel.add(avatarPictureLabel);
					validate();
					newAv = 10;
				}
			}
		});
		comboPanel.add(avatarType);

		JPanel registerPanel = new JPanel();
		registerPanel.setBounds(372, 458, 280, 110);
		registerPanel.setBackground(new Color(191, 191, 191, 255));
		add(registerPanel);
		registerPanel.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(34, 12, 80, 24);
		registerPanel.add(lblUsername);

		username = new JTextField();
		username.setBounds(118, 12, 120, 24);
		registerPanel.add(username);
		username.setColumns(16);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(34, 44, 80, 24);
		registerPanel.add(lblPassword);

		password = new JPasswordField();
		password.setBounds(118, 44, 120, 24);
		registerPanel.add(password);
		password.setColumns(16);

		JLabel lblVerify = new JLabel("Verify:");
		lblVerify.setBounds(34, 76, 80, 24);
		registerPanel.add(lblVerify);

		verify = new JPasswordField();
		verify.setBounds(118, 76, 120, 24);
		registerPanel.add(verify);
		verify.setColumns(16);

		JButton btnChange = new JButton("Change Info");
		btnChange.setBounds(392, 600, 120, 32);
		btnChange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String newUsername = username.getText();
				String newPassword = password.getText();
				String newVerify = verify.getText();
				if (Queries.checkValidPassword(newUsername, newPassword) && Queries.checkValidUsername(newUsername)
						&& password.equals(newVerify)) {
					newUser = new User(newUsername, newPassword, 0, newAv, true);

					JFrame checkFrame = new JFrame("Connection Problem =[");
					checkFrame.setLocationRelativeTo(null);
					checkFrame.setSize(550, 250);
					checkFrame.setResizable(false);
					JPanel checkPanel = new JPanel();
					checkPanel.setBounds(50, 26, 450, 150);
					JTextArea checkMessage = new JTextArea(2, 30);
					checkMessage.setWrapStyleWord(true);
					checkMessage.setLineWrap(true);
					checkMessage.setOpaque(false);
					checkMessage.setEditable(false);
					checkMessage.setFocusable(false);
					checkMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
					checkMessage.setForeground(Color.RED);
					String text = "Are you sure you wish to change your user information? If you click yes your "
							+ "information will be permanently changed, click cancel to go back";
					checkMessage.setText(text);
					checkMessage.setForeground(Color.RED);

					JButton yesBtn = new JButton("Yes");
					yesBtn.setBounds(176, 130, 100, 50);
					yesBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							frame.getClient().sendChangeInfoMessage(newUser);
							checkFrame.dispose();
							// TODO Listen for a response from the server.
						}
					});
					checkFrame.add(yesBtn);

					JButton cancelBtn = new JButton("Cancel");
					cancelBtn.setBounds(276, 130, 100, 50);
					cancelBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							checkFrame.dispose();
						}
					});
					checkFrame.add(cancelBtn);
					checkFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					checkPanel.add(checkMessage);
					checkFrame.add(checkPanel);
					checkFrame.setVisible(true);
				} else { // When the password/username/verify pages are
							// incorrect.
					JFrame errorFrame = new JFrame("Password Error");
					errorFrame.setLocationRelativeTo(null);
					errorFrame.setSize(550, 250);
					errorFrame.setResizable(false);
					JPanel errorPanel = new JPanel();
					errorPanel.setBounds(0, 0, 500, 50);
					JLabel errorMessage = new JLabel();
					errorMessage.setHorizontalAlignment(0);
					errorMessage.setFont(new Font("Tahoma", Font.PLAIN, 18));
					errorMessage.setForeground(Color.RED);
					errorMessage.setText("Please enter a valid username and password.");

					JPanel correctInfo = new JPanel();
					correctInfo.setBounds(0, 50, 500, 1000);

					JLabel info = new JLabel("<html><ul>" + "<li>Your username must be between 2 - 15 characters.</li>"
							+ "<li>Username and password must not be the same.</li>"
							+ "<li>Password and Verify fields must be equal.</li>"
							+ "<li>Passwords must contain a letter and a number.</li>"
							+ "<li>Passwords must be between 8 - 20 characters.</li>" + "</ul><html>");
					info.setHorizontalAlignment(0);
					info.setFont(new Font("Tahoma", Font.PLAIN, 18));
					errorMessage.setForeground(Color.RED);

					JButton gotItBtn = new JButton("OK, Got it.");
					gotItBtn.setBounds(200, 176, 150, 30);
					gotItBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							errorFrame.dispose();// Close the frame
						}
					});
					errorFrame.add(gotItBtn);

					errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					correctInfo.add(info);
					errorPanel.add(errorMessage);
					errorPanel.add(correctInfo);
					errorFrame.add(errorPanel);
					errorFrame.setVisible(true);
				}
			}
		});
		add(btnChange);

		JButton btnSignUp = new JButton("Back");
		btnSignUp.setBounds(512, 600, 120, 32);
		btnSignUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getModel().enterMainMenu(frame.getModel().user);
			}
		});
		add(btnSignUp);

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
	}

	public static ImageIcon getAvatar(int avatar)
	{
		ImageIcon avatarIcon = new ImageIcon();
		if (avatar == 0)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/babbage.jpg"));
		} else if (avatar == 1)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/bieber.jpg"));
		} else if (avatar == 2)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/church.jpg"));
		} else if (avatar == 3)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/dijkstra.jpg"));
		} else if (avatar == 4)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/goldwasser.jpg"));
		} else if (avatar == 5)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/hopper.jpg"));
		} else if (avatar == 6)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/knuth.jpg"));
		} else if (avatar == 7)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/lovelace.jpg"));
		} else if (avatar == 8)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/neumann.jpg"));
		} else if (avatar == 9)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/shannon.jpg"));
		} else if (avatar == 10)
		{
			avatarIcon = new ImageIcon(EditInfoPanel.class.getResource("/res/avatars/turing.jpg"));
		}
		return avatarIcon;
	}
	
}