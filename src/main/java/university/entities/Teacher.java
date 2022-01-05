package university.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Person {
    private long teacherId;
    private List<Course> courses;

    public Teacher(long teacherId, String firstName, String lastName) {
        super(firstName, lastName);
        this.teacherId = teacherId;
        this.courses = new ArrayList<>();
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Removes a course from the teacher's list
     *
     * @param course {@code course} that is deleted from the list
     */
    public void removeCourseFromTeacherList(Course course){
        this.courses.remove(course);
    }

    /**
     * Converts the course array to an int array, used to avoid recursive printing
     * @return List<Integer>
     */
    public List<Integer> coursesId() {
        List<Integer> courseList = new ArrayList<>();
        for (Course c : this.courses)
            courseList.add((int) c.getCourseId());
        return courseList;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId=" + teacherId +
                ", firstName=" + getFirstName() +
                ", lastName=" + getLastName() +
                ", courses=" + coursesId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return getTeacherId() == teacher.getTeacherId() && Objects.equals(getCourses(), teacher.getCourses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTeacherId(), getCourses());
    }


}
