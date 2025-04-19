
import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private List<Course> teachingCourses;

    public Teacher(String name, String id) {
        super(name, id);
        this.teachingCourses = new ArrayList<>();
    }

    public List<Course> getTeachingCourses() {
        return teachingCourses;
    }

    public void addCourse(Course course) {
        if (!teachingCourses.contains(course)) {
            teachingCourses.add(course);
        }
    }

    public void removeCourse(Course course) {
        teachingCourses.remove(course);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
