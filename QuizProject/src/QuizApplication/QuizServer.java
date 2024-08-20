package QuizApplication;

import java.io.*;
import java.net.*;
import java.util.*;

public class QuizServer {
    private static final int PORT = 12345;
    private static Map<String, List<Question>> quizzes = new HashMap<>();

    public static void main(String[] args) {
        quizzes.put("physics", generatePhysicsQuestions());
        quizzes.put("chemistry", generateChemistryQuestions());
        quizzes.put("maths", generateMathsQuestions());

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Quiz Server is running...");
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Question> generatePhysicsQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What is the speed of light?", Arrays.asList("3x10^8 m/s", "3x10^6 m/s", "3x10^5 m/s", "3x10^7 m/s"), "3x10^8 m/s"));
        questions.add(new Question("What is the unit of force?", Arrays.asList("Newton", "Pascal", "Joule", "Watt"), "Newton"));
        questions.add(new Question("What is the acceleration due to gravity on Earth?", Arrays.asList("9.8 m/s^2", "9.8 km/s^2", "8.9 m/s^2", "8.9 km/s^2"), "9.8 m/s^2"));
        questions.add(new Question("Which particle has no charge?", Arrays.asList("Electron", "Proton", "Neutron", "Photon"), "Neutron"));
        questions.add(new Question("What is the first law of thermodynamics?", Arrays.asList("Energy cannot be created or destroyed", "F=ma", "For every action, there is an equal and opposite reaction", "E=mc^2"), "Energy cannot be created or destroyed"));
        return questions;
    }

    private static List<Question> generateChemistryQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What is the chemical symbol for water?", Arrays.asList("H2O", "O2", "CO2", "NaCl"), "H2O"));
        questions.add(new Question("What is the atomic number of carbon?", Arrays.asList("6", "8", "12", "14"), "6"));
        questions.add(new Question("What is the pH of pure water?", Arrays.asList("7", "1", "5", "10"), "7"));
        questions.add(new Question("Which element is known as the 'King of Chemicals'?", Arrays.asList("Sulfuric Acid", "Nitric Acid", "Hydrochloric Acid", "Acetic Acid"), "Sulfuric Acid"));
        questions.add(new Question("What is the common name for NaCl?", Arrays.asList("Salt", "Sugar", "Baking Soda", "Bleach"), "Salt"));
        return questions;
    }

    private static List<Question> generateMathsQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("What is 2+2?", Arrays.asList("3", "4", "5", "6"), "4"));
        questions.add(new Question("What is the square root of 16?", Arrays.asList("2", "3", "4", "5"), "4"));
        questions.add(new Question("What is the value of pi?", Arrays.asList("3.14", "2.71", "1.61", "1.41"), "3.14"));
        questions.add(new Question("What is 15/3?", Arrays.asList("3", "4", "5", "6"), "5"));
        questions.add(new Question("What is the perimeter of a rectangle with length 4 and width 3?", Arrays.asList("7", "10", "12", "14"), "14"));
        return questions;
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                String quizTopic = (String) in.readObject();
                List<Question> questions = quizzes.get(quizTopic);
                QuizGame quizGame = new QuizGame(questions);

                out.writeObject(quizGame.getNextQuestion());

                String userAnswer;
                while ((userAnswer = (String) in.readObject()) != null) {
                    if (userAnswer.equals("finish")) {
                        out.writeObject(quizGame.getScore());
                        break;
                    }
                    boolean correct = quizGame.checkAnswer(userAnswer);
                    out.writeObject(correct);
                    Question nextQuestion = quizGame.getNextQuestion();
                    out.writeObject(nextQuestion);
                    if (nextQuestion == null) {
                        out.writeObject(quizGame.getScore());
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
/////
