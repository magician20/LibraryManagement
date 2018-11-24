/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.settings;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Magician
 */
public class SettingsViewController implements Initializable {

    @FXML
    private JFXTextField nDaysWithoutFine;
    @FXML
    private JFXTextField finePerDay;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField serverName;
    @FXML
    private JFXTextField smtpPort;
    @FXML
    private JFXTextField emailAddress;
    @FXML
    private JFXPasswordField emailPassword;
    @FXML
    private JFXCheckBox sslCheckbox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          initDefaultValues();
    }    
     private Stage getStage() {
        return ((Stage) nDaysWithoutFine.getScene().getWindow());
    }
     private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        nDaysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
        finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
        username.setText(String.valueOf(preferences.getUsername()));
        
        String passHash = String.valueOf(preferences.getPassword());//get password encrpt
        password.setText(passHash);//passHash.substring(0, Math.min(passHash.length(), 20))
    }

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        int ndays = Integer.parseInt(nDaysWithoutFine.getText().trim());
        float fine = Float.parseFloat(finePerDay.getText().trim());
        String uname = username.getText().trim();
        String pass = password.getText();

        Preferences preferences = Preferences.getPreferences();
        preferences.setnDaysWithoutFine(ndays);
        preferences.setFinePerDay(fine);
        preferences.setUsername(uname);
        preferences.setPassword(pass);

        Preferences.writePreferenceToFile(preferences);
    }

    @FXML
    private void handleTestMailAction(ActionEvent event) {
    }

    @FXML
    private void saveMailServerConfuration(ActionEvent event) {
    }
    
}
