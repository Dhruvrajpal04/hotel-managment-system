import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StaffDashboard extends JFrame {
    public StaffDashboard() {
        setTitle("Staff Dashboard - Hotel Management");
        setSize(600, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Staff Panel - Limited Access", JLabel.CENTER);
        title.setBounds(0, 20, 600, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);

        // Staff buttons (no users/rooms manage)
        JButton btnCustomers = new JButton("View/Add Customers");
        btnCustomers.setBounds(50, 80, 200, 40);
        add(btnCustomers);

        JButton btnViewBookings = new JButton("View Bookings");
        btnViewBookings.setBounds(350, 80, 200, 40);
        add(btnViewBookings);

        btnViewBookings.addActionListener(e -> new BookingViewer(this));

        JButton btnCheckin = new JButton("Check-in");
        btnCheckin.setBounds(50, 140, 200, 40);
        add(btnCheckin);

        JButton btnCheckout = new JButton("Check-out & Bill");
        btnCheckout.setBounds(350, 140, 200, 40);
        add(btnCheckout);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(250, 400, 100, 40);
        add(btnLogout);

        btnCustomers.addActionListener(e -> new CustomerForm(this, true));
        btnViewBookings.addActionListener(e -> new BookingViewer(this));
        btnCheckin.addActionListener(e -> new BookingForm(this, true));
        btnCheckout.addActionListener(e -> new BookingForm(this, false));
        btnLogout.addActionListener(e -> {
            new Login();
            dispose();
        });

        setVisible(true);
    }
}
