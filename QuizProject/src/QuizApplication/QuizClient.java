package QuizApplication;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class QuizClient extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel panel;
    private JComboBox<String> topicSelector;
    private JButton startButton, submitButton, nextButton, finishButton;
    private JLabel timerLabel, statusLabel, questionLabel;
    private ButtonGroup optionsGroup;
    private JRadioButton[] optionButtons;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private Question currentQuestion;
    private int timeLeft = 300; // 5 minutes in seconds
    private Timer quizTimer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new QuizClient().setVisible(true));
    }

    public QuizClient() {
        setTitle("Quiz Client");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new JPanel(new BorderLayout());
        add(panel);

        topicSelector = new JComboBox<>(new String[]{"physics", "chemistry", "maths"});
        startButton = new JButton("Start Quiz");
        startButton.addActionListener(e -> startQuiz());
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(e -> submitAnswer());
        nextButton = new JButton("Next Question");
        nextButton.addActionListener(e -> getNextQuestion());
        finishButton = new JButton("Finish Attempt");
        finishButton.addActionListener(e -> finishQuiz());

        timerLabel = new JLabel("Time left: " + timeLeft + " seconds");
        statusLabel = new JLabel("");
        questionLabel = new JLabel("");
        optionsGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionsGroup.add(optionButtons[i]);
        }

        JPanel controlPanel = new JPanel();
        controlPanel.add(topicSelector);
        controlPanel.add(startButton);
        controlPanel.add(submitButton);
        controlPanel.add(nextButton);
        controlPanel.add(finishButton);
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(timerLabel, BorderLayout.SOUTH);
        panel.add(statusLabel, BorderLayout.CENTER);

        JPanel questionPanel = new JPanel(new GridLayout(0, 1));
        questionPanel.add(questionLabel);
        for (JRadioButton optionButton : optionButtons) {
            questionPanel.add(optionButton);
        }
        panel.add(questionPanel, BorderLayout.CENTER);

        disableQuizControls();

        quizTimer = new Timer(1000, e -> updateTimer());
    }

    private void startQuiz() {
        try {
            socket = new Socket("localhost", 12345);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            out.writeObject(topicSelector.getSelectedItem().toString());
            enableQuizControls();
            startButton.setEnabled(false);
            nextButton.setEnabled(false);
            getNextQuestion();
            quizTimer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void submitAnswer() {
        try {
            String selectedAnswer = null;
            for (JRadioButton optionButton : optionButtons) {
                if (optionButton.isSelected()) {
                    selectedAnswer = optionButton.getText();
                    break;
                }
            }
            if (selectedAnswer != null) {
                out.writeObject(selectedAnswer);
                boolean correct = (boolean) in.readObject();
                statusLabel.setText(correct ? "Correct!" : "Incorrect!");
                nextButton.setEnabled(true);
                submitButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an answer!");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getNextQuestion() {
        try {
            currentQuestion = (Question) in.readObject();
            if (currentQuestion != null) {
                questionLabel.setText(currentQuestion.getQuestionText());
                List<String> options = currentQuestion.getOptions();
                for (int i = 0; i < options.size(); i++) {
                    optionButtons[i].setText(options.get(i));
                    optionButtons[i].setSelected(false);
                }
                optionsGroup.clearSelection();
                submitButton.setEnabled(true);
                nextButton.setEnabled(false);
                statusLabel.setText("");
            } else {
                endQuiz();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void finishQuiz() {
        endQuiz();
    }

    private void endQuiz() {
        try {
            out.writeObject("finish");
            int score = (int) in.readObject();
            statusLabel.setText("Quiz Over! Your score: " + score);
            disableQuizControls();
            quizTimer.stop();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void disableQuizControls() {
        submitButton.setEnabled(false);
        nextButton.setEnabled(false);
        finishButton.setEnabled(false);
        for (JRadioButton optionButton : optionButtons) {
            optionButton.setEnabled(false);
        }
    }

    private void enableQuizControls() {
        for (JRadioButton optionButton : optionButtons) {
            optionButton.setEnabled(true);
        }
        finishButton.setEnabled(true);
    }

    private void updateTimer() {
        if (timeLeft > 0) {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft + " seconds");
        } else {
            endQuiz();
        }
    }
}
