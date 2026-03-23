import java.sql.*;

public class HotelUtils {

    public static void addCustomer(Connection con, String name, String phone, String email, String adhaar) throws SQLException {
        String query = "INSERT INTO customers(name,phone,email,adhaar) VALUES(?,?,?,?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, phone);
            pst.setString(3, email);
            pst.setString(4, adhaar);
            pst.executeUpdate();
        }
    }

    public static ResultSet viewRooms(Connection con) throws SQLException {
        Statement st = con.createStatement();
        return st.executeQuery("SELECT * FROM rooms");
    }

    public static void bookRoom(Connection con, int cid, int room) throws SQLException {
        String book = "INSERT INTO bookings(customer_id,room_id,check_in) VALUES(?,?,CURDATE())";
        try (PreparedStatement pst = con.prepareStatement(book)) {
            pst.setInt(1, cid);
            pst.setInt(2, room);
            pst.executeUpdate();
        }
        try (PreparedStatement pst2 = con.prepareStatement("UPDATE rooms SET status='Booked' WHERE room_id=?")) {
            pst2.setInt(1, room);
            pst2.executeUpdate();
        }
    }

    public static double checkoutRoom(Connection con, int room, String checkoutDate) throws SQLException {
        // Auto bill using DATEDIFF
        String sql = "SELECT r.price, DATEDIFF(?, b.check_in) as days FROM rooms r JOIN bookings b ON r.room_id = b.room_id WHERE b.room_id = ? AND b.check_out IS NULL";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, checkoutDate);
            pst.setInt(2, room);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int price = rs.getInt("price");
                int days = rs.getInt("days");
                double bill = price * days;
                // Update checkout
                try (PreparedStatement pst2 = con.prepareStatement("UPDATE bookings SET check_out = ? WHERE room_id = ? AND check_out IS NULL")) {
                    pst2.setString(1, checkoutDate);
                    pst2.setInt(2, room);
                    pst2.executeUpdate();
                }
                // Update room status
                try (PreparedStatement pst3 = con.prepareStatement("UPDATE rooms SET status='Available' WHERE room_id=?")) {
                    pst3.setInt(1, room);
                    pst3.executeUpdate();
                }
                return bill;
            }
        }
        return 0;
    }

    public static ResultSet showBookings(Connection con) throws SQLException {
        Statement st = con.createStatement();
        return st.executeQuery("SELECT b.booking_id,c.customer_id,c.name,c.phone,c.email,c.adhaar,b.room_id,b.check_in,b.check_out FROM bookings b JOIN customers c ON b.customer_id=c.customer_id");
    }
}
