
public class GradeEntry {
    private Student student;
    private Course course;
    private double score;
    private String letterGrade;
    private double gradePoint;

    public GradeEntry(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.letterGrade = "N/A";
        this.gradePoint = 0.0;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getScore() {
        return score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public void setScore(double score) {
        this.score = score;
        calculateLetterGradeAndPoints();
    }

    private void calculateLetterGradeAndPoints() {
        if (score >= 97) {
            letterGrade = "A+";
            gradePoint = 4.0;
        } else if (score >= 93) {
            letterGrade = "A";
            gradePoint = 4.0;
        } else if (score >= 90) {
            letterGrade = "A-";
            gradePoint = 3.7;
        } else if (score >= 87) {
            letterGrade = "B+";
            gradePoint = 3.3;
        } else if (score >= 83) {
            letterGrade = "B";
            gradePoint = 3.0;
        } else if (score >= 80) {
            letterGrade = "B-";
            gradePoint = 2.7;
        } else if (score >= 77) {
            letterGrade = "C+";
            gradePoint = 2.3;
        } else if (score >= 73) {
            letterGrade = "C";
            gradePoint = 2.0;
        } else if (score >= 70) {
            letterGrade = "C-";
            gradePoint = 1.7;
        } else if (score >= 67) {
            letterGrade = "D+";
            gradePoint = 1.3;
        } else if (score >= 63) {
            letterGrade = "D";
            gradePoint = 1.0;
        } else if (score >= 60) {
            letterGrade = "D-";
            gradePoint = 0.7;
        } else {
            letterGrade = "F";
            gradePoint = 0.0;
        }
    }

    @Override
    public String toString() {
        return "Student: " + student.getName() + ", Course: " + course.getName() + 
               ", Grade: " + letterGrade + " (" + score + ")";
    }
}
