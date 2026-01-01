import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewBooksWindow extends JFrame {
    
    private static final String BACKGROUND_IMAGE_PATH = "C:\\Users\\ABCD\\OneDrive\\Desktop\\codeforlibrarymanagementsystem\\Library image 3.png";

    public ViewBooksWindow() {
        setTitle("View Books");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        BackgroundPanel background = new BackgroundPanel(BACKGROUND_IMAGE_PATH);
        background.setLayout(new BorderLayout());
        setContentPane(background);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("All Books", createBooksTablePanel());
        tabbedPane.addTab("Issued Books", createIssuedBooksTablePanel());
        
        tabbedPane.setPreferredSize(new Dimension(900, 550)); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        wrapper.add(tabbedPane, gbc);
        
        JPanel buttonPanel = new JPanel();
        JButton homeButton = new JButton("Home");
        
        Font customFont = new Font("Clab Personal Use", Font.PLAIN, 18);
        homeButton.setFont(customFont);
        homeButton.setBackground(new Color(173, 216, 230));

        homeButton.addActionListener(e -> { new MainWindow(); this.dispose(); });
        buttonPanel.add(homeButton);

        background.add(wrapper, BorderLayout.CENTER);
        background.add(buttonPanel, BorderLayout.SOUTH); 

        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private JScrollPane createBooksTablePanel() {
        String[] columnNames = {"Book ID", "Book Name", "Author", "Status"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        table.getTableHeader().setFont(new Font("Georgia", Font.BOLD, 10));
        table.setFont(new Font("Georgia", Font.BOLD, 10));
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM BOOKS")) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("BOOK_ID"),
                    rs.getString("BOOK_NAME"),
                    rs.getString("AUTHOR"),
                    rs.getString("STATUS")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading All Books: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return scrollPane;
    }

    private JScrollPane createIssuedBooksTablePanel() {
        String[] columnNames = {"Book ID", "Book Name", "Student Name", "GR No", "Time Entry"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        
        table.getTableHeader().setFont(new Font("Georgia", Font.BOLD, 10));
        table.setFont(new Font("Georgia", Font.BOLD, 10));

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ISSUED")) {
            
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("BOOK_ID"),
                    rs.getString("BOOK_NAME"),
                    rs.getString("STUDENT_NAME"),
                    rs.getInt("GR_NO"),
                    rs.getTimestamp("TIME_ENTRY")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading Issued Books: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return scrollPane;
    }
}