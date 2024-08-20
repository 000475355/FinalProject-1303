package QuizApplication;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import java.net.*;

public class QuizApplicationTest {
    private Question question;
    private QuizGame quizGame;
    private QuizClient quizClient;
    private QuizServer quizServer;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private List<Question> questions;

    @Before
    public void setUp() throws IOException {
        // Setup for Question
        question = new Question("What is the capital of France?", Arrays.asList("Paris", "London", "Berlin", "Madrid"), "Paris");

        // Setup for QuizGame
        questions = Arrays.asList(
            new Question("What is 2+2?", Arrays.asList("1", "2", "3", "4"), "4"),
            new Question("What is the capital of Italy?", Arrays.asList("Rome", "Milan", "Naples", "Turin"), "Rome")
        );
        quizGame = new QuizGame(questions);

        // Setup server and client for testing QuizClient and QuizServer
        serverSocket = new ServerSocket(0); // listen on any free port
        quizServer = new QuizServer(serverSocket);
        clientSocket = new Socket("localhost", serverSocket.getLocalPort());
        quizClient = new QuizClient();
        quizClient.connectToServer(clientSocket);

        // Starting server and client in separate threads to handle requests
        new Thread(() -> {
            try {
                quizServer.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            quizClient.startQuiz();
        }).start();
    }

    @Test
    public void testQuestionAttributes() {
        assertEquals("What is the capital of France?", question.getQuestionText());
        assertEquals(Arrays.asList("Paris", "London", "Berlin", "Madrid"), question.getOptions());
        assertEquals("Paris", question.getCorrectAnswer());
    }

    @Test
    public void testGetNextQuestion() {
        assertEquals(questions.get(0), quizGame.getNextQuestion());
        quizGame.getNextQuestion(); // move to next question for proper sequence
        assertEquals(questions.get(1), quizGame.getNextQuestion());
    }

    @Test
    public void testCheckAnswer() {
        quizGame.getNextQuestion(); // Move to first question
        assertTrue(quizGame.checkAnswer("4"));
        assertFalse(quizGame.checkAnswer("3"));
    }

    @Test
    public void testGetScore() {
        quizGame.getNextQuestion(); // Move to first question
        quizGame.checkAnswer("4"); // Correct answer
        quizGame.getNextQuestion(); // Move to second question
        quizGame.checkAnswer("Milan"); // Incorrect answer
        assertEquals(1, quizGame.getScore());
    }

    @Test
    public void testClientServerInteraction() {
        quizClient.submitAnswer("4");
        assertEquals(1, quizClient.getScore());
    }

    @Test
    public void testServerHandling() {
        assertNotNull(quizServer.acceptClientConnection());
        assertTrue(quizServer.handleClient(clientSocket));
    }

    @After
    public void tearDown() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }
}
