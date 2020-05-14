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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import client.Client;
import general.User;

public class LoginPanel extends JPanel {
	
	private JTextField txUsername;
	private JTextField txPassword;
	
	/**
	 * Constructor for a dummy login panel. Hello
	 */
	public LoginPanel(ViewDriver frame) {

		setLayout(null);
		this.setBounds(0, 0, 460, 768);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 1024, 120);
		panel.setBackground(new Color(127, 127, 127, 127));
		add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblWelcomeTo = new JLabel("Login Page");
		lblWelcomeTo.setHorizontalAlignment(0);
		panel.add(lblWelcomeTo);
		lblWelcomeTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWelcomeTo.setForeground(Color.YELLOW);
		
		JPanel loginPanel = new JPanel();
		loginPanel.setBounds(372, 480, 280, 78);
		loginPanel.setBackground(new Color(191, 191, 191, 255));
		add(loginPanel);
		loginPanel.setLayout(null);

		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(34, 12, 80, 24);
		loginPanel.add(lblUsername);
		
		txUsername = new JTextField();
		txUsername.setBounds(117, 12, 120, 24);
		loginPanel.add(txUsername);
		txUsername.setColumns(16);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(34, 44, 80, 24);
		loginPanel.add(lblPassword);
		
		txPassword = new JPasswordField();
		txPassword.setBounds(118, 44, 120, 24);
		loginPanel.add(txPassword);
		txPassword.setColumns(16);
		
		JButton btnNewButton = new JButton("Sign In");
		
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
				User newUser = new User(txUsername.getText(), txPassword.getText(), 0, 0);
				frame.getClient().sendConnectMessage(newUser);
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
