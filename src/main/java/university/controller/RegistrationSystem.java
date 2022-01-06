package university.controller;

import university.entities.Course;
import university.entities.Student;
import university.entities.Teacher;
import university.exceptions.NullException;
import university.exceptions.WrongInputException;
import university.repository.CourseJdbcRepository;
import university.repository.StudentJdbcRepository;
import university.repository.TeacherJdbcRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RegistrationSystem {

    private final TeacherJdbcRepository teacherRepository;
    private final StudentJdbcRepository studentRepository;
    private final CourseJdbcRepository courseRepository;

    public RegistrationSystem(TeacherJdbcRepository teacherRepository, StudentJdbcRepository studentRepository, CourseJdbcRepository courseRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public StudentJdbcRepository getStudentRepository() {
        return studentRepository;
    }

    public TeacherJdbcRepository getTeacherRepository() {
        return teacherRepository;
    }

    public CourseJdbcRepository getCourseRepository() {
        return courseRepository;
    }

    /**
     * Sorts students descending by the number of total credits
     *
     * @return sorted list of students
     */
    public List<Student> sortStudentsByTotalCredits() throws SQLException {
        return studentRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Student::getTotalCredits).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Sorts courses descending by the number of enrolled students
     *
     * @return sorted list of students
     */
    public List<Course> sortCoursesByName() throws SQLException {
        return courseRepository.findAll().stream()
                .sorted(Comparator.comparing(Course::getName))
                .collect(Collectors.toList());
    }

    /**
     * Filters the students who attend the course with the given id
     *
     * @param courseId {@code courseId} id of the course
     * @return list of students who attend the course
     */
    public List<Student> filterStudentsAttendingCourse(Long courseId) throws SQLException {
        return studentRepository.findAll().stream()
                .filter(student -> student.getEnrolledCourses().contains(courseId))
                .collect(Collectors.toList());
    }

    /**
     * Filters the courses with the specified maximum number of enrollments
     *
     * @param maxEnrollment {@code maxEnrollment} number of credits
     * @return list of courses
     */
    public List<Course> filterCoursesWithSpecifiedMaxEnrollment(int maxEnrollment) throws SQLException {
        return courseRepository.findAll().stream()
                .filter(course -> course.getMaxEnrollment() <= maxEnrollment)
                .collect(Collectors.toList());
    }

    /**
     * @return all the elements from course repository
     */
    public List<Course> getAllCourses() throws SQLException {
        return courseRepository.findAll();
    }

    /**
     * @return all the elements from teacher repository
     */
    public List<Teacher> getAllTeachers() throws SQLException {
        return teacherRepository.findAll();
    }

    /**
     * @return all the elements from student repository
     */
    public List<Student> getAllStudents() throws SQLException {
        return studentRepository.findAll();
    }

    /**
     * Enroll a student in a course
     * @param courseId {@code courseId} to which the registration is made
     * @param studentId {@code studentId} for which the registration is made
     * @return
     */
    public boolean register(long courseId, long studentId) throws WrongInputException, NullException, SQLException, IOException {
        Course course = courseRepository.findOne(courseId);
        Student student = studentRepository.findOne(studentId);

        if(course == null) {
            throw new WrongInputException("Non-existing course!");
        }

        if(student == null) {
            throw new WrongInputException("Non-existing student!");
        }

        List<Student> studentsEnrolled = course.getStudentsEnrolled();
        if(studentsEnrolled.size() == course.getMaxEnrollment()) {
            throw new WrongInputException("FULL class! Enroll in another course!");
        }

        if(studentsEnrolled.contains(student)) {
            throw new WrongInputException("Student already enrolled!");
        }

        int creditsAfterRegister = student.getTotalCredits() + course.getCredits();
        if(creditsAfterRegister > 30) {
            throw new WrongInputException("Total number of credits exceeded!");
        }
        student.setTotalCredits(creditsAfterRegister);

        studentsEnrolled.add(student);
        course.setStudentsEnrolled(studentsEnrolled);
        try {
        courseRepository.update(course);
        } catch(NullException | IOException e) {
            System.out.println(e.getMessage());
        }

        List<Course> enrolledCourses = student.getEnrolledCourses();
        enrolledCourses.add(course);
        student.setEnrolledCourses(enrolledCourses);
        try {
        studentRepository.update(student);
        } catch(NullException | IOException e) {
            System.out.println(e.getMessage());
        }

        studentRepository.insertEnrollment(course, student);

        System.out.println("Successfully registered!");
        return true;
    }

    /**
     * Retrieve the courses that have free places
     *
     * @return a list with all the courses
     */
    public List<Course> retrieveCoursesWithFreePlaces() throws SQLException {
        return courseRepository.findAll()
                .stream().filter(c -> c.getStudentsEnrolled().size() < c.getMaxEnrollment())
                .collect(Collectors.toList());
    }

    /**
     * Retrieve the students enrolled in a course
     *
     * @param courseId {@code courseId} in which the students are enrolled
     * @return a list with all the enrolled students
     */
    public List<Student> retrieveStudentsEnrolledForACourse(long courseId) throws WrongInputException, SQLException {
        Course course = null;
        for(Course c: courseRepository.findAll())
            if(c.getCourseId() == courseId)
                course = c;
        if (course == null) {
            throw new WrongInputException("Invalid Course ID");
        }
        return course.getStudentsEnrolled();
    }

    /**
     * Teacher removes a course
     *
     * @param teacher {@code teacher} that removes the course
     * @param course  {@code course}
     * @return the boolean
     */
    public boolean teacherRemovesCourse(Teacher teacher, Course course) throws WrongInputException, NullException, SQLException {
        if(teacherRepository.findOne(teacher.getTeacherId()) == null) {
            throw new WrongInputException("Teacher doesn't exist");
        }

        if(!teacher.getCourses().contains(course)) {
            throw new WrongInputException("Course doesn't exist in the teacher's list");
        }

        for(Student student : course.getStudentsEnrolled()) {
            student.removeCourseFromStudentList(course);
        }

        course.clearList();
        course.setTeacher(null);
        teacher.removeCourseFromTeacherList(course);
        return true;
    }


}
