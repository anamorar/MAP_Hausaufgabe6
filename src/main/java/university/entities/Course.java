package university.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course {
    private long courseId;
    private String name;
    private Teacher teacher;
    private int maxEnrollment;
    private List<Student> studentsEnrolled;
    private int credits;

    public Course(long courseId, String name, Teacher teacher, int maxEnrollment, int credits) {
        this.courseId = courseId;
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled = new ArrayList<>();
        this.credits = credits;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Student> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Student> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Clear the list
     */
    public void clearList(){
        this.studentsEnrolled.clear();
    }

    public List<Integer> studentsEnrolledWithId() {
        List<Integer> studentsWithIdList = new ArrayList<>();
        for (Student s : this.studentsEnrolled)
            studentsWithIdList.add((int) s.getStudentId());
        return studentsWithIdList;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", name='" + name + '\'' +
                ", teacher=" + teacher +
                ", maxEnrollment=" + maxEnrollment +
                ", studentsEnrolled=" + studentsEnrolledWithId() +
                ", credits=" + credits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return getCourseId() == course.getCourseId() && getMaxEnrollment() == course.getMaxEnrollment() && getCredits() == course.getCredits() && Objects.equals(getName(), course.getName()) && Objects.equals(getTeacher(), course.getTeacher()) && Objects.equals(getStudentsEnrolled(), course.getStudentsEnrolled());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCourseId(), getName(), getTeacher(), getMaxEnrollment(), getStudentsEnrolled(), getCredits());
    }
}
