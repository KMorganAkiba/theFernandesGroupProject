package Models;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Appointment {

    private int appointmentID;
    private int patientID;
    private int counselorID;
    private int aptDescriptionID;
    private String aptDateTime;
    private String notes;

    //Appointment Constructor
    public Appointment(int appointmentID, int patientID, int counselorID, int aptDescriptionID, String aptDateTime, String notes){
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.counselorID = counselorID;
        this.aptDescriptionID = aptDescriptionID;
        this.aptDateTime = aptDateTime;
        this.notes = notes;
    }


    //Appointment Getters

    public int getAppointmentID() {
        return appointmentID;
    }

    public int getPatientID() {
        return patientID;
    }

    public int getCounselorID() {
        return counselorID;
    }

    public int getAptDescriptionID() {
        return aptDescriptionID;
    }

    public String getAptDateTime() {
        return aptDateTime;
    }

    public String getNotes() {
        return notes;
    }

    //Appointment Setters

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setCounselorID(int counselorID) {
        this.counselorID = counselorID;
    }

    public void setAptDescriptionID(int aptDescriptionID) {
        this.aptDescriptionID = aptDescriptionID;
    }

    public void setAptDateTime(String aptDateTime) {
        this.aptDateTime = aptDateTime;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
