package Models;

import Main.DatabaseInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class AppointmentDatabase {

    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthAppointment = FXCollections.observableArrayList();
    private static ObservableList<Appointment> biweekAppointment = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weekAppointment = FXCollections.observableArrayList();
    private static ObservableList<Appointment> aptNext4HR = FXCollections.observableArrayList();

    public static ObservableList<Appointment> showAllAppointments(){
        //clears out the observable list allAppointments so that list can be refreshed with new data from the database
        allAppointments.clear();
        LocalDate today = LocalDate.now();
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String appointments = "Select * from appointment where start_datetime >= '" + today + "'order by start_datetime asc;";
            ResultSet rs = statement.executeQuery(appointments);
            while(rs.next()){
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime aptstart = rs.getTimestamp("start_datetime").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                String startTime = aptstart.format(formatTime);
                Appointment appointment = new Appointment(
                    rs.getInt("apt_id"),
                    rs.getInt("pt_id"),
                    rs.getInt("cr_id"),
                    rs.getInt("apt_type_id"),
                    startTime,
                    rs.getString("notes"));
                allAppointments.add(appointment);
            }
            statement.close();
            return allAppointments;
        }
        catch(SQLException e){
            System.out.println("SQLException e: " + e.getMessage());
            return null;
        }
    }

    public static ObservableList<Appointment> showMonthAppointments(){
        //clears out the observable list monthAppointment so that list can be refreshed with new data from the database
        monthAppointment.clear();
        //sets the time frame for when to look for appointments
        LocalDate today = LocalDate.now();
        LocalDate plus30 = LocalDate.now().plusDays(30);
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String appointments = "Select * from appointment where start_datetime >= '" + today + "' AND start_dateTime <= '" + plus30 + "'order by start_datetime asc";
            ResultSet rs = statement.executeQuery(appointments);
            while(rs.next()){
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime aptstart = rs.getTimestamp("start_datetime").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                String startTime = aptstart.format(formatTime);
                Appointment appointment = new Appointment(
                        rs.getInt("apt_id"),
                        rs.getInt("pt_id"),
                        rs.getInt("cr_id"),
                        rs.getInt("apt_type_id"),
                        startTime,
                        rs.getString("notes"));
                monthAppointment.add(appointment);
            }
            statement.close();
            return monthAppointment;
        }
        catch(SQLException e){
            System.out.println("SQLException e: " + e.getMessage());
            return null;
        }
    }

    public static ObservableList<Appointment> showBiweekAppointments(){
        //clears out the observable list biweekAppointment so that list can be refreshed with new data from the database
        biweekAppointment.clear();
        //sets the time frame for when to look for appointments
        LocalDate today = LocalDate.now();
        LocalDate plus14 = LocalDate.now().plusDays(14);
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String appointments = "Select * from appointment where start_datetime >= '" + today + "' AND start_dateTime <= '" + plus14 + "'order by start_datetime asc";
            ResultSet rs = statement.executeQuery(appointments);
            while(rs.next()){
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime aptstart = rs.getTimestamp("start_datetime").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                String startTime = aptstart.format(formatTime);
                Appointment appointment = new Appointment(
                        rs.getInt("apt_id"),
                        rs.getInt("pt_id"),
                        rs.getInt("cr_id"),
                        rs.getInt("apt_type_id"),
                        startTime,
                        rs.getString("notes"));
                biweekAppointment.add(appointment);
            }
            statement.close();
            return biweekAppointment;
        }
        catch(SQLException e){
            System.out.println("SQLException e: " + e.getMessage());
            return null;
        }
    }

    public static ObservableList<Appointment> showWeekAppointments(){
        //clears out the observable list monthAppointment so that list can be refreshed with new data from the database
        weekAppointment.clear();
        //sets the time frame for when to look for appointments
        LocalDate today = LocalDate.now();
        LocalDate plus7 = LocalDate.now().plusDays(7);
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String appointments = "Select * from appointment where start_datetime >= '" + today + "' AND start_dateTime <= '" + plus7 + "'order by start_datetime asc";
            ResultSet rs = statement.executeQuery(appointments);
            while(rs.next()){
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime aptstart = rs.getTimestamp("start_datetime").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                String startTime = aptstart.format(formatTime);
                Appointment appointment = new Appointment(
                        rs.getInt("apt_id"),
                        rs.getInt("pt_id"),
                        rs.getInt("cr_id"),
                        rs.getInt("apt_type_id"),
                        startTime,
                        rs.getString("notes"));
                weekAppointment.add(appointment);
            }
            statement.close();
            return weekAppointment;
        }
        catch(SQLException e){
            System.out.println("SQLException e: " + e.getMessage());
            return null;
        }
    }

    public static boolean newAppointment(int ptID, int aptTypeID, String Time, int counselID, String notes){
      try{
          Statement statement = DatabaseInfo.getConn().createStatement();
          String aptIDquery = "Select apt_id from appointment order by apt_id desc";
          ResultSet rs = statement.executeQuery(aptIDquery);
          rs.next();
          int maxAptID = rs.getInt("apt_id") +1;
          String appointmentTable = "Insert into appointment set apt_id= '" + maxAptID + "',pt_id= '" + ptID + "',cr_id= '" + counselID +"',apt_type_id= '" + aptTypeID + "',notes= '" + notes + "',start_datetime= '" + Time + "' ,patient_pt_id= '" + ptID + "' ,counselor_c_id= '" + counselID +"', APTtype_APTtype_id= '" + aptTypeID + "'";
          int appointmentInfo = statement.executeUpdate(appointmentTable);
          if(appointmentInfo == 1){
              return true;
          }
      }
      catch (SQLException e){
          System.out.println("SQLException: " + e.getMessage());
      }
      return false;
    }

    public static boolean deleteAppointmentRecord(int aptID){
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String deleteAppointment = "Delete from appointment where apt_id=" + aptID;
            int deleteAppointmentRecord = statement.executeUpdate(deleteAppointment);
            if (deleteAppointmentRecord == 1){
                return true;
            }

        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    public static boolean modifyAppointmentRecord(int ptID, int aptTypeID, String Time, int counselID, String notes, int aptID){
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String appointmentTable = "Update appointment set pt_id= '" + ptID + "',cr_id= '" + counselID +"',apt_type_id= '" + aptTypeID + "',notes= '" + notes + "',start_datetime= '" + Time + "' ,patient_pt_id= '" + ptID + "' ,counselor_c_id= '" + counselID +"', APTtype_APTtype_id= '" + aptTypeID + "' Where apt_id= '" + aptID + "'";
            int appointmentUpdate = statement.executeUpdate(appointmentTable);
            if(appointmentUpdate==1){
                return true;
            }
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    public static boolean checkPatientOverlap(int ptID, String time){
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String checkPatient = "Select * from appointment where pt_id = '" + ptID + "'and start_datetime= '" + time +"'";
            ResultSet rs = statement.executeQuery(checkPatient);
            if(rs.next()){
                return true;
            }
        }
        catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
        }
        return false;
    }

    public static boolean checkCounselorOverlap(int cID, String time){
        try {
            Statement statement = DatabaseInfo.getConn().createStatement();
            String checkCounselor = "Select * from appointment where cr_id= '" + cID + "' and start_datetime= '" + time + "'";
            ResultSet rs = statement.executeQuery(checkCounselor);
            if (rs.next()) {
                return true;
            }
        }
            catch(SQLException e){
                System.out.println("SQLException: " + e.getMessage());
            }
            return false;
        }


    public static ObservableList<Appointment> next4HourAppointments(){
        try{
            Statement statement = DatabaseInfo.getConn().createStatement();
            String checkApt4hours = "select * from appointment where  start_datetime > now() and start_datetime <= date_add(now(),interval 4 hour)";
            ResultSet rs = statement.executeQuery(checkApt4hours);
            System.out.println(checkApt4hours);
                while(rs.next()){
                    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime aptstart = rs.getTimestamp("start_datetime").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                    String startTime = aptstart.format(formatTime);
                    Appointment appointment = new Appointment(
                            rs.getInt("apt_id"),
                            rs.getInt("pt_id"),
                            rs.getInt("cr_id"),
                            rs.getInt("apt_type_id"),
                            startTime,
                            rs.getString("notes"));
                    aptNext4HR.add(appointment);

                }
                statement.close();
                return aptNext4HR;
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

}
