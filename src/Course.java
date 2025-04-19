
import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private Teacher instructor;
    private List<Student> enrolledStudents;
    private boolean gradesPublished;

    public Course(String name, Teacher instructor) {
        this.name = name;
        this.instructor = instructor;
        this.enrolledStudents = new ArrayList<>();
        this.gradesPublished = false;
    }

    public String getName() {
        return name;
    }

    public Teacher getInstructor() {
        return instructor;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public boolean areGradesPublished() {
        return gradesPublished;
    }

    public void setGradesPublished(boolean published) {
        this.gradesPublished = published;
    }

    public void addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }

    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }

    @Override
    public String toString() {
        return name + " (Instructor: " + instructor.getName() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return name.equals(course.name) && instructor.equals(course.instructor);
    }
}
