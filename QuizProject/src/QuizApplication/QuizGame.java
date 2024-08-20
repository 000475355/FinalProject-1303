package QuizApplication;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuizGame implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Question> questions;
    private AtomicInteger currentQuestionIndex;
    private AtomicInteger score;

    public QuizGame(List<Question> questions) {
        this.questions = questions;
        this.currentQuestionIndex = new AtomicInteger(0);
        this.score = new AtomicInteger(0);
    }

    public Question getNextQuestion() {
        if (currentQuestionIndex.get() < questions.size()) {
            return questions.get(currentQuestionIndex.getAndIncrement());
        } else {
            return null;
        }
    }

    public boolean checkAnswer(String answer) {
        Question currentQuestion = questions.get(currentQuestionIndex.get() - 1);
        boolean correct = currentQuestion.getCorrectAnswer().equals(answer);
        if (correct) {
            score.incrementAndGet();
        }
        return correct;
    }

    public int getScore() {
        return score.get();
    }
}
