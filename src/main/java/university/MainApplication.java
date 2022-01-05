package university;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import university.controller.RegistrationSystem;
import university.entities.Course;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MainApplication extends javafx.application.Application {
    RegistrationSystem controller;
    Student userStudent;
    Teacher userTeacher;

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
        Scene loginScene = new Scene(loginLayout,400,400);
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
        studentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane studentLayout = new GridPane();
                Scene loginScene = new Scene(studentLayout,400,400);
                Stage newWindow = new Stage();
                newWindow.setTitle("Student Login");
                newWindow.setScene(loginScene);
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

                Scene studentScene = new Scene(registerLayout,400,400);
                loginButton1.setOnAction(e1 -> {
                    try {
                        userStudent = controller.getStudentRepository().findOne((long) Integer.parseInt(studentIdField.getText()));
                    } catch (Exception ex) {
                        userStudent = null;
                    }
                    if(userStudent != null){
                        Label studentInfo1 = new Label("Student ID: " + userStudent.getStudentId());
                        Label studentInfo2 = new Label("First Name: " + userStudent.getFirstName());
                        Label studentInfo3 = new Label("Last Name: " + userStudent.getLastName());
                        Label studentCredits = new Label("Credits: "+ userStudent.getTotalCredits());
                        registerLayout.add(studentInfo1,1,1);
                        registerLayout.add(studentInfo2,1,2);
                        registerLayout.add(studentInfo3,1,3);
                        registerLayout.add(studentCredits,3,1);
                        registerButton.setOnAction(e2 -> {
                            try {
                                controller.register(Integer.parseInt(registerField.getText()), userStudent.getStudentId());
                            } catch (WrongInputException | NullException | SQLException | IOException ex) {
                                ex.printStackTrace();
                            }
                            try {
                                userStudent = controller.getStudentRepository().findOne(userStudent.getStudentId());
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            studentCredits.setText("Credits: " + userStudent.getTotalCredits());
                        });
                        stage.setScene(studentScene);}
                });
            }
        });

        // Teacher login window
        teacherButtton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane teacherLayout = new GridPane();
                Scene loginScene = new Scene(teacherLayout, 310, 290);
                Stage newWindow = new Stage();
                newWindow.setTitle("Teacher Login");
                newWindow.setScene(loginScene);
                AtomicReference<ArrayList<Pair<Integer, Integer>>> enrolledStudents= new AtomicReference<>(new ArrayList<>());
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
                listViewStudents.setPrefHeight(300);
                layout.add(listViewStudents,1,5);

                Scene teacherScene = new Scene(layout, 400, 400);
                loginButton2.setOnAction(e1 -> {
                    try {
                        userTeacher = controller.getTeacherRepository().findOne((long) Integer.parseInt(teacherIdField.getText()));
                    } catch (Exception ex) {
                        userTeacher = null;
                    }
                    if (userTeacher != null) {
                        Label teacherInfo1 = new Label("Teacher ID: " + userTeacher.getTeacherId());
                        Label teacherInfo2 = new Label("First Name: " + userTeacher.getFirstName());
                        Label teacherInfo3 = new Label("Last Name: " + userTeacher.getLastName());
                        layout.add(teacherInfo1, 1, 1);
                        layout.add(teacherInfo2, 1, 2);
                        layout.add(teacherInfo3, 1, 3);
                        stage.setScene(teacherScene);
//                        refreshButton.setOnAction( e2 -> {
//                            listViewStudents.getItems().clear();
//                            if(!courseIdField.getText().equals("")) {
//                                try {
//                                    if(studentsEnrolled(Integer.parseInt(courseIdField.getText()), userTeacher.getTeacherId()) != null)
//                                        for(Student s: studentsEnrolled(Integer.parseInt(courseIdField.getText()), userTeacher.getTeacherId())) {
//                                            try {
//                                                listViewStudents.getItems().add(controller.getStudentRepository().findOne(s.getStudentId()));
//                                            } catch (SQLException ex) {
//                                                listViewStudents.getItems().clear();
//                                            }
//                                        }
//                                } catch (SQLException | NullException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                    }
                });
            }
        });
        stage.show();
    }

//    public List<Student> studentsEnrolled(long courseId, long teacherId) throws SQLException, NullException {
//
//        Course course = null;
//        Teacher teacher = controller.getTeacherRepository().findOne(teacherId);
//        try{
//            course = controller.getCourseRepository().findOne(courseId);
//        } catch (SQLException | NullException throwables) {
//            return null;
//        }
//        if(course == null)
//            return null;
//        if(course.getTeacher().getTeacherId() != teacher.getTeacherId())
//            return null;
//        return course.getStudentsEnrolled();
//    }

    public static void main(String[] args) {
        launch();
    }
}
