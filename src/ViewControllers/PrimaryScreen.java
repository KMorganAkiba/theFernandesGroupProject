package ViewControllers;

import Main.DatabaseInfo;
import Models.Appointment;
import Models.AppointmentDatabase;
import Models.Patient;
import Models.PatientDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PrimaryScreen implements Initializable {
    public TextArea reportsTextArea;
    public ComboBox appointmentCounselorComboBox;
    public DatePicker appointmentDatePicker;
    public ComboBox appointmentTimeCombobox;
    public ComboBox appointmentTypeComboBox;
    public TextField patientIdAppointmnetTxtbox;
    public RadioButton appointmentModifyRadiobutton;
    public Button appointmentSaveModifyButton;
    public TableColumn <Patient,String> patientInsuranceColumn;
    public TableColumn <Patient,String> patientStateColumn;
    public TableColumn <Patient,String> patientPhoneColumn;
    public TableColumn <Patient,String> patientNameColumn;
    public TableColumn <Patient,Integer> patientIDColumn;
    public TableView <Patient> patientTable;
    public TextField patientInsuranceTxtbox;
    public TextField patientPhoneTextbox;
    public TextField patientZipCodeTxtbox;
    public ComboBox patientStateComboBox;
    public TextField patientCityTxtbox;
    public TextField patientAddressTxtbox;
    public TextField patientNameTxtbox;
    public RadioButton modifyPatientRadioButton;
    public TextField addressIDTextbox;
    public TextArea appointmentNotesTextArea;
    public TextField appointmentIDTextBox;
    public TableView <Appointment> appointmentTable;
    public TableColumn <Appointment, Integer> aptIDColumn;
    public TableColumn <Appointment, Integer> aptpatientIDColumn;
    public TableColumn <Appointment, Integer> counserlorIDColumn;
    public TableColumn <Appointment, String> appointmentTimeColumn;
    public TableColumn <Appointment, String> notesColumn;
    private Patient selectedPatient;
    private Appointment selectedAppointment;
    public static Patient modifyPatient = null;
    public static Patient newAppointmentPatientInfo = null;
    public static Appointment modifyAppointment = null;

    //Observable Array List for add/Modify Patient anchor pane. This is used to fill the state combo box with appropriate data.
    private ObservableList <String> states = FXCollections.observableArrayList("Alabama","Alaska","Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana",
            "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin",
            "Wyoming"
    );

    // Observable Array List for the Add/Modify appointment Anchor pane, starting with appointment types, then appointment times, and finally counselor names. This is used to fill the combo boxes with appropriate data.
    private ObservableList <String> appointmentsDescriptions = FXCollections.observableArrayList("Initial Consultation", "Diagnostic Evaluation", "Standard Appointment", "Referrals");

        // All appointments are 1 hour long, with the last available appointment at 8pm do to the office hours are 0900 - 2100.
    private ObservableList <String> appointmentTimesDuringOfficeHours = FXCollections.observableArrayList("9:00 ","10:00 ","11:00 ","12:00 ", "13:00 ", "14:00 ", "15:00 ", "16:00 ", "17:00 ", "18:00 ", "19:00 ", "20:00 " );

            //Obserable Array list for Counselors along with the language(s) they support.
    private ObservableList <String> counselorList = FXCollections.observableArrayList("Albert Pike (EN,DE)", "Juan De la Cierva (EN,ES)",  "Albert Anastasia (EN,ES,DE)");


    //Patients anchor pane controls
    public void modifyPatientButton(MouseEvent mouseEvent) {
        //verifies that a patient is selected and if selected assigns patient info to a variable modifyPatient to store patient info then cast to the patient form.
        Patient select = patientTable.getSelectionModel().getSelectedItem();
        if (select == null)
            return;
        modifyPatient = select;
        setTextFieldsToModifyPatient(modifyPatient);

    }

    public void deletePatientButton(MouseEvent mouseEvent) {
        //validates a patient is selected from the list of patients, if no patient is selected then pressing the delete button will not call the delete method.
        if(patientTable.getSelectionModel().getSelectedItem()!= null){
            selectedPatient = patientTable.getSelectionModel().getSelectedItem();
        }
        else{
            return;
        }
        //posts alert to get user input to validate that the user wants to delete the selected customer
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("You are about to delete " + selectedPatient.getPatientName() + " !");
        Optional <ButtonType> result = alert.showAndWait();
            //if customer selects the Button confirming they wish to delete the patient record, then deletePatientRecord will run.
            if(result.get() == ButtonType.OK){
                PatientDatabase.deletePatientRecord(selectedPatient.getAddressID());
                setPatientTable();
            }
            else{
                return;
            }
    }

    public void createNewAppointmentButton(MouseEvent mouseEvent) {
        //verifies that a patient is selected and if selected assigns patient info to a variable to create an appointment and casts that info to the Add/Modify appointment anchor pane.
        Patient select = patientTable.getSelectionModel().getSelectedItem();
        if (select == null)
            return;
        newAppointmentPatientInfo = select;
        appointmentPatientID(newAppointmentPatientInfo);
    }

        //Add/Modify Patient Controls
        public void patientSaveModifyButton(MouseEvent mouseEvent) throws IOException{
            String patientName = patientNameTxtbox.getText();
            String patientAddress = patientAddressTxtbox.getText();
            String patientCity = patientCityTxtbox.getText();
            String patientState = patientStateComboBox.getSelectionModel().getSelectedItem().toString();
            String patientZip = patientZipCodeTxtbox.getText();
            String patientPhone = patientPhoneTextbox.getText();
            String patientInsurance = patientInsuranceTxtbox.getText();
            String id = addressIDTextbox.getText();
            int ID = Integer.parseInt(id);

            //determines which method will run based on if the radio button is selected to tell the program to either update the database or insert into the database after which will refresh the patient table and then clear out the text fields for the next entry.
            if(modifyPatientRadioButton.isSelected()){
                PatientDatabase.modifyPatientRecord(patientName, patientAddress, patientCity, patientState, patientZip, patientPhone, patientInsurance, ID);
                setPatientTable();
                clearPatientFields();

            }
            else{
                PatientDatabase.addPatient(patientName, patientAddress, patientCity, patientState, patientZip, patientPhone, patientInsurance);
                setPatientTable();
                clearPatientFields();

            }
        }

        public void resetPatientFieldsButton(MouseEvent mouseEvent) {
        //clears out all the populated fields in the patient section.
            clearPatientFields();
        }

            //Appointment anchor pane controls
            public void allAppointmentsButton(MouseEvent mouseEvent) {
            //runs the method to populate the appointment table with all appointments from today forward.
                setAppointmentTable();
            }

            public void monthAppointmentButton(MouseEvent mouseEvent) {
                //runs the method to populate the appointment table with the next 30days of appointments from today's date.
                setAppointmentTable30days();
            }

            public void biWeeklyAppointmentsButton(MouseEvent mouseEvent) {
                //populates the appointment table with appointments coming from the next 14 days.
                setAppointmentTable14days();
            }

            public void weekAppointmentButton(MouseEvent mouseEvent) {
                //populates the appointment table with appointments coming from next 7 days.
                setAppointmentTable7days();
            }


            public void modifyAppointmentButton(MouseEvent mouseEvent) {
                //verifies that an appointment is selected and then casts the information to the add/modify appointment table as well as setting the radio button to selected.
                Appointment select = appointmentTable.getSelectionModel().getSelectedItem();
                if (select == null)
                    return;
                modifyAppointment = select;
                setTextFieldsToModifyAppointment(modifyAppointment);
            }

            public void deleteAppointmentButton(MouseEvent mouseEvent) {
                // verifys that an appointment is selected, if no appointment is selected the button function remains inactive.
                if (appointmentTable.getSelectionModel().getSelectedItem() != null){
                    selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
                }
                else{
                    return;
                }
                //displays an alert to inform the user that the selected appointment will be deleted if they continue.
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("You are about to delete the selected appointment.");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    AppointmentDatabase.deleteAppointmentRecord(selectedAppointment.getAppointmentID());
                    setAppointmentTable();
                }

            }

                //Add/modify appointment Controls
                public boolean appointmentSaveModifyButton(MouseEvent mouseEvent) throws IOException{
                DateTimeFormatter formatTimeStamp = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
                String patientid = patientIdAppointmnetTxtbox.getText();
                int patientID = Integer.parseInt(patientid);
                int appointmentDescriptionID = appointmentTypeComboBox.getSelectionModel().getSelectedIndex() + 1;
                String appointmentTime = appointmentTimeCombobox.getSelectionModel().getSelectedItem().toString();
                String selectedCalenderDate = appointmentDatePicker.getValue().toString();
                    //Takes the two strings above, formats them into the correct pattern sets the time to eastern time as all appointments are in eastern time, and it then outputs a string to use to send to the database in utc time of eastern time.
                        //Strips the everything after the : from the  appointmentTime combobox.
                    String hours = appointmentTime.split(":")[0];
                    int time = Integer.parseInt(hours);
                    String appointmentTimes = String.format("%s %02d:%s",selectedCalenderDate , time, "00");
                    LocalDateTime formatAppointment = LocalDateTime.parse(appointmentTimes, formatTimeStamp);
                    ZoneId est = ZoneId.of("US/Eastern");
                    ZonedDateTime zonetime = formatAppointment.atZone(est);
                    ZonedDateTime utc = zonetime.withZoneSameInstant(ZoneId.of("UTC"));
                    formatAppointment = utc.toLocalDateTime();
                    String formattedAppointment = formatAppointment.toString();
                int counselorID = appointmentCounselorComboBox.getSelectionModel().getSelectedIndex()+1;
                String aptNotes = appointmentNotesTextArea.getText();

                // checks to see if there is a current appointment in the database that is assigned for the patient at the selected time and date.
                if(AppointmentDatabase.checkPatientOverlap(patientID,formattedAppointment)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("This patient already has an appointment at this time");
                    alert.showAndWait();
                    return false;
                }

                // final check for overlaps before determining if the radio button is checked and either the record will be inserted or updated. This section checks to see if the selected counselor has an appointment in the database at the current time and date.
                if(AppointmentDatabase.checkCounselorOverlap(counselorID,formattedAppointment)){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("This counselor already has a booked appointment at this time");
                    alert.showAndWait();
                    return false;
                }

                //checks if the fields are set up to add or modify appointments based on if the radio button is selected.
                if (appointmentModifyRadiobutton.isSelected()){
                    String apptid = appointmentIDTextBox.getText();
                    int aID = Integer.parseInt(apptid);
                    AppointmentDatabase.modifyAppointmentRecord(patientID,appointmentDescriptionID,formattedAppointment,counselorID,aptNotes,aID);
                    clearAppointmentFields();
                    setAppointmentTable();
                    return true;
                    }
                else{
                    AppointmentDatabase.newAppointment(patientID, appointmentDescriptionID, formattedAppointment,counselorID,aptNotes);
                    clearAppointmentFields();
                    setAppointmentTable();
                    return true;
                    }
                }

                public void resetAppointmentFieldsButton(MouseEvent mouseEvent) {
                //method for the appointment reset button to clear out all fields in the appointment anchor pane.
                clearAppointmentFields();
                }

                    //Controls for Reports Anchor pane
                    public void reportOneButton(MouseEvent mouseEvent) {
                        try{
                            Statement statement = DatabaseInfo.getConn().createStatement();
                            String totalAppointmentType = "Select APTtype_APTtype_id as 'Appointment Type' , count(*) AS 'Total per Type' from appointment group by APTtype_APTtype_id";
                            ResultSet rs = statement.executeQuery(totalAppointmentType);
                            while (rs.next()){

                            }

                        }
                        catch (SQLException e) {
                            System.out.println("SQLException: " + e.getMessage());
                        }

                    //statement to get appointment totals and count by type. "Select APTtype_APTtype_id as "Appointment Type" , count(*) AS "Total per Type" from appointment group by APTtype_APTtype_id;"
                    }

                    public void reportTwoButton(MouseEvent mouseEvent) {
                    //statement to get appointment totals by counselor "Select cr_id as "Counselor ID", count(*) AS "Appointment Total" from appointment group by cr_id;"
                    }

                    public void reportThreeButton(MouseEvent mouseEvent) {
                    //Statement to get appointment totals by state. "select address.state stateName,count(1) from address inner join patient on address.address_id = patient.address_id inner join appointment on appointment.pt_id = patient.pt_id group by stateName;"
                    }

                    //Control for exiting the appointment System
                    public void exitAppointmentSystemButton(MouseEvent mouseEvent) throws IOException {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setHeaderText("You are about to close the appointment system.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if(result.get()==ButtonType.OK){
                            Parent mainScreenParent = FXMLLoader.load(getClass().getResource("AuthorizationScreen.fxml"));
                            Scene mainScreenScene = new Scene(mainScreenParent);
                            Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                            mainScreenStage.setScene(mainScreenScene);
                            mainScreenStage.show();
                        }
                        else {
                            return;
                        }
                    }


    public void setPatientTable(){
        //formats the data going into the patient table.
        patientTable.setItems(PatientDatabase.showAllPatients());
        patientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("patientPhone"));
        patientStateColumn.setCellValueFactory(new PropertyValueFactory<>("patientState"));
        patientInsuranceColumn.setCellValueFactory(new PropertyValueFactory<>("patientInsurance"));
    }

    public void setAppointmentTable(){
        //formats the data going into the appointment table.
        appointmentTable.setItems(AppointmentDatabase.showAllAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptpatientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        counserlorIDColumn.setCellValueFactory(new PropertyValueFactory<>("counselorID"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptDateTime"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    public void setAppointmentTable30days(){
        //formats the data going into the appointment table for 30 days.
        appointmentTable.setItems(AppointmentDatabase.showMonthAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptpatientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        counserlorIDColumn.setCellValueFactory(new PropertyValueFactory<>("counselorID"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptDateTime"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    public void setAppointmentTable14days(){
        //formats the data going into the appointment table for the next 14 days.
        appointmentTable.setItems(AppointmentDatabase.showBiweekAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptpatientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        counserlorIDColumn.setCellValueFactory(new PropertyValueFactory<>("counselorID"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptDateTime"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    public void setAppointmentTable7days(){
        //formats the data going into the appointment table for the next 7 days.
        appointmentTable.setItems(AppointmentDatabase.showWeekAppointments());
        aptIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        aptpatientIDColumn.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        counserlorIDColumn.setCellValueFactory(new PropertyValueFactory<>("counselorID"));
        appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("aptDateTime"));
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    public void clearPatientFields(){
        //Method for clearing out the text fields and resetting the combo box after saving or modifying a patient record.
        patientNameTxtbox.clear();
        patientAddressTxtbox.clear();
        patientCityTxtbox.clear();
        patientZipCodeTxtbox.clear();
        patientPhoneTextbox.clear();
        patientInsuranceTxtbox.clear();
        patientStateComboBox.getSelectionModel().clearSelection();
        modifyPatientRadioButton.setSelected(false);
    }

    public void setTextFieldsToModifyPatient(Patient patient){
        //takes the casted data from modifyPatient and populates the text boxes in the patient anchor pane.
        int id = patient.getAddressID();
        String ID = Integer.toString(id);
        addressIDTextbox.setText(ID);
        patientNameTxtbox.setText(patient.getPatientName());
        patientAddressTxtbox.setText(patient.getPatientAddress());
        patientCityTxtbox.setText(patient.getPatientCity());
        patientStateComboBox.getSelectionModel().select(patient.getPatientState());
        patientZipCodeTxtbox.setText(patient.getPatientZipCode());
        patientPhoneTextbox.setText(patient.getPatientPhone());
        patientInsuranceTxtbox.setText(patient.getPatientInsurance());
        modifyPatientRadioButton.setSelected(true);
    }

    public void setTextFieldsToModifyAppointment(Appointment appointment){
        //takes the casted data from the modifyPatient and populates the text boxes in the appointment anchor pane
        int id = appointment.getAppointmentID();
        String aptID = Integer.toString(id);
        appointmentIDTextBox.setText(aptID);
        int pid = appointment.getPatientID();
        String patID = Integer.toString(pid);
        patientIdAppointmnetTxtbox.setText(patID);
        appointmentTypeComboBox.getSelectionModel().select(appointment.getAptDescriptionID()-1);
        appointmentCounselorComboBox.getSelectionModel().select(appointment.getCounselorID()-1);
        appointmentModifyRadiobutton.setSelected(true);
        String dateTime = appointment.getAptDateTime();
        String date = dateTime.substring(0,10);
        appointmentDatePicker.setValue(LocalDate.parse(date));
        String time = dateTime.substring(11,16);
        LocalTime localTime = LocalTime.parse(time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("kk:mm");
        String easternTime = formatter.format(localTime);
        appointmentTimeCombobox.getSelectionModel().select(easternTime );

    }

    public void appointmentPatientID(Patient patient){
        int iD = patient.getPatientID();
        String ID = Integer.toString(iD);
        patientIdAppointmnetTxtbox.setText(ID);
    }

    public void clearAppointmentFields(){
        patientIdAppointmnetTxtbox.clear();
        appointmentTypeComboBox.getSelectionModel().clearSelection();
        appointmentTimeCombobox.getSelectionModel().clearSelection();
        appointmentDatePicker.getEditor().clear();
        appointmentCounselorComboBox.getSelectionModel().clearSelection();
        appointmentNotesTextArea.clear();
        appointmentModifyRadiobutton.setSelected(false);
        appointmentIDTextBox.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //populates the patient combobox with a selection for all 50 states.
        patientStateComboBox.setItems(states);

        //populates the appointment combo boxes
        appointmentTypeComboBox.setItems(appointmentsDescriptions);
        appointmentTimeCombobox.setItems(appointmentTimesDuringOfficeHours);
        appointmentCounselorComboBox.setItems(counselorList);

        //Sets the values for the patient and appointment tables.
        setPatientTable();
        setAppointmentTable();

        //Creating a list of holidays as outlined in the requirements for the next two years.
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add(LocalDate.of(2020,Month.NOVEMBER,11));
        holidays.add(LocalDate.of(2020,Month.NOVEMBER,26));
        holidays.add(LocalDate.of(2020,Month.NOVEMBER,27));
        holidays.add(LocalDate.of(2021,Month.JANUARY,18));
        holidays.add(LocalDate.of(2021,Month.MAY,31));
        holidays.add(LocalDate.of(2021,Month.NOVEMBER,11));
        holidays.add(LocalDate.of(2021,Month.NOVEMBER,25));
        holidays.add(LocalDate.of(2021,Month.NOVEMBER,26));
        holidays.add(LocalDate.of(2022,Month.JANUARY,17));
        holidays.add(LocalDate.of(2022,Month.MAY,30));
        holidays.add(LocalDate.of(2022,Month.NOVEMBER,11));
        holidays.add(LocalDate.of(2022,Month.NOVEMBER,24));
        holidays.add(LocalDate.of(2022,Month.NOVEMBER,25));

        //This lambda takes the datepicker from the Add/Modify Appointment anchorpane and disable dates that are prior to todays localdate, any weekend day, and any listed holiday in the list of holidays as outlined above.
        appointmentDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate day, boolean empty) {
                super.updateItem(day, empty);
                setDisable(
                        empty||day.isBefore(LocalDate.now()) || day.getDayOfWeek() == DayOfWeek.SATURDAY|| day.getDayOfWeek() == DayOfWeek.SUNDAY||holidays.contains(day));

                }
            }
        );
    }
}
