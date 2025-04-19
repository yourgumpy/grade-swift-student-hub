
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataManager {
    private static final String STUDENTS_FILE = "students.csv";
    private static final String TEACHERS_FILE = "teachers.csv";
    private static final String COURSES_FILE = "courses.csv";
    private static final String GRADES_FILE = "grades.csv";

    private List<Student> students;
    private List<Teacher> teachers;
    private List<Course> courses;

    public DataManager() {
        this.students = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.courses = new ArrayList<>();
        loadData();
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        saveTeachers();
    }

    public void addCourse(Course course) {
        courses.add(course);
        saveCourses();
    }

    public Student findStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equals(id)) {
                return student;
            }
        }
        return null;
    }

    public Teacher findTeacherById(String id) {
        for (Teacher teacher : teachers) {
            if (teacher.getId().equals(id)) {
                return teacher;
            }
        }
        return null;
    }

    public Course findCourseByName(String name) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null;
    }

    private void loadData() {
        loadTeachers();
        loadStudents();
        loadCourses();
        loadGrades();
        
        // If no data exists, initialize with sample data
        if (teachers.isEmpty() && students.isEmpty() && courses.isEmpty()) {
            initializeSampleData();
        }
    }

    private void loadStudents() {
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    Student student = new Student(parts[0], parts[1]);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing students file found. Will be created when students are added.");
        }
    }

    private void loadTeachers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEACHERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    Teacher teacher = new Teacher(parts[0], parts[1]);
                    teachers.add(teacher);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing teachers file found. Will be created when teachers are added.");
        }
    }

    private void loadCourses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String courseName = parts[0];
                    String teacherId = parts[1];
                    boolean gradesPublished = Boolean.parseBoolean(parts[2]);
                    
                    Teacher teacher = findTeacherById(teacherId);
                    if (teacher != null) {
                        Course course = new Course(courseName, teacher);
                        course.setGradesPublished(gradesPublished);
                        courses.add(course);
                        teacher.addCourse(course);
                        
                        // Add enrolled students
                        if (parts.length > 3) {
                            for (int i = 3; i < parts.length; i++) {
                                String studentId = parts[i];
                                Student student = findStudentById(studentId);
                                if (student != null) {
                                    course.addStudent(student);
                                    student.enrollInCourse(course);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing courses file found. Will be created when courses are added.");
        }
    }

    private void loadGrades() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GRADES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String studentId = parts[0];
                    String courseName = parts[1];
                    double score = Double.parseDouble(parts[2]);
                    
                    Student student = findStudentById(studentId);
                    Course course = findCourseByName(courseName);
                    
                    if (student != null && course != null) {
                        GradeEntry grade = new GradeEntry(student, course);
                        grade.setScore(score);
                        student.addGrade(grade);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("No existing grades file found. Will be created when grades are added.");
        }
    }

    public void saveStudents() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student student : students) {
                writer.write(student.getName() + "," + student.getId());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving students: " + e.getMessage());
        }
    }

    public void saveTeachers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEACHERS_FILE))) {
            for (Teacher teacher : teachers) {
                writer.write(teacher.getName() + "," + teacher.getId());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving teachers: " + e.getMessage());
        }
    }

    public void saveCourses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COURSES_FILE))) {
            for (Course course : courses) {
                StringBuilder sb = new StringBuilder();
                sb.append(course.getName()).append(",");
                sb.append(course.getInstructor().getId()).append(",");
                sb.append(course.areGradesPublished());
                
                for (Student student : course.getEnrolledStudents()) {
                    sb.append(",").append(student.getId());
                }
                
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving courses: " + e.getMessage());
        }
    }

    public void saveGrades() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GRADES_FILE))) {
            for (Student student : students) {
                for (GradeEntry grade : student.getGrades()) {
                    writer.write(student.getId() + "," + 
                                 grade.getCourse().getName() + "," + 
                                 grade.getScore());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving grades: " + e.getMessage());
        }
    }

    private void initializeSampleData() {
        // Create two teachers
        Teacher teacher1 = new Teacher("Dr. Smith", "T001");
        Teacher teacher2 = new Teacher("Prof. Johnson", "T002");
        
        addTeacher(teacher1);
        addTeacher(teacher2);
        
        // Create two students
        Student student1 = new Student("John Doe", "S001");
        Student student2 = new Student("Jane Smith", "S002");
        
        addStudent(student1);
        addStudent(student2);
        
        // Create two courses
        Course course1 = new Course("Introduction to Computer Science", teacher1);
        Course course2 = new Course("Advanced Mathematics", teacher2);
        
        addCourse(course1);
        addCourse(course2);
        
        // Enroll students in courses
        student1.enrollInCourse(course1);
        student1.enrollInCourse(course2);
        student2.enrollInCourse(course1);
        
        // Add courses to teachers
        teacher1.addCourse(course1);
        teacher2.addCourse(course2);
        
        // Add some grades
        GradeEntry grade1 = new GradeEntry(student1, course1);
        grade1.setScore(85); // B
        student1.addGrade(grade1);
        
        GradeEntry grade2 = new GradeEntry(student1, course2);
        grade2.setScore(92); // A-
        student1.addGrade(grade2);
        
        GradeEntry grade3 = new GradeEntry(student2, course1);
        grade3.setScore(78); // C+
        student2.addGrade(grade3);
        
        // Save everything
        saveCourses();
        saveGrades();
    }
}
