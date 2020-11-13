package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseInfo {
    private static final String databaseName = "U07uXB";
    private static final String URL = "jdbc:mysql://3.227.166.251/U07uXB";
    private static final String userName = "U07uXB";
    private static final String passWord = "53689138700";
    private static final String Driver = "com.mysql.jdbc.Driver";
    private static Connection conn;

    static Connection startConnection(){
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(URL, userName, passWord);
            System.out.println("Connection Successful");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() throws SQLException {
        try {
            conn.close();
            System.out.println("Connection closed");
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConn(){
        return conn;
    }
}
