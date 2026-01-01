import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class IssueReturnBooksWindow extends JFrame {
    private JTextField bookIdField, bookNameField, studentNameField, grNoField;

    private static final String BACKGROUND_IMAGE_PATH = "C:\\Users\\ABCD\\OneDrive\\Desktop\\codeforlibrarymanagementsystem\\LIBRARY 2.png";

    public IssueReturnBooksWindow() {
        setTitle("Issue/Return Books");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        BackgroundPanel background = new BackgroundPanel(BACKGROUND_IMAGE_PATH);
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        JLabel bookIdLabel = new JLabel("Book ID:");
        JLabel bookNameLabel = new JLabel("Book Name:");
        JLabel studentNameLabel = new JLabel("Name of the student:");
        JLabel grNoLabel = new JLabel("GR NO:");

        bookIdField = new JTextField(15);
        bookNameField = new JTextField(15);
        studentNameField = new JTextField(15);
        grNoField = new JTextField(15);

        JButton issueButton = new JButton("Issue Book:");
        JButton returnButton = new JButton("Return Book");
        JButton homeButton = new JButton("Home");

        Font customFont = new Font("Clab Personal Use", Font.PLAIN, 18);
        
        setLabelStyle(bookIdLabel, new Color(173, 216, 230), customFont);
        setLabelStyle(bookNameLabel, new Color(144, 238, 144), customFont);
        setLabelStyle(studentNameLabel, new Color(255, 255, 224), customFont);
        setLabelStyle(grNoLabel, new Color(255, 182, 193), customFont);
        
        bookIdField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        bookNameField.setFont(customFont);
        studentNameField.setFont(customFont);
        grNoField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        
        issueButton.setFont(customFont); issueButton.setBackground(new Color(173, 216, 230)); 
        returnButton.setFont(customFont); returnButton.setBackground(new Color(255, 182, 193)); 
        homeButton.setFont(customFont); homeButton.setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 0; background.add(bookIdLabel, gbc);
        gbc.gridy = 1; background.add(bookNameLabel, gbc);
        gbc.gridy = 2; background.add(studentNameLabel, gbc);
        gbc.gridy = 3; background.add(grNoLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; gbc.gridy = 0; background.add(bookIdField, gbc);
        gbc.gridy = 1; background.add(bookNameField, gbc);
        gbc.gridy = 2; background.add(studentNameField, gbc);
        gbc.gridy = 3; background.add(grNoField, gbc);

        gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 1; gbc.gridwidth = 1; background.add(issueButton, gbc);
        gbc.gridx = 2; gbc.gridwidth = 1; background.add(returnButton, gbc);
        gbc.gridx = 3; gbc.gridwidth = 1; background.add(homeButton, gbc);

        issueButton.addActionListener(e -> issueBooks());
        returnButton.addActionListener(e -> returnBooks());
        homeButton.addActionListener(e -> { new MainWindow(); this.dispose(); });

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setLabelStyle(JLabel label, Color color, Font font) {
        label.setFont(font);
        label.setOpaque(true);
        label.setBackground(color);
    }

    private void issueBooks() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());
            String bookName = bookNameField.getText();
            String studentName = studentNameField.getText();
            int grNo = Integer.parseInt(grNoField.getText());

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement checkStmt = conn.prepareStatement("SELECT STATUS FROM BOOKS WHERE BOOK_ID = ?");
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next() || "ISSUED".equals(rs.getString("STATUS"))) {
                    JOptionPane.showMessageDialog(this, "Book ID does not exist or is already issued.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                conn.setAutoCommit(false); 

                PreparedStatement updateStmt = conn.prepareStatement("UPDATE BOOKS SET STATUS='ISSUED' WHERE BOOK_ID=?");
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ISSUED (BOOK_ID, BOOK_NAME, STUDENT_NAME, GR_NO) VALUES(?, ?, ?, ?)");
                insertStmt.setInt(1, bookId);
                insertStmt.setString(2, bookName);
                insertStmt.setString(3, studentName);
                insertStmt.setInt(4, grNo);
                insertStmt.executeUpdate();

                conn.commit(); 
                JOptionPane.showMessageDialog(this, "THE RECORD HAS BEEN SUCCESSFULLY UPDATED", "UPDATED", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();

            } catch (SQLException ex) {
                try (Connection conn = DBConnection.getConnection()) { conn.rollback(); } catch (SQLException rollbackEx) { }
                JOptionPane.showMessageDialog(this, "Error occurred while updating: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID and GR NO must be numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnBooks() {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());

            try (Connection conn = DBConnection.getConnection()) {
                
                PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM ISSUED WHERE BOOK_ID = ?");
                checkStmt.setInt(1, bookId);
                ResultSet rs = checkStmt.executeQuery();

                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Book ID not found in issued records.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                conn.setAutoCommit(false); 

                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM ISSUED WHERE BOOK_ID=?");
                deleteStmt.setInt(1, bookId);
                deleteStmt.executeUpdate();

                PreparedStatement updateStmt = conn.prepareStatement("UPDATE BOOKS SET STATUS='RETURNED' WHERE BOOK_ID=?");
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                conn.commit(); 
                JOptionPane.showMessageDialog(this, "THE RECORD HAS BEEN SUCCESSFULLY UPDATED", "UPDATED", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();

            } catch (SQLException ex) {
                try (Connection conn = DBConnection.getConnection()) { conn.rollback(); } catch (SQLException rollbackEx) { }
                JOptionPane.showMessageDialog(this, "UPDATION WAS UNSUCCESSFUL: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}