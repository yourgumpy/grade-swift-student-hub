
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<Course> enrolledCourses;
    private List<GradeEntry> grades;

    public Student(String name, String id) {
        super(name, id);
        this.enrolledCourses = new ArrayList<>();
        this.grades = new ArrayList<>();
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
            course.addStudent(this);
        }
    }

    public void dropCourse(Course course) {
        enrolledCourses.remove(course);
        course.removeStudent(this);
        // Remove any grades associated with this course
        grades.removeIf(grade -> grade.getCourse().equals(course));
    }

    public double calculateCGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }

        double totalPoints = 0.0;
        int totalCourses = 0;

        for (GradeEntry grade : grades) {
            if (grade.getLetterGrade() != null && !grade.getLetterGrade().equals("N/A")) {
                totalPoints += grade.getGradePoint();
                totalCourses++;
            }
        }

        return totalCourses > 0 ? totalPoints / totalCourses : 0.0;
    }

    public List<GradeEntry> getGrades() {
        return grades;
    }

    public void addGrade(GradeEntry grade) {
        grades.add(grade);
    }

    public GradeEntry getGradeForCourse(Course course) {
        for (GradeEntry grade : grades) {
            if (grade.getCourse().equals(course)) {
                return grade;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
