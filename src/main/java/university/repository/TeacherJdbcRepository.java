package university.repository;

import university.entities.Teacher;
import university.exceptions.NullException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeacherJdbcRepository implements  ICrudRepository<Teacher>{
    private String connectionUrl;
    private String user;
    private String password;
    private Connection connection;

    public TeacherJdbcRepository(String connectionUrl, String user, String password, Connection connection) throws SQLException {
        this.connectionUrl = connectionUrl;
        this.user = user;
        this.password = password;
        this.connection = DriverManager.getConnection(connectionUrl, user, password);
    }

    public TeacherJdbcRepository() throws SQLException{
    }

    @Override
    public Teacher findOne(Long id) throws NullException, SQLException {
        if (id == null) {
            throw new NullException("Null id!");
        }
        String findOneQuery = "SELECT teacherId, firstName, lastName FROM Teachers " + "WHERE teacherId = '" + id + "'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(findOneQuery);
        Teacher teacher = null;
        while (resultSet.next()) {
            teacher = new Teacher(resultSet.getLong("teacherId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"));
        }
        return teacher;
    }

    @Override
    public List<Teacher> findAll() throws SQLException {
        List<Teacher> repoOfTeachers = new ArrayList<>();
        String findAllQuery = "SELECT * FROM Teachers ";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(findAllQuery);

        while (resultSet.next()){
            Teacher teacher = new Teacher(resultSet.getLong("teacherId"),
                    resultSet.getString("firstName"),
                    resultSet.getString("lastName"));
            repoOfTeachers.add(teacher);
        }
        return repoOfTeachers;
    }

    @Override
    public void update(Teacher entity) throws NullException, IOException, SQLException {
        String updateQuery = "UPDATE Teachers SET firstName = ?, lastName = ? WHERE teacherId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setLong(3, entity.getTeacherId());
        preparedStatement.execute();
    }

    @Override
    public void del(Long id) throws NullException, IOException, SQLException {
        String deleteQuery = "DELETE FROM Teachers WHERE teacherId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setLong(1, id);
        preparedStatement.execute();
    }

    @Override
    public void save(Teacher entity) throws NullException, IOException, SQLException {
        String insertQuery = "INSERT INTO Teachers (teacherId, firstName, lastName) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setLong(1, entity.getTeacherId());
        preparedStatement.setString(2, entity.getFirstName());
        preparedStatement.setString(3, entity.getLastName());
        preparedStatement.execute();
    }
}
