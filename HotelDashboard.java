import javax.swing.*;

public class HotelDashboard extends JFrame {

public HotelDashboard(){

setTitle("Hotel Management Dashboard");
setSize(400,300);
setLayout(null);

JLabel label=new JLabel("Welcome to Hotel Management System");
label.setBounds(70,50,300,30);
add(label);

JButton customers=new JButton("Customers");
customers.setBounds(130,100,120,30);
add(customers);

JButton rooms=new JButton("Rooms");
rooms.setBounds(130,140,120,30);
add(rooms);

JButton bookings=new JButton("Bookings");
bookings.setBounds(130,180,120,30);
add(bookings);

setVisible(true);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

}

}