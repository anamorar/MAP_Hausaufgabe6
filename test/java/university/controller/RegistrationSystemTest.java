package university.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class RegistrationSystemTest {

    RegistrationSystem controller;
    Teacher teacher1 = new Teacher(101, "Ion", "Pop");
    Teacher teacher2 = new Teacher(102, "Mirela", "Popa");
    Teacher teacher3 = new Teacher(103, "Diana", "Cristea");

    Student student1= new Student(201,"Ana", "Morar", 30);
    Student student2 = new Student(202,"Bogdan", "Popoviciu", 15);
    Student student3 = new Student(203,"Ilie", "Macelaru", 20);
    Student student4 = new Student(204,"Raluca", "Ionut", 28);

    Course course1 = new Course(301, "Math", teacher1, 70, 5);
    Course course2 = new Course(302, "Management", teacher2, 50, 6);
    Course course3 = new Course(303, "Database", teacher3, 70, 6);
    Course course4 = new Course(304, "Data Structure", teacher3, 70, 5);
    Course course5 = new Course(305, "Economics", teacher2, 1, 6);

    @BeforeEach
    void setUp() throws SQLException {
        String connectionUrl = "jdbc:mysql://localhost:3306/universitysql";
        String user = "root";
        String password = "1234";
        Connection connection = DriverManager.getConnection(connectionUrl, user, password);
        TeacherJdbcRepository teacherRepository = new TeacherJdbcRepository(connectionUrl, user, password, connection);
        StudentJdbcRepository studentRepository = new StudentJdbcRepository(connectionUrl, user, password, connection);
        CourseJdbcRepository courseRepository = new CourseJdbcRepository(connectionUrl, user, password, connection);

        controller = new RegistrationSystem(teacherRepository, studentRepository, courseRepository);
        try {
            Statement statement1 = connection.createStatement();
            statement1.execute("DELETE FROM Enrolled");
            Statement statement2 = connection.createStatement();
            statement2.execute("DELETE FROM Students");
            Statement statement3 = connection.createStatement();
            statement3.execute("DELETE FROM Courses");
            Statement statement4 = connection.createStatement();
            statement4.execute("DELETE FROM Teachers");
        } catch (SQLException e) {
            Assertions.fail();
        }

        // Adding the entities
        try {
            controller.getTeacherRepository().save(teacher1);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }
        try {
            controller.getTeacherRepository().save(teacher2);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }
        try {
            controller.getTeacherRepository().save(teacher3);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }

        try {
            controller.getStudentRepository().save(student1);
        } catch(SQLException | IOException e){
            Assertions.fail();
        }
        try {
            controller.getStudentRepository().save(student2);
        } catch(SQLException | IOException e){
            Assertions.fail();
        }
        try {
            controller.getStudentRepository().save(student3);
        } catch(SQLException | IOException e){
            Assertions.fail();
        }
        try {
            controller.getStudentRepository().save(student4);
        } catch(SQLException | IOException e){
            Assertions.fail();
        }

        try {
            controller.getCourseRepository().save(course1);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }
        try {
            controller.getCourseRepository().save(course2);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }
        try {
            controller.getCourseRepository().save(course3);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }
        try {
            controller.getCourseRepository().save(course4);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }
        try {
            controller.getCourseRepository().save(course5);
        } catch(SQLException | IOException | NullException e){
            Assertions.fail();
        }
    }

    @Test
    void register() throws SQLException {
        try {
            controller.register(305, 202);
        } catch (WrongInputException | NullException e) {
            Assertions.fail();
        } catch (IOException e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    void retrieveCoursesWithFreePlaces() throws IOException, NullException, WrongInputException {
        // 4 courses with free places
        try {
            controller.register(305, 202);
            Assertions.assertEquals(5, controller.retrieveCoursesWithFreePlaces().size());
        }catch(SQLException e){
            Assertions.fail();
        }
    }

    @Test
    void retrieveStudentsEnrolledForACourse() {
        try {
            Assertions.assertTrue(controller.retrieveStudentsEnrolledForACourse(301).isEmpty());       }catch(SQLException | WrongInputException e){
            Assertions.fail();
        }
    }

    @Test
    void sortStudentsByTotalCredits() {
        List<Student> listOfStudents = new ArrayList<>();
        try {
            listOfStudents = controller.sortStudentsByTotalCredits();
        }catch (SQLException e){
            Assertions.fail();
        }
        Assertions.assertEquals(201, listOfStudents.get(0).getStudentId());
        Assertions.assertEquals(204, listOfStudents.get(1).getStudentId());
        Assertions.assertEquals(203, listOfStudents.get(2).getStudentId());
        Assertions.assertEquals(202, listOfStudents.get(3).getStudentId());
    }

    @Test
    void filterStudentsAttendingCourse() {
        List<Student> listOfStudents = new ArrayList<>();
        try {
            listOfStudents = controller.filterStudentsAttendingCourse(course5.getCourseId());
        }catch (SQLException e){
            Assertions.fail();
        }
        Assertions.assertEquals(0, listOfStudents.size());
    }

    @Test
    void sortCoursesByName() {
        List<Course> listOfCourses = new ArrayList<>();
        try {
            listOfCourses = controller.sortCoursesByName();
        }catch (SQLException e){
            Assertions.fail();
        }

        Assertions.assertEquals(304, listOfCourses.get(0).getCourseId());
        Assertions.assertEquals(303, listOfCourses.get(1).getCourseId());
        Assertions.assertEquals(305, listOfCourses.get(2).getCourseId());
        Assertions.assertEquals(302, listOfCourses.get(3).getCourseId());
        Assertions.assertEquals(301, listOfCourses.get(4).getCourseId());
    }

    @Test
    void filterCoursesWithSpecifiedMaxEnrollment() {
        List<Course> listOfCourses = new ArrayList<>();
        try {
            listOfCourses = controller.filterCoursesWithSpecifiedMaxEnrollment(50);
        }catch (SQLException e){
            Assertions.fail();
        }

        Assertions.assertEquals(2, listOfCourses.size());
        Assertions.assertEquals(302, listOfCourses.get(0).getCourseId());
        Assertions.assertEquals(305, listOfCourses.get(1).getCourseId());
    }
}