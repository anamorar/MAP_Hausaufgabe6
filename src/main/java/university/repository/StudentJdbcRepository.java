package university.repository;

import university.entities.Course;
import university.entities.Student;
import university.entities.Teacher;
import university.exceptions.NullException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentJdbcRepository implements ICrudRepository<Student> {
    private String connectionUrl;
    private String user;
    private String password;
    private Connection connection;

    public StudentJdbcRepository(String connectionUrl, String user, String password, Connection connection) throws SQLException {
        this.connectionUrl = connectionUrl;
        this.user = user;
        this.password = password;
        this.connection = DriverManager.getConnection(connectionUrl, user, password);
    }

    public StudentJdbcRepository() {
    }

    @Override
    public Student findOne(Long id) throws SQLException, NullException {
        if (id == null) {
            throw new NullException("Null id!");
        }
        String findOneQuery = "SELECT studentId, firstName, lastName, totalCredits FROM Students WHERE studentId = '" + id + "'";
        String studentsEnrolledQuery = "SELECT C.courseId, C.name, C.credits, C.maxEnrollment, T.teacherId, T.firstName, T.lastName " +
                "FROM Enrolled E INNER JOIN Courses C ON C.courseId = E.courseId " +
                "INNER JOIN Teachers T ON T.teacherId = C.teacherId WHERE E.studentId = '" + id + "'";

        Statement statement = connection.createStatement();
        Statement innerStatement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(findOneQuery);

        Student student = null;
        List<Course> enrolledCourses = new ArrayList<>();
        while (resultSet.next()) {
            student = new Student(resultSet.getLong("studentId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getInt("totalCredits"));
            ResultSet resultSet2 = innerStatement.executeQuery(studentsEnrolledQuery);
            while (resultSet2.next()) {
                Course course = new Course(resultSet.getLong("courseId"),
                        resultSet.getString("name"),
                        new Teacher(resultSet.getLong("teacherId"),
                                resultSet.getString("firstName"),
                                resultSet.getString("lastName")),
                        resultSet.getInt("maxEnrollment"),
                        resultSet.getInt("credits"));
                enrolledCourses.add(course);
            }
            student.setEnrolledCourses(enrolledCourses);
        }
        return student;
    }

    @Override
    public List<Student> findAll() throws SQLException {
        List<Student> repoOfStudents = new ArrayList<>();
        String findAllQuery = "SELECT * FROM Students ";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(findAllQuery);

        while (resultSet.next()) {
            Student student = new Student(resultSet.getLong("studentId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"),
                    resultSet.getInt("totalCredits"));
            repoOfStudents.add(student);
        }
        return repoOfStudents;
    }

    @Override
    public void update(Student entity) throws NullException, IOException, SQLException {
        String updateQuery = "UPDATE Students SET firstName = ?, lastName = ?, totalCredits = ? WHERE studentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setInt(3, entity.getTotalCredits());
        preparedStatement.setLong(4, entity.getStudentId());
        preparedStatement.execute();
    }

    @Override
    public void del(Long id) throws NullException, IOException, SQLException {
        String deleteQuery = "DELETE FROM Students WHERE studentId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
    }

    @Override
    public void save(Student entity) throws IOException, SQLException {
        String insertQuery = "INSERT INTO Students (studentId, firstName, lastName, credits) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setLong(1, entity.getStudentId());
        preparedStatement.setString(2, entity.getFirstName());
        preparedStatement.setString(3, entity.getLastName());
        preparedStatement.setInt(4, entity.getTotalCredits());
        preparedStatement.execute();
    }

    /**
     * Insert course registers to database from student's list
     * @param student
     * @throws SQLException
     */
    public void insertEnrollment(Course course, Student student) throws SQLException {
        String registerQuery = "INSERT IGNORE INTO Enrolled values (?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(registerQuery);
        for(Course courseId : student.getEnrolledCourses()){
            preparedStatement.setLong(1, course.getCourseId());
            preparedStatement.setLong(2, student.getStudentId());
            preparedStatement.execute();
        }
    }
}

