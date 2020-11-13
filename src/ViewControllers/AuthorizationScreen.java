package ViewControllers;

import Models.Appointment;
import Models.AppointmentDatabase;
import Models.CounselorDatabase;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

public class AuthorizationScreen implements Initializable {
    public Button loginButton;
    public Label pinLabel;
    public Label passwordLabel;
    public TextField pinTxtBox;
    public TextField passwordTxtBox;
    public TextField usernameTxtbox;
    public Label usernameLabel;
    public Label defaultLanguageLabel;
    public Label mainOfficeTimeLabel;
    public Label localTimeLabel;
    public Label companyNameLabel;
    public Label localTimePrompt;
    public Label mainOfficePrompt;
    public String failedlogin;

    public boolean loginButton(MouseEvent mouseEvent) throws IOException {
        String username = usernameTxtbox.getText();
        String password = passwordTxtBox.getText();
        String pinNum = pinTxtBox.getText();
        if(username.isEmpty()||password.isEmpty()||pinNum.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("All fields must be filled out!");
            alert.showAndWait();
        }
        else {
            int pin = Integer.parseInt(pinNum);
            Boolean counselor = CounselorDatabase.checkCredentials(username, password, pin);
            if (counselor) {
                ObservableList <Appointment> next4 = AppointmentDatabase.next4HourAppointments();
                if (next4.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("You have no appointments in the next 4 hours.");
                    alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    String list = "";
                    for (Appointment ap:next4)
                          {
                              list = list + ap.getAptDateTime() + " " + ap.getNotes() + "\n";
                    }
                    alert.setHeaderText(list);
                    alert.showAndWait();
                }

                Parent mainScreenParent = FXMLLoader.load(getClass().getResource("PrimaryScreen.fxml"));
                Scene mainScreenScene = new Scene(mainScreenParent);
                Stage mainScreenStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                mainScreenStage.setScene(mainScreenScene);
                mainScreenStage.show();

                return true;

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(failedlogin);
                alert.showAndWait();
                return false;
            }
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        Locale locale = Locale.getDefault();
        rb = ResourceBundle.getBundle("LoginLanguages/language", locale);
        usernameLabel.setText(rb.getString("username"));
        passwordLabel.setText(rb.getString("password"));
        pinLabel.setText(rb.getString("pin"));
        loginButton.setText(rb.getString("login"));
        defaultLanguageLabel.setText(rb.getString("language"));
        companyNameLabel.setText(rb.getString("heading"));
        localTimePrompt.setText(rb.getString("localTime"));
        LocalDateTime lt = LocalDateTime.now();
        String localTime = lt.format(formatTime);
        mainOfficePrompt.setText(rb.getString("mainOfficeTime"));
        localTimeLabel.setText( localTime);
        LocalDateTime mOT  = LocalDateTime.now(ZoneId.of("US/Eastern"));
        String mainOffice = mOT.format(formatTime);
        mainOfficeTimeLabel.setText(mainOffice);
        failedlogin = rb.getString("alertText");
    }
}
