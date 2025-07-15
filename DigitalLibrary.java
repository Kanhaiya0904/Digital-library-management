import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class LibrarySystem {
    JFrame frame;
    JTable bookTable;
    DefaultTableModel tableModel;
    JTextField searchField;
    ArrayList<Book> books = new ArrayList<>();
    Set<String> issuedBooks = new HashSet<>();

    class Book {
        String id, title, author, category;
        boolean isIssued;

        Book(String id, String title, String author, String category) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.category = category;
            this.isIssued = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySystem().loginScreen());
    }

    void loginScreen() {
        JFrame login = new JFrame("Library Login");
        login.setSize(350, 200);
        login.setLayout(new GridLayout(4, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton loginBtn = new JButton("Login");

        login.add(userLabel); login.add(userField);
        login.add(passLabel); login.add(passField);
        login.add(new JLabel()); login.add(loginBtn);

        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setVisible(true);

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin123")) {
                login.dispose();
                showAdminPanel();
            } else if (user.equals("user") && pass.equals("user123")) {
                login.dispose();
                showUserPanel();
            } else {
                JOptionPane.showMessageDialog(login, "Invalid credentials!");
            }
        });
    }

    void preloadBooks() {
        for (int i = 1; i <= 20; i++) {
            books.add(new Book("B" + i, "Book Title " + i, "Author " + i, i % 2 == 0 ? "Science" : "Literature"));
        }
    }

    void showAdminPanel() {
        preloadBooks();
        frame = new JFrame("Library Admin Panel");
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        String[] cols = {"ID", "Title", "Author", "Category", "Issued"};
        tableModel = new DefaultTableModel(cols, 0);
        bookTable = new JTable(tableModel);
        refreshTable();

        JPanel topPanel = new JPanel();
        JTextField idField = new JTextField(5);
        JTextField titleField = new JTextField(10);
        JTextField authorField = new JTextField(10);
        JTextField catField = new JTextField(8);
        JButton addBtn = new JButton("Add");
        JButton delBtn = new JButton("Delete");

        topPanel.add(new JLabel("ID:")); topPanel.add(idField);
        topPanel.add(new JLabel("Title:")); topPanel.add(titleField);
        topPanel.add(new JLabel("Author:")); topPanel.add(authorField);
        topPanel.add(new JLabel("Category:")); topPanel.add(catField);
        topPanel.add(addBtn); topPanel.add(delBtn);

        addBtn.addActionListener(e -> {
            books.add(new Book(idField.getText(), titleField.getText(), authorField.getText(), catField.getText()));
            refreshTable();
        });

        delBtn.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row != -1) {
                books.remove(row);
                refreshTable();
            }
        });

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    void showUserPanel() {
        preloadBooks();
        frame = new JFrame("Library User Panel");
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        String[] cols = {"ID", "Title", "Author", "Category", "Issued"};
        tableModel = new DefaultTableModel(cols, 0);
        bookTable = new JTable(tableModel);
        refreshTable();

        JPanel bottomPanel = new JPanel();
        JTextField bookIdField = new JTextField(10);
        JButton issueBtn = new JButton("Issue");
        JButton returnBtn = new JButton("Return");

        bottomPanel.add(new JLabel("Book ID:")); bottomPanel.add(bookIdField);
        bottomPanel.add(issueBtn); bottomPanel.add(returnBtn);

        issueBtn.addActionListener(e -> {
            String id = bookIdField.getText();
            for (Book b : books) {
                if (b.id.equals(id)) {
                    if (!b.isIssued) {
                        b.isIssued = true;
                        issuedBooks.add(b.id);
                        JOptionPane.showMessageDialog(frame, "Book Issued!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Already Issued!");
                    }
                    break;
                }
            }
            refreshTable();
        });

        returnBtn.addActionListener(e -> {
            String id = bookIdField.getText();
            for (Book b : books) {
                if (b.id.equals(id)) {
                    if (b.isIssued) {
                        b.isIssued = false;
                        issuedBooks.remove(b.id);
                        JOptionPane.showMessageDialog(frame, "Book Returned!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Book was not issued!");
                    }
                    break;
                }
            }
            refreshTable();
        });

        frame.add(new JScrollPane(bookTable), BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    void refreshTable() {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[]{b.id, b.title, b.author, b.category, b.isIssued ? "Yes" : "No"});
        }
    }
}
