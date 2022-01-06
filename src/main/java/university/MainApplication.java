package university;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import university.controller.RegistrationSystem;
import university.entities.Student;
import university.entities.Teacher;
import university.exceptions.NullException;
import university.exceptions.WrongInputException;
import university.repository.CourseJdbcRepository;
import university.repository.StudentJdbcRepository;
import university.repository.TeacherJdbcRepository;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApplication extends javafx.application.Application {
    RegistrationSystem controller;
    Student student;
    Teacher teacher;

    /**
     * Setup and functionality for GUI
     * @param stage
     * @throws SQLException
     */
    @Override
    public void start(Stage stage) throws SQLException {

        String connectionUrl = "jdbc:mysql://localhost:3306/universitysql";
        String user = "root";
        String password = "1234";
        Connection connection = DriverManager.getConnection(connectionUrl, user, password);;
        TeacherJdbcRepository teacherRepository = new TeacherJdbcRepository(connectionUrl, user, password, connection);
        StudentJdbcRepository studentRepository = new StudentJdbcRepository(connectionUrl, user, password, connection);
        CourseJdbcRepository courseRepository = new CourseJdbcRepository(connectionUrl, user, password, connection);

        controller = new RegistrationSystem(teacherRepository, studentRepository, courseRepository);

        // Login window
        GridPane loginLayout = new GridPane();
        Scene loginScene = new Scene(loginLayout,350,200);
        stage.setScene(loginScene);
        loginLayout.setHgap(10);
        loginLayout.setVgap(10);
        stage.setTitle("Login");
        Label loginLabel = new Label("Login as: ");
        loginLabel.setFont(Font.font("Calibri", 18));
        loginLayout.add(loginLabel,9,1);

        Button studentButton = new Button("Student");
        studentButton.setFont(Font.font("Calibri", 17));
        HBox hbBtn1 = new HBox(10);
        hbBtn1.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn1.getChildren().add(studentButton);
        loginLayout.add(hbBtn1, 9, 2);

        Button teacherButtton = new Button("Teacher");
        teacherButtton.setFont(Font.font("Calibri", 17));
        HBox hbBtn2 = new HBox(10);
        hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn2.getChildren().add(teacherButtton);
        loginLayout.add(hbBtn2, 10, 2);

        // Student login window
        studentButton.setOnAction(event -> {
            GridPane studentLayout = new GridPane();
            Scene loginScene1 = new Scene(studentLayout,400,200);
            Stage newWindow = new Stage();
            newWindow.setTitle("Student Login");
            newWindow.setScene(loginScene1);
            studentLayout.setHgap(10);
            studentLayout.setVgap(10);
            Label studentIdLabel = new Label("Enter ID:");
            studentIdLabel.setFont(Font.font("Calibri", 15));
            TextField studentIdField = new TextField();
            Button loginButton1 = new Button();
            loginButton1.setText("Login");
            loginButton1.setFont(Font.font("Calibri", 15));
            studentLayout.add(studentIdLabel,1,2);
            studentLayout.add(studentIdField,2,2);
            studentLayout.add(loginButton1,1,3);
            newWindow.show();

            GridPane registerLayout = new GridPane();
            stage.setTitle("Student Details");
            registerLayout.setHgap(10);
            registerLayout.setVgap(10);
            Button registerButton = new Button();
            registerButton.setText("Register");
            TextField registerField = new TextField();
            registerLayout.add(registerField,1,5);
            registerLayout.add(registerButton,2,5);

            Scene studentScene = new Scene(registerLayout,400,200);
            loginButton1.setOnAction(e1 -> {
                try {
                    student = controller.getStudentRepository().findOne((long) Integer.parseInt(studentIdField.getText()));
                } catch (NullException | SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error!");
                    alert.setHeaderText("");
                    alert.setContentText(ex.toString());
                    alert.showAndWait();
                }
                if (student == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("");
                    alert.setContentText("You don't exist in the database!");
                    alert.showAndWait();
                } else {
                    Label studentInfo1 = new Label("Student ID: " + student.getStudentId());
                    Label studentInfo2 = new Label("First Name: " + student.getFirstName());
                    Label studentInfo3 = new Label("Last Name: " + student.getLastName());
                    Label studentCredits = new Label("Credits: " + student.getTotalCredits());
                    registerLayout.add(studentInfo1, 1, 1);
                    registerLayout.add(studentInfo2, 1, 2);
                    registerLayout.add(studentInfo3, 1, 3);
                    registerLayout.add(studentCredits, 3, 1);
                    registerButton.setOnAction(e2 -> {
                        try {
                            controller.register(Integer.parseInt(registerField.getText()), student.getStudentId());

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Message");
                            alert.setHeaderText("");
                            alert.setContentText("You have been registered!");
                            alert.showAndWait();

                        } catch (WrongInputException | NullException | SQLException | IOException ex) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error!");
                            alert.setHeaderText("");
                            alert.setContentText(ex.toString());
                            alert.showAndWait();
                        }
                        try {
                            student = controller.getStudentRepository().findOne(student.getStudentId());
                        } catch (SQLException | NullException ex) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Error!");
                            alert.setHeaderText("");
                            alert.setContentText(ex.toString());
                            alert.showAndWait();
                        }
                        studentCredits.setText("Credits: " + student.getTotalCredits());
                        stage.setScene(studentScene);
                    });
                } stage.setScene(studentScene);
            });
        });

        // Teacher login window
        teacherButtton.setOnAction(event -> {
            GridPane teacherLayout = new GridPane();
            Scene loginScene1 = new Scene(teacherLayout, 400, 200);
            Stage newWindow = new Stage();
            newWindow.setTitle("Teacher Login");
            newWindow.setScene(loginScene1);
            teacherLayout.setHgap(10);
            teacherLayout.setVgap(10);
            Label teacherIdLabel = new Label("Enter ID:");
            teacherIdLabel.setFont(Font.font("Calibri", 15));
            TextField teacherIdField = new TextField();
            Button loginButton2 = new Button();
            loginButton2.setText("Login");
            loginButton2.setFont(Font.font("Calibri", 15));
            teacherLayout.add(teacherIdLabel, 1, 2);
            teacherLayout.add(teacherIdField, 2, 2);
            teacherLayout.add(loginButton2, 1, 3);
            newWindow.show();

            GridPane layout = new GridPane();
            stage.setTitle("Teacher Details");
            layout.setHgap(10);
            layout.setVgap(10);
            Button refreshButton = new Button();
            refreshButton.setText("Refresh");
            TextField courseIdField = new TextField();
            layout.add(refreshButton,2,4);
            layout.add(courseIdField,1,4);

            ListView<Student> listViewStudents = new ListView();
            listViewStudents.setPrefWidth(300);
            listViewStudents.setPrefHeight(250);
            layout.add(listViewStudents,1,5);

            Scene teacherScene = new Scene(layout, 400, 400);
            loginButton2.setOnAction(e1 -> {
                try {
                    teacher = controller.getTeacherRepository().findOne((long) Integer.parseInt(teacherIdField.getText()));
                } catch (NullException | SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error!");
                    alert.setHeaderText("");
                    alert.setContentText(ex.toString());
                    alert.showAndWait();
                }
                if (teacher == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("");
                    alert.setContentText("You don't exist in the database!");
                    alert.showAndWait();
                } else {
                    Label teacherInfo1 = new Label("Teacher ID: " + teacher.getTeacherId());
                    Label teacherInfo2 = new Label("First Name: " + teacher.getFirstName());
                    Label teacherInfo3 = new Label("Last Name: " + teacher.getLastName());
                    layout.add(teacherInfo1, 1, 1);
                    layout.add(teacherInfo2, 1, 2);
                    layout.add(teacherInfo3, 1, 3);
                    stage.setScene(teacherScene);
                }
            });
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
