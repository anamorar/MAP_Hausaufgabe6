package university.ui;

import university.controller.RegistrationSystem;
import university.entities.Course;
import university.entities.Student;
import university.entities.Teacher;
import university.exceptions.NullException;
import university.exceptions.WrongInputException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


public class ConsoleView {
    private final RegistrationSystem controller;

    public ConsoleView(RegistrationSystem controller) {
        this.controller = controller;
    }

    /**
     * Displays the start menu with options
     */
    public void displayStartMenu() {
        System.out.println("""
                 ==================================================================
                 ||  1. Teachers Menu                                            ||
                 ||  2. Courses Menu                                             ||
                 ||  3. Students Menu                                            ||
                 ||  0. Exit application                                         ||
                 ==================================================================""");
    }

    /**
     * Displays the menu for teachers with options
     */
    public void displayTeacherMenu() {
        System.out.println("""
                 ==================================================================
                 ||  TEACHER menu: (type number for action)                      ||
                 ||  1. All teachers                                             ||
                 ||  2. Add teacher                                              ||
                 ||  3. Update teacher                                           ||
                 ||  4. Delete teacher                                           ||
                 ==================================================================
                 ||  0. Back to menu                                             ||
                 ==================================================================""");
    }

    /**
     * Displays the menu for students with options
     */
    public void displayStudentMenu() {
        System.out.println("""
                 ==================================================================
                 ||  STUDENT menu: (type number for action)                      ||
                 ||  1. All students                                             ||
                 ||  2. Add student                                              ||
                 ||  3. Update student                                           ||
                 ||  4. Delete student                                           ||
                 ||  5. Register                                                 ||
                 ||  6. Show students enrolled for a course                      ||
                 ||  7. Sort students by total credits                           ||
                 ||  8. Filter the students who attend the specified course      ||
                 ==================================================================
                 ||  0. Back to menu                                             ||
                 ==================================================================""");
    }

    /**
     * Displays the menu for courses with options
     */
    public void displayCourseMenu() {
        System.out.println("""
                 ==================================================================
                 ||  COURSE menu: (type number for action)                       ||
                 ||  1. All courses                                              ||
                 ||  2. Add course                                               ||
                 ||  3. Update course                                            ||
                 ||  4. Delete course                                            ||
                 ||  5. Show courses with free places                            ||
                 ||  6. Sort courses by name                                     ||
                 ||  7. Filter courses with the specified nr of enrollments      ||
                 ==================================================================
                 ||  0. Back to menu                                             || 
                 ================================================================== """);
    }

    public void endProgram() {
        System.out.println("""
                 =====================================================
                 ||  Program shutting down...                       ||
                 ||  Press ENTER to continue                        ||
                 =====================================================""");
    }

    /**
     * Checks if it was entered a valid option
     */
    public void errorWrongInput() {
        System.out.println("""
                 =====================================================
                 ||  You have entered the wrong value.              ||
                 ||  Press ENTER to continue                        ||
                 =====================================================""");
    }

    public void startProgram() throws IOException, NullException, WrongInputException, SQLException {
        Scanner input = new Scanner(System.in);
        int decision, loop;
        do {
            displayStartMenu();
            System.out.println("Decision: ");
            decision = input.nextInt();
            loop = 1;
            switch (decision) {
                case 0 -> {
                    endProgram();
                    loop = 0;
                    break;
                }
                case 1 -> displayTeacher();
                case 2 -> displayCourses();
                case 3 -> displayStudent();
                default -> errorWrongInput();
            }
        } while (loop != 0);
    }

    public void displayTeacher() throws IOException, NullException, SQLException, WrongInputException {
        Scanner input = new Scanner(System.in);
        int decision, loop;
        do {
            displayTeacherMenu();
            System.out.println("Decision: ");
            decision = input.nextInt();
            loop = 1;
            switch (decision) {
                case 1 -> controller.getAllTeachers().forEach(System.out::println);
                case 2 -> saveTeacherUI();

                case 3 -> updateTeacherUI();
                case 4 -> deleteTeacherUI();
                case 0 -> {
                    startProgram();
                    loop = 0;
                }
                default -> errorWrongInput();
            }
        } while (loop != 0);
    }

    public void displayStudent() throws IOException, NullException, WrongInputException, SQLException {
        Scanner input = new Scanner(System.in);
        long courseId;
        int decision, loop;
        do {
            displayStudentMenu();
            System.out.println("Decision: ");
            decision = input.nextInt();
            loop = 1;
            switch (decision) {
                case 1-> controller.getAllStudents().forEach(System.out::println);
                case 2 -> saveStudentUI();
                case 3 -> updateStudentUI();
                case 4 -> deleteStudentUI();
                case 5 -> registerUI();
                case 6 -> {
                    System.out.println("Enter Course Id: ");
                    courseId = input.nextLong();
                    controller.retrieveStudentsEnrolledForACourse(courseId).forEach(System.out::println);
                }
                case 7 -> controller.sortStudentsByTotalCredits().forEach(System.out::println);
                case 8 -> {
                    System.out.println("Enter Specified Course Id: ");
                    courseId = input.nextLong();
                    controller.filterStudentsAttendingCourse(courseId).forEach(System.out::println);
                }
                case 0 -> {
                    startProgram();
                    loop = 0;
                }
                default -> errorWrongInput();
            }
        } while (loop != 0);
    }

    public void displayCourses() throws IOException, NullException, SQLException, WrongInputException {
        Scanner input = new Scanner(System.in);
        int decision, maxEnrollment, loop;
        do {
            displayCourseMenu();
            System.out.println("Decision: ");
            decision = input.nextInt();
            loop = 1;
            switch (decision) {
                case 1 -> controller.getAllCourses().forEach(System.out::println);
                case 2 -> saveCourseUI();
                case 3 -> updateCoursesUI();
                case 4 -> deleteCoursesUI();
                case 5 -> controller.retrieveCoursesWithFreePlaces().forEach(System.out::println);
                case 6 -> controller.sortCoursesByName().forEach(System.out::println);
                case 7 -> {
                    System.out.println("Enter number of maximum enrollment: ");
                    maxEnrollment = input.nextInt();
                    controller.filterCoursesWithSpecifiedMaxEnrollment(maxEnrollment).forEach(System.out::println);
                }
                case 0 -> {
                    startProgram();
                    loop = 0;
                }
                default -> errorWrongInput();
            }
        } while (loop != 0);
    }

    public void registerUI() throws WrongInputException, NullException, SQLException, IOException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Course's ID: ");
        long courseId = input.nextLong();
        System.out.println("Enter Student's ID: ");
        long studentId = input.nextLong();
        controller.register(courseId, studentId);
    }

    private void saveTeacherUI() throws NullException {
        Scanner scanner = new Scanner(System.in);
        String firstName, lastName;
        long teacherId;

        System.out.println("Enter Teacher's ID: ");
        teacherId = scanner.nextLong();
        System.out.println("Enter Teacher's First Name: ");
        firstName = scanner.next();
        System.out.println("Enter Teacher's Last Name: ");
        lastName = scanner.next();

        Teacher teacher = new Teacher(teacherId, firstName, lastName);
        try {
            controller.getTeacherRepository().save(teacher);
        } catch (IOException | SQLException e) {
            System.out.println("Can't add the teacher");
        }
    }

    public void updateTeacherUI() {
        Scanner scanner = new Scanner(System.in);
        String newFirstName, newLastName;
        long teacherId;

        System.out.println("Enter Teacher's ID: ");
        teacherId = scanner.nextLong();
        System.out.println("Enter Teacher's First Name: ");
        newFirstName = scanner.next();
        System.out.println("Enter Teacher's Last Name: ");
        newLastName = scanner.next();

        Teacher teacher = new Teacher(teacherId, newFirstName, newLastName);
        try {
            controller.getTeacherRepository().update(teacher);
        } catch (IOException | NullException | SQLException e) {
            System.out.println("Can't update the teacher");
        }
    }

    public void deleteTeacherUI() {
        Scanner scanner = new Scanner(System.in);
        long teacherId;

        System.out.println("Enter Teacher's ID: ");
        teacherId = scanner.nextLong();

        try {
            controller.getTeacherRepository().del(teacherId);
        } catch (IOException | NullException | SQLException e) {
            System.out.println("Can't delete the teacher");
        }

    }

    private void saveStudentUI() {
        Scanner scanner = new Scanner(System.in);
        String firstName, lastName;
        long studentId;
        int credits;

        System.out.println("Enter Student's ID: ");
        studentId = scanner.nextLong();
        System.out.println("Enter Student's First Name: ");
        firstName = scanner.next();
        System.out.println("Enter Student's Last Name: ");
        lastName = scanner.next();
        System.out.println("Enter Student's Credits: ");
        credits = scanner.nextInt();

        Student student = new Student(studentId, firstName, lastName, credits);
        try {
            controller.getStudentRepository().save(student);
        } catch (IOException | SQLException e) {
            System.out.println("Can't add the student");
        }
    }

    public void updateStudentUI() {
        Scanner scanner = new Scanner(System.in);
        String newFirstName, newLastName;
        long studentId;
        int newCredits;

        System.out.println("Enter Student's ID: ");
        studentId = scanner.nextLong();
        System.out.println("Enter Student's New First Name: ");
        newFirstName = scanner.next();
        System.out.println("Enter Student's New Last Name: ");
        newLastName = scanner.next();
        System.out.println("Enter Student's New Credits: ");
        newCredits = scanner.nextInt();

        Student student = new Student(studentId, newFirstName, newLastName, newCredits);
        try {
            controller.getStudentRepository().update(student);
        } catch (IOException | NullException | SQLException e) {
            System.out.println("Can't update the student");
        }
    }

    public void deleteStudentUI() {
        Scanner scanner = new Scanner(System.in);
        long studentId;

        System.out.println("Enter Student's ID: ");
        studentId = scanner.nextLong();

        try {
            controller.getStudentRepository().del(studentId);
        } catch (IOException | NullException | SQLException e) {
            System.out.println("Can't delete the student");
        }
    }

    private void saveCourseUI() throws NullException, SQLException {
        Scanner scanner = new Scanner(System.in);
        String name;
        long courseId, teacherId;
        int maxEnrollment, credits;

        System.out.println("Enter Course's ID: ");
        courseId = scanner.nextLong();
        System.out.println("Enter Course's Name: ");
        name = scanner.next();
        System.out.println("Enter Teacher's ID: ");
        teacherId = scanner.nextLong();
        System.out.println("Enter Max Enrollment: ");
        maxEnrollment = scanner.nextInt();
        System.out.println("Enter Course's Credits: ");
        credits = scanner.nextInt();

        Teacher teacher = new Teacher(-1,"","");

        for(Teacher t : controller.getTeacherRepository().findAll()){
            if(t.getTeacherId() == teacherId){
                teacher.setFirstName(t.getFirstName());
                teacher.setLastName(t.getLastName());
                teacher.setTeacherId(t.getTeacherId());
            }
        }

        Course course = new Course(courseId, name, teacher, maxEnrollment, credits);
        try {
            controller.getCourseRepository().save(course);
        } catch (IOException e) {
            System.out.println("Can't add the course");
        }
    }

    public void updateCoursesUI() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String newName;
        long courseId, newTeacherId;
        int newMaxEnrollment, newCredits;

        System.out.println("Enter Course's ID: ");
        courseId = scanner.nextLong();
        System.out.println("Enter Course's Name: ");
        newName = scanner.next();
        System.out.println("Enter Teacher's ID: ");
        newTeacherId = scanner.nextLong();
        System.out.println("Enter Max Enrollment: ");
        newMaxEnrollment = scanner.nextInt();
        System.out.println("Enter Course's Credits: ");
        newCredits = scanner.nextInt();

        Teacher newTeacher = new Teacher(-1,"","");

        for(Teacher t : controller.getTeacherRepository().findAll()){
            if(t.getTeacherId() == newTeacherId){
                newTeacher.setFirstName(t.getFirstName());
                newTeacher.setLastName(t.getLastName());
                newTeacher.setTeacherId(t.getTeacherId());
            }
        }

        Course course = new Course(courseId, newName, newTeacher, newMaxEnrollment, newCredits);
        try {
            controller.getCourseRepository().update(course);
        } catch (IOException | NullException | SQLException e) {
            System.out.println("Can't update the course");
        }
    }

    public void deleteCoursesUI() {
        Scanner scanner = new Scanner(System.in);
        long courseId;

        System.out.println("Enter Course's ID: ");
        courseId = scanner.nextLong();

        try {
            controller.getCourseRepository().del(courseId);
        } catch (IOException | NullException | SQLException e) {
            System.out.println("Can't delete the course");
        }
    }
}
