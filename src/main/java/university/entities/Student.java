package university.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student extends Person {
    private long studentId;
    private int totalCredits;
    private List<Course> enrolledCourses;

    public Student(long studentId, String firstName, String lastName, int totalCredits) {
        super(firstName, lastName);
        this.studentId = studentId;
        this.totalCredits = totalCredits;
        this.enrolledCourses = new ArrayList<>();
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * Removes a course from the student's list
     *
     * @param course {@code course} that is deleted from the list
     */
    public void removeCourseFromStudentList(Course course){
        this.enrolledCourses.remove(course);
    }

    /**
     * Converts the course array to an int array, used to avoid recursive printing
     * @return List<Integer>
     */
    public List<Integer> enrolledCoursesWithId(){
        List<Integer> listWithCourseId = new ArrayList<>();
        for(Course c : enrolledCourses)
            listWithCourseId.add((int)c.getCourseId());
        return listWithCourseId;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", totalCredits=" + totalCredits +
                ", enrolledCourses=" + enrolledCoursesWithId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return getStudentId() == student.getStudentId() && getTotalCredits() == student.getTotalCredits() && Objects.equals(getEnrolledCourses(), student.getEnrolledCourses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStudentId(), getTotalCredits(), getEnrolledCourses());
    }


}
