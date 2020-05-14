package general;

import java.util.Arrays;

public class Question {
	public String question;
	public String[] answers;
	public int answer;
	
	public Question(String question, String[] answers, int answer) {
		this.question=question;
		this.answers=answers;
		this.answer=answer;
	}

	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

	public int getAnswer() {
		return answer;
	}
	
	@Override
	public String toString() {
		return getQuestion() + "  |  " + Arrays.toString(getAnswers()) + "  |  " + getAnswer();
	}

}
