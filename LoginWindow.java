import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    private static final String BACKGROUND_IMAGE_PATH = "C:\\Users\\ABCD\\OneDrive\\Desktop\\codeforlibrarymanagementsystem\\Librarians.png";

    public LoginWindow() {
        setTitle("Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BackgroundPanel background = new BackgroundPanel(BACKGROUND_IMAGE_PATH);
        background.setLayout(new GridBagLayout()); 
        setContentPane(background);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");

        Font customFont = new Font("Clab Personal Use", Font.PLAIN, 18);
        
        usernameLabel.setFont(customFont);
        usernameLabel.setOpaque(true);
        usernameLabel.setBackground(new Color(173, 216, 230)); 

        passwordLabel.setFont(customFont);
        passwordLabel.setOpaque(true);
        passwordLabel.setBackground(new Color(144, 238, 144)); 
        
        usernameField.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        passwordField.setFont(customFont);
        
        loginButton.setFont(customFont);
        signupButton.setFont(customFont);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        
        gbc.gridx = 0; gbc.gridy = 0; background.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; background.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; background.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; background.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        background.add(loginButton, gbc);

        gbc.gridy = 3; 
        background.add(signupButton, gbc);

        loginButton.addActionListener(e -> checkLogin());
        signupButton.addActionListener(e -> registerUser());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void checkLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String sql = "SELECT * FROM login WHERE USERNAME=? AND PASSWORD=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                new MainWindow(); 
                this.dispose();    
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.\nPlease Signup.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String sql = "INSERT INTO login (USERNAME, PASSWORD) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "User registered successfully.", "Signup Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: Username might already exist or " + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginWindow());
    }
}