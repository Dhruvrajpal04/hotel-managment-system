import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connect() {

        try {

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hotel_management",
                "root",
                "Dhruv@4567"
            );

            System.out.println("Database Connected Successfully");

            return con;

        } catch (Exception e) {

            System.out.println(e);
            return null;

        }
    }
}