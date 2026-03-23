import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RoomViewer extends JDialog {
    public RoomViewer(JFrame parent) {
        super(parent, "Rooms", true);
        setSize(500, 400);
        setLayout(new BorderLayout());

        try {
            Connection con = DBConnection.connect();
            ResultSet rs = HotelUtils.viewRooms(con);
            StringBuilder sb = new StringBuilder("RoomID\tType\tPrice\tStatus\n");
            while (rs.next()) {
                sb.append(rs.getInt("room_id")).append("\t")
                  .append(rs.getString("room_type")).append("\t")
                  .append(rs.getInt("price")).append("\t")
                  .append(rs.getString("status")).append("\n");
            }
            JTextArea ta = new JTextArea(sb.toString());
            ta.setEditable(false);
            ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
            add(new JScrollPane(ta), BorderLayout.CENTER);
        } catch (Exception ex) {
            add(new JLabel("Error loading rooms: " + ex.getMessage()));
        }

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose, BorderLayout.SOUTH);

        setVisible(true);
    }
}
