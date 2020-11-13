package Models;

public class Patient {

    private int patientID;
    private String patientName;
    private int addressID;
    private String patientAddress;
    private String patientCity;
    private String patientState;
    private String patientZipCode;
    private String patientPhone;
    private String patientInsurance;

    //Patient Constructor
    public Patient(int patientID,String patientName,int addressID, String patientAddress, String patientCity, String patientState, String patientZipCode, String patientPhone,String patientInsurance){
        this.patientID = patientID;
        this.patientName = patientName;
        this.addressID = addressID;
        this.patientAddress = patientAddress;
        this.patientCity = patientCity;
        this.patientState = patientState;
        this.patientZipCode = patientZipCode;
        this.patientPhone = patientPhone;
        this.patientInsurance = patientInsurance;


    }

    //Patient Getters
    public int getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public int getAddressID() {
        return addressID;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public String getPatientCity() {
        return patientCity;
    }

    public String getPatientState() {
        return patientState;
    }

    public String getPatientZipCode() {
        return patientZipCode;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getPatientInsurance() {
        return patientInsurance;
    }

    //Patient Setters

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public void setPatientCity(String patientCity) {
        this.patientCity = patientCity;
    }

    public void setPatientState(String patientState) {
        this.patientState = patientState;
    }

    public void setPatientZipCode(String patientZipCode) {
        this.patientZipCode = patientZipCode;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public void setPatientInsurance(String patientInsurance) {
        this.patientInsurance = patientInsurance;
    }
}
