import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class BookingViewer extends JDialog {
    public BookingViewer(JFrame parent) {
        super(parent, "Bookings", true);
        setSize(800, 500);
        setLayout(new BorderLayout());

        try {
            Connection con = DBConnection.connect();
            ResultSet rs = HotelUtils.showBookings(con);
            StringBuilder sb = new StringBuilder("BookingID CustID Name Phone Email Adhaar RoomID CheckIn CheckOut\n");
            while (rs.next()) {
                sb.append(rs.getInt("booking_id")).append(" ")
                  .append(rs.getInt("customer_id")).append(" ")
                  .append(rs.getString("name")).append(" ")
                  .append(rs.getString("phone")).append(" ")
                  .append(rs.getString("email")).append(" ")
                  .append(rs.getString("adhaar")).append(" ")
                  .append(rs.getInt("room_id")).append(" ")
                  .append(rs.getDate("check_in")).append(" ")
                  .append(rs.getDate("check_out")).append("\n");
            }
            JTextArea ta = new JTextArea(sb.toString());
            ta.setEditable(false);
            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            add(new JScrollPane(ta), BorderLayout.CENTER);
        } catch (Exception ex) {
            add(new JLabel("Error loading bookings: " + ex.getMessage()));
        }

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose, BorderLayout.SOUTH);

        setVisible(true);
    }
}
