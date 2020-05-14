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

/**
 * Here is the first JPanel that a user will see when they launch the program.
 * @author Parker, Richard
 * @version 17.03.2018.
 */
public class WelcomePanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public WelcomePanel(ViewDriver frame) {
		setSize(1024, 768);
		setLayout(null);
		
		// Semi-transparent header panel that displays the panel's title more visibly.
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(0, 1, 0, 0));
		headerPanel.setBounds(0, 0, 1024, 120); 
		headerPanel.setBackground(new Color(127, 127, 127, 127));
		add(headerPanel);

		// WelcomePanel's title part I.
		JLabel welcomeLabel = new JLabel("Welcome to");
		welcomeLabel.setForeground(Color.YELLOW);
		welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		welcomeLabel.setHorizontalAlignment(0);
		headerPanel.add(welcomeLabel);
 
		// WelcomePanel's title part II.
		JLabel mscRevQuiz = new JLabel("Super Magic Fairy fun Quiz!");
		mscRevQuiz.setForeground(Color.YELLOW);
		mscRevQuiz.setFont(new Font("Comic Sans MS", Font.PLAIN, 24));
		mscRevQuiz.setHorizontalAlignment(0);
		headerPanel.add(mscRevQuiz);

		/** Login button.
		* When clicked calls frame's model's userLoginMenu method which removes WelcomePanel and replaces it with
		* LoginPanel.
		*/
		JButton loginButton = new JButton("Login!");
		loginButton.setBounds(392, 600, 112, 32);
		loginButton.setToolTipText("Click to sign in.");
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getModel().enterLoginMenu(); 
			}
		});
		add(loginButton);
		
		/** Register button.
		* When clicked calls frame's model's enterRegisterMenu method which removes WelcomePanel and replaces it with
		* RegisterPanel.
		*/
		JButton registerButton = new JButton("Register");
		registerButton.setToolTipText("Click to create a new user");
		registerButton.setBounds(524, 600, 120, 32);
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getModel().enterRegisterMenu(false);
			}
		});
		add(registerButton);

		// Background label displays an image without text overlapping. 
		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background3.jpg")));
		backgroundLabel.setBounds(0, 0, 1024, 768);
		add(backgroundLabel);
	
	}
	
}
