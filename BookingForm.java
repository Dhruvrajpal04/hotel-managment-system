import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BookingForm extends JDialog {
    public BookingForm(JFrame parent, boolean checkinMode) {
        super(parent, checkinMode ? "Check-in" : "Check-out", true);
        setSize(400, 250);
        setLayout(null);

        JLabel lblCid = new JLabel("Customer ID:");
        lblCid.setBounds(20, 20, 100, 25);
        add(lblCid);
        JTextField txtCid = new JTextField();
        txtCid.setBounds(130, 20, 200, 25);
        add(txtCid);

        JLabel lblRoom = new JLabel("Room ID:");
        lblRoom.setBounds(20, 60, 100, 25);
        add(lblRoom);
        JTextField txtRoom = new JTextField();
        txtRoom.setBounds(130, 60, 200, 25);
        add(txtRoom);

        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):");
        lblDate.setBounds(20, 100, 100, 25);
        add(lblDate);
        JTextField txtDate = new JTextField();
        txtDate.setBounds(130, 100, 200, 25);
        add(txtDate);

        JButton btnProcess = new JButton(checkinMode ? "Book" : "Checkout");
        btnProcess.setBounds(130, 150, 200, 30);
        add(btnProcess);

        btnProcess.addActionListener(e -> {
            try {
                Connection con = DBConnection.connect();
                if (checkinMode) {
                    HotelUtils.bookRoom(con, Integer.parseInt(txtCid.getText()), Integer.parseInt(txtRoom.getText()));
                    JOptionPane.showMessageDialog(this, "Room booked!");
                } else {
                    double bill = HotelUtils.checkoutRoom(con, Integer.parseInt(txtRoom.getText()), txtDate.getText());
                    JOptionPane.showMessageDialog(this, "Checkout done. Bill: $" + bill);
                }
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}
