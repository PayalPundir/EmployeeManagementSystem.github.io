package Employee.Management.System;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewEmployees extends JFrame implements ActionListener {

    JTable table;
    Choice cemployeeId;
    JButton search, print, update, back;
    
    ViewEmployees() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);
        
        JLabel searchlbl = new JLabel("Search by Employee Id");
        searchlbl.setBounds(20, 20, 150, 20);
        add(searchlbl);
        
        cemployeeId = new Choice();
        cemployeeId.setBounds(180, 20, 150, 20);
        add(cemployeeId);
        
        try {
            Conn c = new Conn();
            ResultSet rs = c.s.executeQuery("select empId from employee");
            while (rs.next()) {
                cemployeeId.add(rs.getString("empId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);
        
        // Adding table to JScrollPane
        JScrollPane jsp = new JScrollPane(table);
        jsp.setBounds(0, 100, 900, 600);
        add(jsp);
        
        search = new JButton("Search");
        search.setBounds(20, 70, 80, 20);
        search.addActionListener(this);
        add(search);
        
        print = new JButton("Print");
        print.setBounds(120, 70, 80, 20);
        print.addActionListener(this);
        add(print);
        
        update = new JButton("Update");
        update.setBounds(220, 70, 80, 20);
        update.addActionListener(this);
        add(update);
        
        back = new JButton("Back");
        back.setBounds(320, 70, 80, 20);
        back.addActionListener(this);
        add(back);
        
        setSize(900, 700);
        setLocation(300, 100);
        setVisible(true);
        
        loadAllEmployees();
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == search) {
            loadEmployeesById(cemployeeId.getSelectedItem());
        } else if (ae.getSource() == print) {
            try {
                table.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (ae.getSource() == update) {
            setVisible(false);
            // Uncomment when UpdateEmployee class is implemented
             new UpdateEmployee(cemployeeId.getSelectedItem());
        } else {
            setVisible(false);
            // Uncomment when Home class is implemented
             new Home();
        }
    }
    
    private void loadAllEmployees() {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee";
            ResultSet rs = c.s.executeQuery(query);
            populateTable(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadEmployeesById(String empId) {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM employee WHERE empId = '" + empId + "'";
            ResultSet rs = c.s.executeQuery(query);
            populateTable(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void populateTable(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            
            // Setting table model
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            model.setColumnCount(0);
            
            // Adding column names
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(rsmd.getColumnName(i));
            }
            
            // Adding rows
            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = rs.getObject(i);
                }
                model.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ViewEmployees();
    }
}
