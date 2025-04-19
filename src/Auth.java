
import javax.swing.JOptionPane;

public class Auth {
    private DataManager dataManager;
    
    public Auth(DataManager dataManager) {
        this.dataManager = dataManager;
    }
    
    public Student loginStudent(String id) {
        Student student = dataManager.findStudentById(id);
        if (student == null) {
            JOptionPane.showMessageDialog(null, "Student ID not found", "Login Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return student;
    }
    
    public Teacher loginTeacher(String id) {
        Teacher teacher = dataManager.findTeacherById(id);
        if (teacher == null) {
            JOptionPane.showMessageDialog(null, "Teacher ID not found", "Login Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return teacher;
    }
    
    public Student registerStudent(String name, String id) {
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Check if student ID already exists
        if (dataManager.findStudentById(id) != null) {
            JOptionPane.showMessageDialog(null, "Student ID already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        Student student = new Student(name, id);
        dataManager.addStudent(student);
        return student;
    }
    
    public Teacher registerTeacher(String name, String id) {
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Name cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        if (id == null || id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Check if teacher ID already exists
        if (dataManager.findTeacherById(id) != null) {
            JOptionPane.showMessageDialog(null, "Teacher ID already exists", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        Teacher teacher = new Teacher(name, id);
        dataManager.addTeacher(teacher);
        return teacher;
    }
}
