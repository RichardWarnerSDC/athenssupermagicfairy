package guiSwing;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import client.Client;

import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

@SuppressWarnings("serial")
public class HelpMenuPanel extends JPanel {
	
	//private Scanner input;
	private JTextArea helpText;
	private JOptionPane errorDialog;
	
	/**
	 * Create the panel.
	 * @param viewDriver 
	 * @param client 
	 */
	public HelpMenuPanel(Client client, ViewDriver frame) {
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(331, 0, 360, 120);
		panel.setBackground(new Color(127, 127, 127, 127));
		add(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblWelcomeTo = new JLabel("Help");
		lblWelcomeTo.setHorizontalAlignment(0);
		panel.add(lblWelcomeTo);
		lblWelcomeTo.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblWelcomeTo.setForeground(Color.YELLOW);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.getModel().enterMainMenu(frame.getModel().user);
			}});
		btnBack.setBounds(452, 640, 120, 32);
		add(btnBack);
		
		JPanel helpPanel = new JPanel();
		helpPanel.setLayout(new BorderLayout());
		helpPanel.setBounds(128, 128, 768, 512);
		
		helpText = new JTextArea();
		JScrollPane scrollHelp = new JScrollPane(helpText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		DefaultCaret caret = (DefaultCaret) helpText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
	
		helpText.setText(getHelp());
		helpText.setEditable(false);

		helpText.setWrapStyleWord(true);
		helpText.setLineWrap(true);

		

		helpPanel.add(scrollHelp,BorderLayout.CENTER);
		add(helpPanel);
		
		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setIcon(new ImageIcon(WelcomePanel.class.getResource("/res/images/background3.jpg")));
		backgroundLabel.setBounds(0, 0, 1024, 768);
		add(backgroundLabel);


	}

	public String getHelp()
	{
		String helpString = "";
		BufferedReader bufferedReader;
		bufferedReader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/res/texts/help.txt")));
		try {
		    StringBuilder stringBuilder = new StringBuilder();
		    String string = bufferedReader.readLine();

		    while (string != null) {
		        stringBuilder.append(string);
		        stringBuilder.append("\n");
		        string = bufferedReader.readLine();
		    }
		    helpString = stringBuilder.toString();
		} 
		catch (IOException e1) 
		{
			errorDialog = new JOptionPane();
			errorDialog.setMessage("Unable to get help");
			this.remove(helpText);
			this.add(errorDialog);
			e1.printStackTrace();
		} 
		finally 
		{
			try {
				bufferedReader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return helpString;
	}
	
}
