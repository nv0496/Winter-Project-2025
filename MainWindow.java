import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private static final String BACKGROUND_IMAGE_PATH = "C:\\Users\\ABCD\\OneDrive\\Desktop\\codeforlibrarymanagementsystem\\LIBRARIES.png";
    public MainWindow() {
        setTitle("Library Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BackgroundPanel background = new BackgroundPanel(BACKGROUND_IMAGE_PATH);
        background.setLayout(null);
        setContentPane(background);

        JButton btnAddBooks = new JButton("ADD/DELETE BOOKS");
        JButton btnDisplayBooks = new JButton("DISPLAY BOOKS");
        JButton btnIssueReturn = new JButton("ISSUE/RETURN BOOKS");

        int width = (int) (1000 * 0.6);
        int height = (int) (700 * 0.1);
        int x = (int) (1000 * 0.2);
        
        btnAddBooks.setBounds(x, (int) (700 * 0.4), width, height);
        btnDisplayBooks.setBounds(x, (int) (700 * 0.5), width, height);
        btnIssueReturn.setBounds(x, (int) (700 * 0.6), width, height);

        btnAddBooks.addActionListener(e -> new AddDeleteBooksWindow());
        btnDisplayBooks.addActionListener(e -> new ViewBooksWindow());
        btnIssueReturn.addActionListener(e -> new IssueReturnBooksWindow());

        background.add(btnAddBooks);
        background.add(btnDisplayBooks);
        background.add(btnIssueReturn);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}