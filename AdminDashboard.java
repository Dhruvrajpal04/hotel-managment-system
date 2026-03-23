import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard - Hotel Management");
        setSize(600, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel title = new JLabel("Admin Panel - Full Access", JLabel.CENTER);
        title.setBounds(0, 20, 600, 30);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);

        // Full access buttons
        JButton btnCustomers = new JButton("Manage Customers");
        btnCustomers.setBounds(50, 80, 200, 40);
        add(btnCustomers);

        JButton btnRooms = new JButton("Manage Rooms");
        btnRooms.setBounds(350, 80, 200, 40);
        add(btnRooms);

        JButton btnViewBookings = new JButton("View Bookings");
        btnViewBookings.setBounds(50, 140, 200, 40);
        add(btnViewBookings);
        btnViewBookings.addActionListener(e -> new BookingViewer(this));

        JButton btnRoomsView = new JButton("View Rooms");
        btnRoomsView.setBounds(350, 140, 200, 40);
        add(btnRoomsView);
        btnRoomsView.addActionListener(e -> new RoomViewer(this));

        JButton btnUsers = new JButton("Manage Users");
        btnUsers.setBounds(50, 190, 200, 40);
        add(btnUsers);
        btnUsers.addActionListener(e -> JOptionPane.showMessageDialog(this, "Users management (basic login suffices)"));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(250, 400, 100, 40);
        add(btnLogout);
        btnLogout.addActionListener(e -> {
            new Login();
            dispose();
        });

        btnCustomers.addActionListener(e -> new CustomerForm(this, true));
        btnRooms.addActionListener(e -> JOptionPane.showMessageDialog(this, "Full rooms manage (TODO advanced)"));

        setVisible(true);
    }
}
