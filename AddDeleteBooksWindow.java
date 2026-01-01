import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddDeleteBooksWindow extends JFrame {
    private JTextField bookIdField, bookNameField, authorField;
    private JComboBox<String> statusDropdown;
    private static final String BACKGROUND_IMAGE_PATH = "C:\\Users\\ABCD\\OneDrive\\Desktop\\codeforlibrarymanagementsystem\\LIBRARY IMAGE.png";

    public AddDeleteBooksWindow() {
        setTitle("Add/Delete Books");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        BackgroundPanel background = new BackgroundPanel(BACKGROUND_IMAGE_PATH);
        background.setLayout(new GridBagLayout()); 
        setContentPane(background);

        JLabel bookIdLabel = new JLabel("Book ID:");
        JLabel bookNameLabel = new JLabel("Book Name:");
        JLabel authorLabel = new JLabel("Author:");
        JLabel statusLabel = new JLabel("Status:");

        bookIdField = new JTextField(15);
        bookNameField = new JTextField(15);
        authorField = new JTextField(15);
        
        String[] statuses = {"RETURNED", "ISSUED"};
        statusDropdown = new JComboBox<>(statuses);
        statusDropdown.setSelectedItem("RETURNED");

        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton homeButton = new JButton("Home");

        Font customFont = new Font("Clab Personal Use", Font.PLAIN, 18);
        
        setLabelStyle(bookIdLabel, new Color(173, 216, 230), customFont);
        setLabelStyle(bookNameLabel, new Color(144, 238, 144), customFont);
        setLabelStyle(authorLabel, new Color(255, 255, 224), customFont);
        setLabelStyle(statusLabel, new Color(255, 182, 193), customFont);
        
        bookIdField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        bookNameField.setFont(customFont);
        authorField.setFont(customFont);
        statusDropdown.setFont(customFont);

        addButton.setFont(customFont); addButton.setBackground(Color.LIGHT_GRAY);
        deleteButton.setFont(customFont); deleteButton.setBackground(new Color(144, 238, 144)); 
        homeButton.setFont(customFont); homeButton.setBackground(new Color(173, 216, 230)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0; gbc.gridy = 0; background.add(bookIdLabel, gbc);
        gbc.gridy = 1; background.add(bookNameLabel, gbc);
        gbc.gridy = 2; background.add(authorLabel, gbc);
        gbc.gridy = 3; background.add(statusLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1; gbc.gridy = 0; background.add(bookIdField, gbc);
        gbc.gridy = 1; background.add(bookNameField, gbc);
        gbc.gridy = 2; background.add(authorField, gbc);
        gbc.gridy = 3; background.add(statusDropdown, gbc);

        gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0; gbc.gridwidth = 1; background.add(addButton, gbc);
        gbc.gridx = 1; background.add(deleteButton, gbc);
        gbc.gridx = 2; background.add(homeButton, gbc);

        addButton.addActionListener(e -> addBook());
        deleteButton.addActionListener(e -> deleteBook());
        homeButton.addActionListener(e -> { new MainWindow(); this.dispose(); });

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setLabelStyle(JLabel label, Color color, Font font) {
        label.setFont(font);
        label.setOpaque(true);
        label.setBackground(color);
    }

    private void addBook() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO BOOKS (BOOK_ID, BOOK_NAME, AUTHOR, STATUS) VALUES (?, ?, ?, ?)")) {
            
            int bookId = Integer.parseInt(bookIdField.getText());
            String bookName = bookNameField.getText();
            String author = authorField.getText();
            String status = (String) statusDropdown.getSelectedItem();

            pstmt.setInt(1, bookId);
            pstmt.setString(2, bookName);
            pstmt.setString(3, author);
            pstmt.setString(4, status);
            
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Book details added to the table", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Insertion of data into the table was unsuccessful: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM BOOKS WHERE BOOK_ID=?")) {
            
            int bookId = Integer.parseInt(bookIdField.getText());

            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "DELETION WAS SUCCESFUL", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Book ID not found.", "UNSUCCESFUL", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Book ID must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DELETION WAS UNSUCCESFUL: " + ex.getMessage(), "UNSUCCESFUL", JOptionPane.ERROR_MESSAGE);
        }
    }
}