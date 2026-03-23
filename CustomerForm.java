import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerForm extends JDialog {
    public CustomerForm(JFrame parent, boolean addMode) {
        super(parent, "Customer", true);
        setSize(400, 300);
        setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(20, 20, 80, 25);
        add(lblName);
        JTextField txtName = new JTextField();
        txtName.setBounds(110, 20, 250, 25);
        add(txtName);

        JLabel lblPhone = new JLabel("Phone:");
        lblPhone.setBounds(20, 60, 80, 25);
        add(lblPhone);
        JTextField txtPhone = new JTextField();
        txtPhone.setBounds(110, 60, 250, 25);
        add(txtPhone);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 100, 80, 25);
        add(lblEmail);
        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(110, 100, 250, 25);
        add(txtEmail);

        JLabel lblAdhaar = new JLabel("Adhaar:");
        lblAdhaar.setBounds(20, 140, 80, 25);
        add(lblAdhaar);
        JTextField txtAdhaar = new JTextField();
        txtAdhaar.setBounds(110, 140, 250, 25);
        add(txtAdhaar);

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(110, 200, 100, 30);
        add(btnSave);

        btnSave.addActionListener(e -> {
            try {
                Connection con = DBConnection.connect();
                HotelUtils.addCustomer(con, txtName.getText(), txtPhone.getText(), txtEmail.getText(), txtAdhaar.getText());
                JOptionPane.showMessageDialog(this, "Customer saved!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBounds(220, 200, 100, 30);
        add(btnCancel);
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }
}
