Section 1 - Project Description
1.1 Project Name: Quiz Application
1.2 Description: This project involves developing a client-server quiz application where users can take quizzes on various subjects (Physics, Chemistry, and Math). The server stores quiz questions and manages the quiz state, while the client provides a user interface for selecting topics, answering questions, and displaying scores.
Section 2 - Overview
2.1 Purpose: To create an interactive quiz application that allows users to test their knowledge in different subjects. The intended audience is students and educators.
2.2 Scope: This module covers the client and server implementation for the quiz application, including the user interface design, question management, and score tracking.
2.3 Requirements:
2.3.1 Functional Requirements:
R1: Users can select a quiz topic from Physics, Chemistry, or Math. Big O: O(1) - Topic selection is a constant time operation.
R2: Display a series of 10 questions for the selected topic. Big O: O(1) - Retrieving a fixed number of questions.
R3: Allow users to answer each question. Big O: O(1) - Each answer submission is a constant time operation.
R4: Provide feedback on whether the answer was correct or incorrect. Big O: O(1) - Feedback is given in constant time after each submission.
R5: Display the final score at the end of the quiz. Big O: O(1) - Calculating score from a fixed set of responses.
R6: Include a countdown timer of 5 minutes for the quiz. Big O: O(1) - Timer management is constant in this context.
2.3.2 Non-Functional Requirements:
Performance: Handle up to 100 concurrent users without performance degradation. Big O: O(n) - Performance scales linearly with the number of users.
Reliability: Achieve 99.9% uptime. Big O: O(1) - Constant monitoring required to maintain uptime.
2.3.3 Technical Requirements:
Hardware: Run on servers with at least 8GB of RAM and 2 CPUs.
Software: Developed using Java and Swing for the client, and Java for the server.
2.3.4 Security Requirements:
Data Encryption: Encrypt all data transferred between client and server using TLS. Big O: O(n) - Encryption time scales with the amount of data transferred.
Section 3 - System Architecture
3.1 Overview: The system consists of a client-server architecture where the client is responsible for the user interface and the server manages the quiz logic and data. Big O: O(n) - Overall system complexity scales with the number of concurrent users and operations performed.
Section 4 - User Interface Design
4.1 Overview: The UI includes a topic selection screen, quiz question display, feedback for answers, and a final score display.
4.2 Navigation Flow: Topic Selection Screen → Quiz Question Screen → Feedback Screen → Final Score Screen
4.3 Use Cases / User Function Description:
Use Case 1: User selects a quiz topic.
Use Case 2: User answers a series of 10 questions.
Use Case 3: User receives feedback on each answer.
Use Case 4: User views the final score.
Section 5 - Testing
5.1 Test Plan Creation:
Objective: Validate the functionality and performance of the quiz application.
Scope: Includes functional testing, performance testing, and security testing.
Resources: Details the personnel, tools, and environments required.
Schedule: Details the timeline for testing activities.
Test Cases: Define individual tests with steps, expected results, and actual results.
Section 6 - Monitoring
Key Metrics:
Performance Metrics: Response times, throughput, server load, CPU usage, memory usage.
Error Metrics: Error rates, types of errors, affected users.
Availability Metrics: Uptime, downtime, service availability.
User Metrics: Active users, user sessions, retention rates.
Section 7 - Other Interfaces
7.1 Interface X: Interaction with external systems or APIs if applicable.
