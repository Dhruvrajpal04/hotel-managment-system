import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class HotelManagementGUI extends JFrame {
    Connection con;

    public HotelManagementGUI() {
        setTitle("Hotel Management System - GUI Console");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        con = DBConnection.connect();

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnAddCustomer = new JButton("1. Add Customer");
        JButton btnViewRooms = new JButton("2. View Rooms");
        JButton btnBookRoom = new JButton("3. Book Room");
        JButton btnCheckout = new JButton("4. Checkout Room");
        JButton btnShowBookings = new JButton("5. Show Bookings");
        JButton btnExit = new JButton("6. Exit");

        menuPanel.add(btnAddCustomer);
        menuPanel.add(btnViewRooms);
        menuPanel.add(btnBookRoom);
        menuPanel.add(btnCheckout);
        menuPanel.add(btnShowBookings);
        menuPanel.add(btnExit);

        add(menuPanel, BorderLayout.WEST);

        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Add Customer
        btnAddCustomer.addActionListener(e -> {
            String result = showInputDialog("Add Customer", "Name:", "Phone:", "Email:", "Adhaar:");
            if (result != null) {
                String[] data = result.split(",");
                try {
                    HotelUtils.addCustomer(con, data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim());
                    outputArea.append("Customer Added!\n");
                } catch (Exception ex) {
                    outputArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        // View Rooms
        btnViewRooms.addActionListener(e -> {
            try {
                ResultSet rs = HotelUtils.viewRooms(con);
                outputArea.append("\n=== ROOMS ===\nRoomID Type Price Status\n");
                while (rs.next()) {
                    outputArea.append(rs.getInt("room_id") + " " + rs.getString("room_type") + " " +
                                    rs.getInt("price") + " " + rs.getString("status") + "\n");
                }
                outputArea.append("\n");
            } catch (Exception ex) {
                outputArea.append("Error: " + ex.getMessage() + "\n");
            }
        });

        // Book Room
        btnBookRoom.addActionListener(e -> {
            String cid = JOptionPane.showInputDialog("Customer ID:");
            String room = JOptionPane.showInputDialog("Room ID:");
            if (cid != null && room != null) {
                try {
                    HotelUtils.bookRoom(con, Integer.parseInt(cid), Integer.parseInt(room));
                    outputArea.append("Room Booked!\n");
                } catch (Exception ex) {
                    outputArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        // Checkout
        btnCheckout.addActionListener(e -> {
            String room = JOptionPane.showInputDialog("Room ID:");
            String date = JOptionPane.showInputDialog("Checkout Date (YYYY-MM-DD):");
            if (room != null && date != null) {
                try {
                    double bill = HotelUtils.checkoutRoom(con, Integer.parseInt(room), date);
                    outputArea.append("Checkout Done. Total Bill = $" + bill + "\n");
                } catch (Exception ex) {
                    outputArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        // Show Bookings
        btnShowBookings.addActionListener(e -> {
            try {
                ResultSet rs = HotelUtils.showBookings(con);
                outputArea.append("\n=== BOOKINGS ===\nBookingID CustID Name Phone Email Adhaar RoomID CheckIn CheckOut\n");
                while (rs.next()) {
                    outputArea.append(rs.getInt("booking_id") + " " + rs.getInt("customer_id") + " " +
                                    rs.getString("name") + " " + rs.getString("phone") + " " +
                                    rs.getString("email") + " " + rs.getString("adhaar") + " " +
                                    rs.getInt("room_id") + " " + rs.getDate("check_in") + " " +
                                    rs.getDate("check_out") + "\n");
                }
                outputArea.append("\n");
            } catch (Exception ex) {
                outputArea.append("Error: " + ex.getMessage() + "\n");
            }
        });

        btnExit.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private String showInputDialog(String title, String... labels) {
        JTextField[] fields = new JTextField[labels.length];
        JPanel panel = new JPanel(new GridLayout(labels.length, 2));
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            fields[i] = new JTextField();
            panel.add(fields[i]);
        }
        int result = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            StringBuilder sb = new StringBuilder();
            for (JTextField field : fields) {
                sb.append(field.getText()).append(",");
            }
            return sb.toString();
        }
        return null;
    }

    public static void main(String[] args) {
        new HotelManagementGUI();
    }
}
