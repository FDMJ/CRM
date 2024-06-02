package crm.gui;

import crm.database.DatabaseManager;
import crm.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CRMGui extends JFrame {
    private DatabaseManager databaseManager;
    private DefaultTableModel tableModel;

    public CRMGui(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        initializeGui();
    }

    private void initializeGui() {
        setTitle("CRM System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);

        panel.add(inputPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                Customer customer = new Customer(0, name, email, phone);
                databaseManager.addCustomer(customer);
                refreshTable();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
                    Customer customer = new Customer(id, name, email, phone);
                    databaseManager.updateCustomer(customer);
                    refreshTable();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    databaseManager.deleteCustomer(id);
                    refreshTable();
                }
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Customer> customers = databaseManager.getAllCustomers();
        for (Customer customer : customers) {
            tableModel.addRow(new Object[]{customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone()});
        }
    }

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager("jdbc:sqlite:crm.db");
        CRMGui gui = new CRMGui(databaseManager);
        gui.setVisible(true);
    }
}
