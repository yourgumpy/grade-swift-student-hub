
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class GradingSystemGUI extends JFrame {
    // Colors
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 240); // #F0F0F0
    private static final Color TEXT_COLOR = new Color(51, 51, 51); // #333333
    private static final Color BUTTON_COLOR = new Color(0, 123, 255); // #007BFF
    private static final Color BUTTON_HOVER_COLOR = new Color(0, 86, 179); // #0056b3
    private static final Color TABLE_GRID_COLOR = new Color(224, 224, 240); // #E0E0F0
    private static final Color TABLE_ALT_ROW_COLOR = new Color(249, 249, 249); // #F9F9F9
    private static final Color FIELD_BORDER_COLOR = new Color(211, 211, 211); // #D3D3D3

    // Font
    private static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 16);

    private DataManager dataManager;
    private Auth auth;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Current user
    private Student currentStudent;
    private Teacher currentTeacher;

    // Panels
    private JPanel startPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel studentDashboardPanel;
    private JPanel teacherDashboardPanel;
    private JPanel enrollCoursePanel;
    private JPanel dropCoursePanel;
    private JPanel addCoursePanel;
    private JPanel enterGradesPanel;

    public GradingSystemGUI() {
        this.dataManager = new DataManager();
        this.auth = new Auth(dataManager);

        setTitle("Student Grading System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup main panel with card layout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_COLOR);

        createPanels();
        add(mainPanel);
        setVisible(true);
    }

    private void createPanels() {
        createStartPanel();
        createLoginPanel();
        createRegisterPanel();
        createStudentDashboardPanel();
        createTeacherDashboardPanel();
        createEnrollCoursePanel();
        createDropCoursePanel();
        createAddCoursePanel();
        createEnterGradesPanel();
    }

    private void createStartPanel() {
        startPanel = new JPanel();
        startPanel.setLayout(new GridBagLayout());
        startPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Student Grading System", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JButton studentLoginButton = createStyledButton("Student Login");
        JButton teacherLoginButton = createStyledButton("Teacher Login");
        JButton registerButton = createStyledButton("Register");
        
        studentLoginButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
            ((JComboBox) loginPanel.getComponent(2)).setSelectedItem("Student");
        });
        
        teacherLoginButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "login");
            ((JComboBox) loginPanel.getComponent(2)).setSelectedItem("Teacher");
        });
        
        registerButton.addActionListener(e -> cardLayout.show(mainPanel, "register"));
        
        gbc.insets = new Insets(0, 0, 30, 0);
        startPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(10, 0, 10, 0);
        startPanel.add(studentLoginButton, gbc);
        startPanel.add(teacherLoginButton, gbc);
        startPanel.add(registerButton, gbc);
        
        mainPanel.add(startPanel, "start");
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Login", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(REGULAR_FONT);
        roleLabel.setForeground(TEXT_COLOR);
        
        String[] roles = {"Student", "Teacher"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(REGULAR_FONT);
        
        JLabel idLabel = new JLabel("Enter ID:");
        idLabel.setFont(REGULAR_FONT);
        idLabel.setForeground(TEXT_COLOR);
        
        JTextField idField = new JTextField(20);
        idField.setFont(REGULAR_FONT);
        
        JButton loginButton = createStyledButton("Login");
        JButton backButton = createStyledButton("Back");
        
        loginButton.addActionListener(e -> {
            String id = idField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();
            
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ID", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (role.equals("Student")) {
                currentStudent = auth.loginStudent(id);
                if (currentStudent != null) {
                    updateStudentDashboard();
                    cardLayout.show(mainPanel, "studentDashboard");
                }
            } else if (role.equals("Teacher")) {
                currentTeacher = auth.loginTeacher(id);
                if (currentTeacher != null) {
                    updateTeacherDashboard();
                    cardLayout.show(mainPanel, "teacherDashboard");
                }
            }
            
            idField.setText("");
        });
        
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "start");
            idField.setText("");
        });
        
        gbc.insets = new Insets(0, 0, 30, 0);
        loginPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(5, 0, 5, 0);
        loginPanel.add(roleLabel, gbc);
        loginPanel.add(roleComboBox, gbc);
        loginPanel.add(idLabel, gbc);
        loginPanel.add(idField, gbc);
        
        gbc.insets = new Insets(20, 0, 10, 0);
        loginPanel.add(loginButton, gbc);
        
        gbc.insets = new Insets(10, 0, 10, 0);
        loginPanel.add(backButton, gbc);
        
        mainPanel.add(loginPanel, "login");
    }

    private void createRegisterPanel() {
        registerPanel = new JPanel(new GridBagLayout());
        registerPanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Register", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setFont(REGULAR_FONT);
        roleLabel.setForeground(TEXT_COLOR);
        
        String[] roles = {"Student", "Teacher"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(REGULAR_FONT);
        
        JLabel nameLabel = new JLabel("Enter Name:");
        nameLabel.setFont(REGULAR_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        
        JTextField nameField = new JTextField(20);
        nameField.setFont(REGULAR_FONT);
        
        JLabel idLabel = new JLabel("Enter ID:");
        idLabel.setFont(REGULAR_FONT);
        idLabel.setForeground(TEXT_COLOR);
        
        JTextField idField = new JTextField(20);
        idField.setFont(REGULAR_FONT);
        
        JButton registerButton = createStyledButton("Register");
        JButton backButton = createStyledButton("Back");
        
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();
            
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and ID cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (role.equals("Student")) {
                currentStudent = auth.registerStudent(name, id);
                if (currentStudent != null) {
                    JOptionPane.showMessageDialog(this, "Student registered successfully!", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    updateStudentDashboard();
                    cardLayout.show(mainPanel, "studentDashboard");
                }
            } else if (role.equals("Teacher")) {
                currentTeacher = auth.registerTeacher(name, id);
                if (currentTeacher != null) {
                    JOptionPane.showMessageDialog(this, "Teacher registered successfully!", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    updateTeacherDashboard();
                    cardLayout.show(mainPanel, "teacherDashboard");
                }
            }
            
            nameField.setText("");
            idField.setText("");
        });
        
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "start");
            nameField.setText("");
            idField.setText("");
        });
        
        gbc.insets = new Insets(0, 0, 30, 0);
        registerPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(5, 0, 5, 0);
        registerPanel.add(roleLabel, gbc);
        registerPanel.add(roleComboBox, gbc);
        registerPanel.add(nameLabel, gbc);
        registerPanel.add(nameField, gbc);
        registerPanel.add(idLabel, gbc);
        registerPanel.add(idField, gbc);
        
        gbc.insets = new Insets(20, 0, 10, 0);
        registerPanel.add(registerButton, gbc);
        
        gbc.insets = new Insets(10, 0, 10, 0);
        registerPanel.add(backButton, gbc);
        
        mainPanel.add(registerPanel, "register");
    }

    private void createStudentDashboardPanel() {
        studentDashboardPanel = new JPanel(new BorderLayout(20, 20));
        studentDashboardPanel.setBackground(BACKGROUND_COLOR);
        studentDashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Top panel for student info
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        topPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel nameLabel = new JLabel();
        nameLabel.setFont(SUBTITLE_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        
        JLabel idLabel = new JLabel();
        idLabel.setFont(SUBTITLE_FONT);
        idLabel.setForeground(TEXT_COLOR);
        
        JLabel cgpaLabel = new JLabel();
        cgpaLabel.setFont(SUBTITLE_FONT);
        cgpaLabel.setForeground(TEXT_COLOR);
        
        topPanel.add(nameLabel);
        topPanel.add(idLabel);
        topPanel.add(cgpaLabel);
        
        // Center panel for courses table
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel coursesLabel = new JLabel("Enrolled Courses");
        coursesLabel.setFont(SUBTITLE_FONT);
        coursesLabel.setForeground(TEXT_COLOR);
        
        String[] columnNames = {"Course Name", "Instructor", "Grade"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable coursesTable = new JTable(tableModel);
        coursesTable.setFont(REGULAR_FONT);
        coursesTable.setRowHeight(25);
        coursesTable.setGridColor(TABLE_GRID_COLOR);
        coursesTable.setSelectionBackground(BUTTON_COLOR);
        coursesTable.setSelectionForeground(Color.WHITE);
        
        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < coursesTable.getColumnCount(); i++) {
            coursesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Alternating row colors
        coursesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW_COLOR);
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR));
        
        centerPanel.add(coursesLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        
        JButton enrollButton = createStyledButton("Enroll in Course");
        JButton dropButton = createStyledButton("Drop Course");
        JButton logoutButton = createStyledButton("Logout");
        
        enrollButton.addActionListener(e -> {
            updateEnrollCoursePanel();
            cardLayout.show(mainPanel, "enrollCourse");
        });
        
        dropButton.addActionListener(e -> {
            updateDropCoursePanel();
            cardLayout.show(mainPanel, "dropCourse");
        });
        
        logoutButton.addActionListener(e -> {
            currentStudent = null;
            cardLayout.show(mainPanel, "start");
        });
        
        bottomPanel.add(enrollButton);
        bottomPanel.add(dropButton);
        bottomPanel.add(logoutButton);
        
        studentDashboardPanel.add(topPanel, BorderLayout.NORTH);
        studentDashboardPanel.add(centerPanel, BorderLayout.CENTER);
        studentDashboardPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(studentDashboardPanel, "studentDashboard");
    }

    private void updateStudentDashboard() {
        if (currentStudent == null) return;
        
        JPanel topPanel = (JPanel) studentDashboardPanel.getComponent(0);
        
        JLabel nameLabel = (JLabel) topPanel.getComponent(0);
        JLabel idLabel = (JLabel) topPanel.getComponent(1);
        JLabel cgpaLabel = (JLabel) topPanel.getComponent(2);
        
        nameLabel.setText("Name: " + currentStudent.getName());
        idLabel.setText("ID: " + currentStudent.getId());
        cgpaLabel.setText("CGPA: " + CGPA.format(currentStudent.calculateCGPA()));
        
        JPanel centerPanel = (JPanel) studentDashboardPanel.getComponent(1);
        JScrollPane scrollPane = (JScrollPane) centerPanel.getComponent(1);
        JTable coursesTable = (JTable) scrollPane.getViewport().getView();
        DefaultTableModel tableModel = (DefaultTableModel) coursesTable.getModel();
        
        tableModel.setRowCount(0);
        
        for (Course course : currentStudent.getEnrolledCourses()) {
            GradeEntry gradeEntry = currentStudent.getGradeForCourse(course);
            String letterGrade = (gradeEntry != null && course.areGradesPublished()) ? gradeEntry.getLetterGrade() : "N/A";
            
            tableModel.addRow(new Object[]{
                course.getName(),
                course.getInstructor().getName(),
                letterGrade
            });
        }
    }

    private void createTeacherDashboardPanel() {
        teacherDashboardPanel = new JPanel(new BorderLayout(20, 20));
        teacherDashboardPanel.setBackground(BACKGROUND_COLOR);
        teacherDashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Top panel for teacher info
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        topPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel nameLabel = new JLabel();
        nameLabel.setFont(SUBTITLE_FONT);
        nameLabel.setForeground(TEXT_COLOR);
        
        JLabel idLabel = new JLabel();
        idLabel.setFont(SUBTITLE_FONT);
        idLabel.setForeground(TEXT_COLOR);
        
        topPanel.add(nameLabel);
        topPanel.add(idLabel);
        
        // Center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel coursesLabel = new JLabel("Your Courses");
        coursesLabel.setFont(SUBTITLE_FONT);
        coursesLabel.setForeground(TEXT_COLOR);
        
        JComboBox<Course> coursesComboBox = new JComboBox<>();
        coursesComboBox.setFont(REGULAR_FONT);
        
        JPanel actionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);
        
        JButton enterGradesButton = createStyledButton("Enter Grades");
        JButton publishGradesButton = createStyledButton("Publish Grades");
        JButton viewStatsButton = createStyledButton("View Stats");
        JButton addCourseButton = createStyledButton("Add Course");
        
        centerPanel.add(coursesLabel, BorderLayout.NORTH);
        centerPanel.add(coursesComboBox, BorderLayout.CENTER);
        
        actionPanel.add(enterGradesButton);
        actionPanel.add(publishGradesButton);
        actionPanel.add(viewStatsButton);
        actionPanel.add(addCourseButton);
        
        enterGradesButton.addActionListener(e -> {
            Course selectedCourse = (Course) coursesComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            updateEnterGradesPanel(selectedCourse);
            cardLayout.show(mainPanel, "enterGrades");
        });
        
        publishGradesButton.addActionListener(e -> {
            Course selectedCourse = (Course) coursesComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to publish grades for " + selectedCourse.getName() + "?", 
                "Confirm Publish", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                selectedCourse.setGradesPublished(true);
                dataManager.saveCourses();
                JOptionPane.showMessageDialog(this, "Grades published successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        viewStatsButton.addActionListener(e -> {
            Course selectedCourse = (Course) coursesComboBox.getSelectedItem();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            double highestGrade = 0;
            double lowestGrade = 100;
            double totalGrade = 0;
            int count = 0;
            
            for (Student student : selectedCourse.getEnrolledStudents()) {
                GradeEntry grade = student.getGradeForCourse(selectedCourse);
                if (grade != null) {
                    double score = grade.getScore();
                    highestGrade = Math.max(highestGrade, score);
                    lowestGrade = Math.min(lowestGrade, score);
                    totalGrade += score;
                    count++;
                }
            }
            
            if (count == 0) {
                JOptionPane.showMessageDialog(this, "No grades available for this course", "Statistics", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            double averageGrade = totalGrade / count;
            
            JOptionPane.showMessageDialog(this, 
                "Course: " + selectedCourse.getName() + "\n" +
                "Number of students: " + selectedCourse.getEnrolledStudents().size() + "\n" +
                "Number of graded students: " + count + "\n" +
                "Highest Grade: " + highestGrade + "\n" +
                "Lowest Grade: " + lowestGrade + "\n" +
                "Average Grade: " + String.format("%.2f", averageGrade),
                "Course Statistics", JOptionPane.INFORMATION_MESSAGE);
        });
        
        addCourseButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "addCourse");
        });
        
        // Bottom panel for logout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        
        JButton logoutButton = createStyledButton("Logout");
        
        logoutButton.addActionListener(e -> {
            currentTeacher = null;
            cardLayout.show(mainPanel, "start");
        });
        
        bottomPanel.add(logoutButton);
        
        centerPanel.add(actionPanel, BorderLayout.SOUTH);
        
        teacherDashboardPanel.add(topPanel, BorderLayout.NORTH);
        teacherDashboardPanel.add(centerPanel, BorderLayout.CENTER);
        teacherDashboardPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(teacherDashboardPanel, "teacherDashboard");
    }

    private void updateTeacherDashboard() {
        if (currentTeacher == null) return;
        
        JPanel topPanel = (JPanel) teacherDashboardPanel.getComponent(0);
        
        JLabel nameLabel = (JLabel) topPanel.getComponent(0);
        JLabel idLabel = (JLabel) topPanel.getComponent(1);
        
        nameLabel.setText("Name: " + currentTeacher.getName());
        idLabel.setText("ID: " + currentTeacher.getId());
        
        JPanel centerPanel = (JPanel) teacherDashboardPanel.getComponent(1);
        JComboBox<Course> coursesComboBox = (JComboBox<Course>) centerPanel.getComponent(1);
        
        coursesComboBox.removeAllItems();
        
        for (Course course : currentTeacher.getTeachingCourses()) {
            coursesComboBox.addItem(course);
        }
    }

    private void createEnrollCoursePanel() {
        enrollCoursePanel = new JPanel(new BorderLayout(20, 20));
        enrollCoursePanel.setBackground(BACKGROUND_COLOR);
        enrollCoursePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Available Courses", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        DefaultListModel<Course> listModel = new DefaultListModel<>();
        JList<Course> coursesList = new JList<>(listModel);
        coursesList.setFont(REGULAR_FONT);
        coursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(BUTTON_COLOR);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(index % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW_COLOR);
                    c.setForeground(TEXT_COLOR);
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(coursesList);
        scrollPane.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton enrollButton = createStyledButton("Enroll");
        JButton backButton = createStyledButton("Back");
        
        enrollButton.addActionListener(e -> {
            Course selectedCourse = coursesList.getSelectedValue();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (currentStudent.getEnrolledCourses().contains(selectedCourse)) {
                JOptionPane.showMessageDialog(this, "You are already enrolled in this course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            currentStudent.enrollInCourse(selectedCourse);
            dataManager.saveCourses();
            
            JOptionPane.showMessageDialog(this, "Enrolled in " + selectedCourse.getName() + " successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            updateStudentDashboard();
            cardLayout.show(mainPanel, "studentDashboard");
        });
        
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "studentDashboard");
        });
        
        buttonPanel.add(enrollButton);
        buttonPanel.add(backButton);
        
        enrollCoursePanel.add(titleLabel, BorderLayout.NORTH);
        enrollCoursePanel.add(scrollPane, BorderLayout.CENTER);
        enrollCoursePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(enrollCoursePanel, "enrollCourse");
    }

    private void updateEnrollCoursePanel() {
        if (currentStudent == null) return;
        
        JScrollPane scrollPane = (JScrollPane) enrollCoursePanel.getComponent(1);
        JList<Course> coursesList = (JList<Course>) scrollPane.getViewport().getView();
        DefaultListModel<Course> listModel = (DefaultListModel<Course>) coursesList.getModel();
        
        listModel.clear();
        
        for (Course course : dataManager.getCourses()) {
            if (!currentStudent.getEnrolledCourses().contains(course)) {
                listModel.addElement(course);
            }
        }
    }

    private void createDropCoursePanel() {
        dropCoursePanel = new JPanel(new BorderLayout(20, 20));
        dropCoursePanel.setBackground(BACKGROUND_COLOR);
        dropCoursePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Enrolled Courses", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        DefaultListModel<Course> listModel = new DefaultListModel<>();
        JList<Course> coursesList = new JList<>(listModel);
        coursesList.setFont(REGULAR_FONT);
        coursesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    c.setBackground(BUTTON_COLOR);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(index % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW_COLOR);
                    c.setForeground(TEXT_COLOR);
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(coursesList);
        scrollPane.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton dropButton = createStyledButton("Drop");
        JButton backButton = createStyledButton("Back");
        
        dropButton.addActionListener(e -> {
            Course selectedCourse = coursesList.getSelectedValue();
            if (selectedCourse == null) {
                JOptionPane.showMessageDialog(this, "Please select a course", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to drop " + selectedCourse.getName() + "?", 
                "Confirm Drop", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                currentStudent.dropCourse(selectedCourse);
                dataManager.saveCourses();
                dataManager.saveGrades();
                
                JOptionPane.showMessageDialog(this, "Dropped " + selectedCourse.getName() + " successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                updateStudentDashboard();
                cardLayout.show(mainPanel, "studentDashboard");
            }
        });
        
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "studentDashboard");
        });
        
        buttonPanel.add(dropButton);
        buttonPanel.add(backButton);
        
        dropCoursePanel.add(titleLabel, BorderLayout.NORTH);
        dropCoursePanel.add(scrollPane, BorderLayout.CENTER);
        dropCoursePanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(dropCoursePanel, "dropCourse");
    }

    private void updateDropCoursePanel() {
        if (currentStudent == null) return;
        
        JScrollPane scrollPane = (JScrollPane) dropCoursePanel.getComponent(1);
        JList<Course> coursesList = (JList<Course>) scrollPane.getViewport().getView();
        DefaultListModel<Course> listModel = (DefaultListModel<Course>) coursesList.getModel();
        
        listModel.clear();
        
        for (Course course : currentStudent.getEnrolledCourses()) {
            listModel.addElement(course);
        }
    }

    private void createAddCoursePanel() {
        addCoursePanel = new JPanel(new GridBagLayout());
        addCoursePanel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Add New Course", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel courseNameLabel = new JLabel("Course Name:");
        courseNameLabel.setFont(REGULAR_FONT);
        courseNameLabel.setForeground(TEXT_COLOR);
        
        JTextField courseNameField = new JTextField(20);
        courseNameField.setFont(REGULAR_FONT);
        
        JButton addButton = createStyledButton("Add Course");
        JButton backButton = createStyledButton("Back");
        
        addButton.addActionListener(e -> {
            String courseName = courseNameField.getText().trim();
            
            if (courseName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if course name already exists
            if (dataManager.findCourseByName(courseName) != null) {
                JOptionPane.showMessageDialog(this, "A course with this name already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Course newCourse = new Course(courseName, currentTeacher);
            currentTeacher.addCourse(newCourse);
            dataManager.addCourse(newCourse);
            
            JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            courseNameField.setText("");
            updateTeacherDashboard();
            cardLayout.show(mainPanel, "teacherDashboard");
        });
        
        backButton.addActionListener(e -> {
            courseNameField.setText("");
            cardLayout.show(mainPanel, "teacherDashboard");
        });
        
        gbc.insets = new Insets(0, 0, 30, 0);
        addCoursePanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(5, 0, 5, 0);
        addCoursePanel.add(courseNameLabel, gbc);
        addCoursePanel.add(courseNameField, gbc);
        
        gbc.insets = new Insets(20, 0, 10, 0);
        addCoursePanel.add(addButton, gbc);
        
        gbc.insets = new Insets(10, 0, 10, 0);
        addCoursePanel.add(backButton, gbc);
        
        mainPanel.add(addCoursePanel, "addCourse");
    }

    private void createEnterGradesPanel() {
        enterGradesPanel = new JPanel(new BorderLayout(20, 20));
        enterGradesPanel.setBackground(BACKGROUND_COLOR);
        enterGradesPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Enter Grades", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel courseLabel = new JLabel();
        courseLabel.setFont(SUBTITLE_FONT);
        courseLabel.setForeground(TEXT_COLOR);
        
        String[] columnNames = {"Student Name", "Student ID", "Current Grade", "New Score (0-100)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        
        JTable gradesTable = new JTable(tableModel);
        gradesTable.setFont(REGULAR_FONT);
        gradesTable.setRowHeight(25);
        gradesTable.setGridColor(TABLE_GRID_COLOR);
        
        // Only make the score column editable
        gradesTable.setDefaultEditor(Object.class, null);
        gradesTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()));
        
        // Center align table cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < gradesTable.getColumnCount(); i++) {
            gradesTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Alternating row colors
        gradesTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : TABLE_ALT_ROW_COLOR);
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(FIELD_BORDER_COLOR));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton saveButton = createStyledButton("Save Grades");
        JButton backButton = createStyledButton("Back");
        
        saveButton.addActionListener(e -> {
            Course course = (Course) gradesTable.getClientProperty("course");
            if (course == null) return;
            
            int rowCount = tableModel.getRowCount();
            boolean anyGradesUpdated = false;
            
            for (int i = 0; i < rowCount; i++) {
                String studentId = (String) tableModel.getValueAt(i, 1);
                String newScoreStr = (String) tableModel.getValueAt(i, 3);
                
                if (newScoreStr == null || newScoreStr.trim().isEmpty()) {
                    continue;
                }
                
                try {
                    double newScore = Double.parseDouble(newScoreStr);
                    
                    if (newScore < 0 || newScore > 100) {
                        JOptionPane.showMessageDialog(this, "Score must be between 0 and 100", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    Student student = dataManager.findStudentById(studentId);
                    if (student != null) {
                        GradeEntry gradeEntry = student.getGradeForCourse(course);
                        
                        if (gradeEntry == null) {
                            gradeEntry = new GradeEntry(student, course);
                            student.addGrade(gradeEntry);
                        }
                        
                        gradeEntry.setScore(newScore);
                        anyGradesUpdated = true;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid score format. Please enter a number between 0 and 100", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            if (anyGradesUpdated) {
                dataManager.saveGrades();
                JOptionPane.showMessageDialog(this, "Grades saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateEnterGradesPanel(course);
            } else {
                JOptionPane.showMessageDialog(this, "No grades were updated", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        backButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "teacherDashboard");
        });
        
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(titleLabel);
        
        JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        coursePanel.setBackground(BACKGROUND_COLOR);
        coursePanel.add(courseLabel);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(coursePanel, BorderLayout.CENTER);
        
        enterGradesPanel.add(headerPanel, BorderLayout.NORTH);
        enterGradesPanel.add(scrollPane, BorderLayout.CENTER);
        enterGradesPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainPanel.add(enterGradesPanel, "enterGrades");
    }

    private void updateEnterGradesPanel(Course course) {
        if (course == null) return;
        
        JPanel headerPanel = (JPanel) enterGradesPanel.getComponent(0);
        JPanel coursePanel = (JPanel) headerPanel.getComponent(1);
        JLabel courseLabel = (JLabel) coursePanel.getComponent(0);
        
        courseLabel.setText("Course: " + course.getName());
        
        JScrollPane scrollPane = (JScrollPane) enterGradesPanel.getComponent(1);
        JTable gradesTable = (JTable) scrollPane.getViewport().getView();
        gradesTable.putClientProperty("course", course);
        
        DefaultTableModel tableModel = (DefaultTableModel) gradesTable.getModel();
        tableModel.setRowCount(0);
        
        for (Student student : course.getEnrolledStudents()) {
            GradeEntry gradeEntry = student.getGradeForCourse(course);
            String currentGrade = (gradeEntry != null) ? gradeEntry.getLetterGrade() : "N/A";
            
            tableModel.addRow(new Object[]{
                student.getName(),
                student.getId(),
                currentGrade,
                ""
            });
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        
        return button;
    }

    public static void main(String[] args) {
        try {
            // Set the look and feel to system
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new GradingSystemGUI());
    }
}
