package Models;

import Main.DatabaseInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PatientDatabase {

    private static ObservableList<Patient> patientList = FXCollections.observableArrayList();

    public static ObservableList<Patient> showAllPatients() {
        // clears out the observable list to rebuild every time the showAllPatients method is called.
        patientList.clear();
        try {
            Statement statement = DatabaseInfo.getConn().createStatement();
            //SQL to pull all patient records that have a values that are linked by address_id.
            String patients = "select * from address, patient Where address.address_id = patient.address_id";
            ResultSet rs = statement.executeQuery(patients);
            while(rs.next()){
                Patient patient = new Patient(
                        rs.getInt("pt_id"),
                        rs.getString("pt_name"),
                        rs.getInt("address_id"),
                        rs.getString("addressline_1"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("postal_code"),
                        rs.getString("phone"),
                        rs.getString("INS_PR"));
                patientList.add(patient);
            }
            statement.close();
            return patientList;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public static boolean addPatient(String name, String address, String city, String state, String postal, String phone, String insurance){
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            //searches the database for the highest value address_id.
            String idQuery = "Select address_id from address order by address_id desc";
            ResultSet rs = statement.executeQuery(idQuery);
            rs.next();
            //sets returned address id to maxID and increases the value by 1 for the new record.
            int maxID = rs.getInt("address_id") + 1;
            String addressTable = "Insert into address set address_id= '" + maxID + "', addressline_1= '" + address + "', city= '" + city + "', state= '" + state + "', postal_code= '" + postal + "', phone= '" + phone +"'";
            int addressInfo = statement.executeUpdate(addressTable);
            System.out.println(addressTable);
            //if inserting into the address table is successful than the follow statement will run to add patients info to patients table.
            if (addressInfo == 1){
                String patientTable = "Insert into patient set pt_id= '" + maxID + "', pt_name= '" + name + "', address_id= '" + maxID + "', INS_PR= '" + insurance + "', address_address_id= '" +maxID+ "'";
                int patientInfo = statement.executeUpdate(patientTable);
                System.out.println(patientTable);
                if( patientInfo == 1);
                return true;
            }
        }
        catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    public static boolean deletePatientRecord(int ID){
        //deletes patient record from the database starting with the patient table and then moving to the address table both associated on the address_id.
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String deletePatient = "Delete from patient where address_id=" + ID;
            int deletePatientRecord = statement.executeUpdate(deletePatient);
            if (deletePatientRecord == 1){
                String deleteAddress = "Delete from address where address_id=" + ID;
                int deleteAddRecord = statement.executeUpdate(deleteAddress);
                if (deleteAddRecord == 1){
                    return true;
                }
            }
    }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    public static boolean modifyPatientRecord(String name, String address, String city, String state, String postal, String phone, String insurance, int ID){
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String addressTable = "Update address set addressline_1= '" + address + "', city= '" + city + "', state= '" + state + "', postal_code= '" + postal + "', phone= '" + phone + "' where address_id= '" + ID + "'" ;
            int addressInfo = statement.executeUpdate(addressTable);
            System.out.println(addressTable);
            //if the update to the address table is successful than the following statement will run to update the patients info in the patients table.
            if (addressInfo == 1){
                String patientTable = "Update patient set pt_name= '" + name + "', INS_PR= '" + insurance + "' where address_id= '" + ID + "'";
                int patientInfo = statement.executeUpdate(patientTable);
                System.out.println(patientTable);
                if( patientInfo == 1);
                return true;
            }
        }
        catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }
}
