package university.repository;

import university.entities.Course;
import university.entities.Student;
import university.entities.Teacher;
import university.exceptions.NullException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseJdbcRepository implements ICrudRepository<Course>{
    private String connectionUrl;
    private String user;
    private String password;
    private Connection connection;

    public CourseJdbcRepository(String connectionUrl, String user, String password, Connection connection) throws SQLException {
        this.connectionUrl = connectionUrl;
        this.user = user;
        this.password = password;
        this.connection = DriverManager.getConnection(connectionUrl, user, password);
    }

    public CourseJdbcRepository() throws SQLException{
    }

    @Override
    public Course findOne(Long id) throws NullException, SQLException {
        String findOneQuery = "SELECT C.courseId, C.name, T.teacherId, C.maxEnrollment, C.credits, T.firstName, T.lastName" +
                " FROM Courses C " + "INNER JOIN Teachers T ON C.teacherId = T.teacherId WHERE C.courseId = '" + id + "'";
        String studentsEnrolledQuery = "SELECT S.studentId, S.firstName, S.lastName, S.totalCredits FROM Enrolled E " +
                "INNER JOIN Students S ON E.studentId = S.studentId WHERE E.courseId = '" + id + "'";
        Statement statement = connection.createStatement();
        Statement innerStatement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(findOneQuery);

        Course course = null;
        List<Student> studentsEnrolled = new ArrayList<>();
        while (resultSet.next()) {
            course = new Course(resultSet.getLong("courseId"),
                    resultSet.getString("name"),
                    new Teacher(resultSet.getLong("teacherId"),
                            resultSet.getString("firstName"),
                            resultSet.getString("lastName")),
                    resultSet.getInt("maxEnrollment"),
                    resultSet.getInt("credits"));
            ResultSet resultSet2 = innerStatement.executeQuery(studentsEnrolledQuery);
            while (resultSet2.next()) {
                Student student = new Student(resultSet2.getLong("studentId"),
                        resultSet2.getString("firstName"),
                        resultSet2.getString("lastName"),
                        resultSet2.getInt("totalCredits"));
                studentsEnrolled.add(student);
            }
            course.setStudentsEnrolled(studentsEnrolled);
        }
        return course;
    }

    @Override
    public List<Course> findAll() throws SQLException {
        List<Course> repoOfCourses = new ArrayList<>();
        String findAllQuery = "SELECT * FROM Courses C LEFT OUTER JOIN Teachers T ON C.teacherId = T.teacherId";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(findAllQuery);

        while (resultSet.next()){
            Course course = new Course(resultSet.getLong("courseId"),
                                resultSet.getString("name"),
                                new Teacher(resultSet.getLong("teacherId"), resultSet.getString("firstName"), resultSet.getString("lastName")),
                                resultSet.getInt("maxEnrollment"),
                                resultSet.getInt("credits"));
            repoOfCourses.add(course);
        }
        return repoOfCourses;
    }

    @Override
    public void update(Course entity) throws NullException, IOException, SQLException {
        String updateQuery = "UPDATE Courses SET name = ?, teacherId = ?, maxEnrollment = ?, credits = ? WHERE courseId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setLong(2, entity.getTeacher().getTeacherId());
        preparedStatement.setInt(3, entity.getMaxEnrollment());
        preparedStatement.setInt(4, entity.getCredits());
        preparedStatement.setLong(5, entity.getCourseId());
        preparedStatement.execute();
    }

    @Override
    public void del(Long id) throws NullException, IOException, SQLException {
        String deleteQuery = "DELETE FROM Courses WHERE courseId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
    }

    @Override
    public void save(Course entity) throws NullException, IOException, SQLException {
        String insertQuery = "INSERT INTO Courses (courseId, name, teacherId, maxEnrollment, credits) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setLong(1, entity.getCourseId());
        preparedStatement.setString(2, entity.getName());
        preparedStatement.setLong(3, entity.getTeacher().getTeacherId());
        preparedStatement.setInt(4, entity.getMaxEnrollment());
        preparedStatement.setInt(5, entity.getCredits());
        preparedStatement.execute();
    }
}
