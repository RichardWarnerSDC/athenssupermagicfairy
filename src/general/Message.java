package general;

import java.io.Serializable;
import java.util.Map;

/** This Message class allows us to pass Message objects in our
 *  communication protocols between the server and the client.
 * 
 *  @author Ed Wong, Team Athens
 *  @version 1/3/2018
 */

public class Message implements Serializable{
	private int header;
	private User user;
	private String text;
	private String question;
	private String[] options;
	private int answer;
	private User[] queueing;
	private Map<User,Integer> scores;
	
	public Message(int header, User user, String text, String question, String[] options, int answer){
		this.header = header;
		this.user = user;
		this.text = text;
		this.question = question;
		this.options = options;
		this.answer = answer;
		this.queueing = null;
	}

	public Message(int header, User user, String text, String question, String[] options, int answer, User[] queueing) {
		this.header = header;
		this.user = user;
		this.text = text;
		this.question = question;
		this.options = options;
		this.answer = answer;
		this.queueing = queueing;
	}
	
	public Message(int header, Map<User,Integer> scores) {
		this.header = header;
		this.scores = scores;
	}

	public int getHeader() {
		return header;
	}

	public User getUser() {
		return user;
	}

	public String getText() {
		return text;
	}

	public String getQuestion() {
		return question;
	}

	public String[] getOptions() {
		return options;
	}

	public int getAnswer() {
		return answer;
	}
	
	public User[] getQueueing(){
		return queueing;
	}
	
	public Map<User,Integer> getScores(){
		return scores;
	}

	public String toString() {
		return header+" "+user+" "+
				text+" "+question+" "+
				options+" "+answer+" "+
				queueing;
	}
}