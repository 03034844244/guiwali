import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LibraryGUI extends JFrame {
    private List<Item> items;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton loadButton;
    private JButton saveButton;

    public LibraryGUI() {
        setTitle("Library Management System");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        items = new ArrayList<>();
        tableModel = new DefaultTableModel(new Object[]{"Title", "Author", "Year"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        editButton = new JButton("Edit");
        deleteButton = new JButton("Delete");
        loadButton = new JButton("Load");
        saveButton = new JButton("Save");

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddBookDialog();
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEditBookDialog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDeleteBookDialog();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadItemsFromFile("books.txt");
            }
        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveItemsToFile("books.txt");
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void showAddBookDialog() {
        JFrame addBookFrame = new JFrame("Add Book");
        addBookFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addBookFrame.setSize(300, 200);
        addBookFrame.setLayout(new GridLayout(4, 2));

        JLabel titleLabel = new JLabel("Title");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author");
        JTextField authorField = new JTextField();
        JLabel yearLabel = new JLabel("Year");
        JTextField yearField = new JTextField();
        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());

                Item item = new Item(title, author, year);
                items.add(item);
                tableModel.addRow(new Object[]{title, author, year});

                addBookFrame.dispose();
            }
        });

        addBookFrame.add(titleLabel);
        addBookFrame.add(titleField);
        addBookFrame.add(authorLabel);
        addBookFrame.add(authorField);
        addBookFrame.add(yearLabel);
        addBookFrame.add(yearField);
        addBookFrame.add(new JLabel());
        addBookFrame.add(saveButton);

        addBookFrame.setVisible(true);
    }

    public void showEditBookDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, " select  book for edit.");
            return;
        }

        JFrame editBookFrame = new JFrame("Edit Book");
        editBookFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editBookFrame.setSize(300, 200);
        editBookFrame.setLayout(new GridLayout(4, 2));

        JLabel titleLabel = new JLabel("Title");
        JTextField titleField = new JTextField(tableModel.getValueAt(selectedRow, 0).toString());
        JLabel authorLabel = new JLabel("Author");
        JTextField authorField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString());
        JLabel yearLabel = new JLabel("Year");
        JTextField yearField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
        JButton saveButton = new JButton("Save");

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText());

                tableModel.setValueAt(title, selectedRow, 0);
                tableModel.setValueAt(author, selectedRow, 1);
                tableModel.setValueAt(year, selectedRow, 2);

                Item item = items.get(selectedRow);
                item.setTitle(title);
                item.setAuthor(author);
                item.setYear(year);

                editBookFrame.dispose();
            }
        });

        editBookFrame.add(titleLabel);
        editBookFrame.add(titleField);
        editBookFrame.add(authorLabel);
        editBookFrame.add(authorField);
        editBookFrame.add(yearLabel);
        editBookFrame.add(yearField);
        editBookFrame.add(new JLabel());
        editBookFrame.add(saveButton);

        editBookFrame.setVisible(true);
    }

    public void showDeleteBookDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, " select a book to delete.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Are you sure ?u wanna del?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            items.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        }
    }

    public void loadItemsFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                String author = parts[1];
                int year = Integer.parseInt(parts[2]);

                Item item = new Item(title, author, year);
                items.add(item);
                tableModel.addRow(new Object[]{title, author, year});
            }
            reader.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading items from file " + e.getMessage());
        }
    }

    public void saveItemsToFile(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            for (Item item : items) {
                writer.write(item.getTitle() + "," + item.getAuthor() + "," + item.getYear());
                writer.newLine();
            }
            writer.close();
            JOptionPane.showMessageDialog(this, "Items are saved to file successfully");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving items to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryGUI();
            }
        });
    }
}
