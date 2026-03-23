import java.sql.*;
import java.util.Scanner;

public class HotelManagement {

public static void main(String[] args){

try{

Connection con = DBConnection.connect();
Scanner sc = new Scanner(System.in);

while(true){

System.out.println("\n===== HOTEL MANAGEMENT SYSTEM =====");
System.out.println("1 Add Customer");
System.out.println("2 View Rooms");
System.out.println("3 Book Room");
System.out.println("4 Checkout Room");
System.out.println("5 Show Bookings");
System.out.println("6 Exit");

System.out.print("Enter Choice: ");
int choice = sc.nextInt();
sc.nextLine();


// ADD CUSTOMER
if(choice == 1){

System.out.print("Name: ");
String name = sc.nextLine();

System.out.print("Phone: ");
String phone = sc.nextLine();

System.out.print("Email: ");
String email = sc.nextLine();

System.out.print("Adhaar: ");
String adhaar = sc.nextLine();

String query="INSERT INTO customers(name,phone,email,adhaar) VALUES(?,?,?,?)";

PreparedStatement pst=con.prepareStatement(query);

pst.setString(1,name);
pst.setString(2,phone);
pst.setString(3,email);
pst.setString(4,adhaar);

pst.executeUpdate();

System.out.println("Customer Added!");

}


// VIEW ROOMS
else if(choice==2){

Statement st=con.createStatement();

ResultSet rs=st.executeQuery("SELECT * FROM rooms");

System.out.println("\nRoomID  Type  Price  Status");

while(rs.next()){

System.out.println(
rs.getInt("room_id")+" "+
rs.getString("room_type")+" "+
rs.getInt("price")+" "+
rs.getString("status")
);

}

}


// BOOK ROOM
else if(choice==3){

System.out.print("Customer ID: ");
int cid=sc.nextInt();

System.out.print("Room ID: ");
int room=sc.nextInt();

String book="INSERT INTO bookings(customer_id,room_id,check_in) VALUES(?,?,CURDATE())";

PreparedStatement pst=con.prepareStatement(book);

pst.setInt(1,cid);
pst.setInt(2,room);

pst.executeUpdate();

PreparedStatement pst2=con.prepareStatement(
"UPDATE rooms SET status='Booked' WHERE room_id=?"
);

pst2.setInt(1,room);
pst2.executeUpdate();

System.out.println("Room Booked!");

}


// CHECKOUT
else if(choice==4){

System.out.print("Room ID: ");
int room=sc.nextInt();

System.out.print("Days Stayed: ");
int days=sc.nextInt();

PreparedStatement pst=con.prepareStatement(
"SELECT price FROM rooms WHERE room_id=?"
);

pst.setInt(1,room);

ResultSet rs=pst.executeQuery();

int price=0;

if(rs.next())
price=rs.getInt("price");

int bill=price*days;

System.out.println("Total Bill = "+bill);

PreparedStatement pst2=con.prepareStatement(
"UPDATE bookings SET check_out=DATE_ADD(check_in,INTERVAL ? DAY) WHERE room_id=? AND check_out IS NULL"
);

pst2.setInt(1,days);
pst2.setInt(2,room);
pst2.executeUpdate();

PreparedStatement pst3=con.prepareStatement(
"UPDATE rooms SET status='Available' WHERE room_id=?"
);

pst3.setInt(1,room);
pst3.executeUpdate();

System.out.println("Checkout Done");

}


// SHOW BOOKINGS WITH CUSTOMER DETAILS
else if(choice==5){

Statement st=con.createStatement();

ResultSet rs=st.executeQuery(

"SELECT b.booking_id,c.customer_id,c.name,c.phone,c.email,c.adhaar,b.room_id,b.check_in,b.check_out "+
"FROM bookings b "+
"JOIN customers c ON b.customer_id=c.customer_id"

);

System.out.println("\nBookingID CustID Name Phone Email Adhaar RoomID CheckIn CheckOut");

while(rs.next()){

System.out.println(
rs.getInt("booking_id")+" "+
rs.getInt("customer_id")+" "+
rs.getString("name")+" "+
rs.getString("phone")+" "+
rs.getString("email")+" "+
rs.getString("adhaar")+" "+
rs.getInt("room_id")+" "+
rs.getDate("check_in")+" "+
rs.getDate("check_out")
);

}

}


// EXIT
else if(choice==6){

break;

}

}

}

catch(Exception e){

System.out.println(e);

}

}

}