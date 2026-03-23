import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {

JTextField usernameField;
JPasswordField passwordField;

public Login(){

setTitle("Hotel Management Login");
setSize(350,200);
setLayout(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

JLabel userLabel=new JLabel("Username:");
userLabel.setBounds(40,30,80,25);
add(userLabel);

usernameField=new JTextField();
usernameField.setBounds(120,30,150,25);
add(usernameField);

JLabel passLabel=new JLabel("Password:");
passLabel.setBounds(40,70,80,25);
add(passLabel);

passwordField=new JPasswordField();
passwordField.setBounds(120,70,150,25);
add(passwordField);

JButton loginButton=new JButton("Login");
loginButton.setBounds(120,110,100,30);
add(loginButton);

        loginButton.addActionListener(new ActionListener(){

public void actionPerformed(ActionEvent e){

String username=usernameField.getText();
String password=new String(passwordField.getPassword());

try{

Connection con=DBConnection.connect();

PreparedStatement pst=con.prepareStatement(
"SELECT role FROM users WHERE username=? AND password=?"
);

pst.setString(1,username);
pst.setString(2,password);

ResultSet rs=pst.executeQuery();

if(rs.next()){

String role = rs.getString("role");

JOptionPane.showMessageDialog(null,"Login Successful as " + role + "!");

if("admin".equals(role)) {
    new AdminDashboard();
} else {
    new StaffDashboard();
}
dispose();

} else {

JOptionPane.showMessageDialog(null,"Invalid Login");

}

} catch(Exception ex){

System.out.println(ex);

JOptionPane.showMessageDialog(null,"Database error: " + ex.getMessage());

}

}

});

setVisible(true);

}

public static void main(String[] args){

new Login();

}

}