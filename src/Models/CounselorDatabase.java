package Models;

import Main.DatabaseInfo;
import Main.Logs;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CounselorDatabase {

    private static Counselor currentCounselor;

    public static Counselor getCurrentCounselor() {
        return currentCounselor;
    }

    public static boolean checkCredentials(String username, String password, int pin) {
        try {
            Statement statement = DatabaseInfo.getConn().createStatement();
            String check = "Select * From counselor WHERE c_name= '" + username + "'AND c_password= '" + password + "' AND c_pin= " + pin ;
            ResultSet rs = statement.executeQuery(check);
            if (rs.next()) {
                currentCounselor = new Counselor();
                currentCounselor.setUsername(rs.getString("c_name"));
                statement.close();
                Logs.logSuccess(username);
                return true;
            } else {
                Logs.logFailure(username);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
            return false;
        }
    }
}