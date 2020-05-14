package guiSwing;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import client.Client;
import database.Queries;
import general.User;

public class RegisterPanel extends JPanel {

	private ViewDriver frame;
	private JTextField txUsername;
	private JPasswordField txPassword;
	private JPasswordField txVerify;
	private boolean popupFrame = false;

	/**
	 * Constructor for a dummy login panel.
	 */
	public RegisterPanel(ViewDriver frame) {

		this.frame = frame;
		setLayout(null);
		this.setBounds(0, 0, 460, 768);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 1024, 120);
		panel.setBackground(new Color(127, 127, 127, 127));
		add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblWelcomeTo = new JLabel("Register a new user");
		lblWelcomeTo.setHorizontalAlignment(0);
		panel.add(lblWelcomeTo);
		lblWelcomeTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWelcomeTo.setForeground(Color.YELLOW);

		JPanel registerPanel = new JPanel();
		registerPanel.setBounds(372, 458, 280, 110);
		registerPanel.setBackground(new Color(191, 191, 191, 255));
		add(registerPanel);
		registerPanel.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(34, 12, 80, 24);
		registerPanel.add(lblUsername);

		txUsername = new JTextField();
		txUsername.setBounds(118, 12, 120, 24);
		registerPanel.add(txUsername);
		txUsername.setColumns(16);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(34, 44, 80, 24);
		registerPanel.add(lblPassword);

		txPassword = new JPasswordField();
		txPassword.setBounds(118, 44, 120, 24);
		registerPanel.add(txPassword);
		txPassword.setColumns(16);

		JLabel lblVerify = new JLabel("Verify:");
		lblVerify.setBounds(34, 76, 80, 24);
		registerPanel.add(lblVerify);

		txVerify = new JPasswordField();
		txVerify.setBounds(118, 76, 120, 24);
		registerPanel.add(txVerify);
		txVerify.setColumns(16);

		JButton btnNewButton = new JButton("Register");
		
		InputMap im = btnNewButton.getInputMap(WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = btnNewButton.getActionMap();
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "clickMe");
        am.put("clickMe", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.doClick();
            }
        });
        
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Queries.checkValidUsername(txUsername.getText())
						&& Queries.checkValidPassword(txUsername.getText(), txPassword.getText())
						&& txPassword.getText().equals(txVerify.getText())) {
					User newUser = new User(txUsername.getText(), txPassword.getText(), 0, 0, true); //Added a true here.
					frame.getClient().sendRegisterMessage(newUser);
				} else {
					System.out.println("Passwords don't match!");
					txPassword.setText("");
					txVerify.setText("");

					if (!popupFrame) { // popupFrame is to allow us to make only
										// one frame pop up at any given time,
										// its not currently implemented.
						/*
						 * Here, we open an error frame which tells the user the
						 * correct format of a username and password.
						 */
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
								+ "<li>Username and Password can only contain letters and numbers.</li>"
								+ "<li>Password and Verify fields must be equal.</li>"
								+ "<li>Passwords must be between 5 - 20 characters.</li>" + "</ul><html>");
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

			}
		});

		btnNewButton.setBounds(392, 600, 112, 32);
		add(btnNewButton);

		JButton btnSignUp = new JButton("Back");
		btnSignUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getModel().enterWelcomePage();

			}
		});
		btnSignUp.setBounds(524, 600, 120, 32);
		add(btnSignUp);

		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background3.jpg")));
		backgroundLabel.setBounds(0, 0, 1024, 768);
		add(backgroundLabel);
	}
	
}
