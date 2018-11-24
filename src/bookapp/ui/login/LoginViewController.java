/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.login;

import bookapp.database.DatabaseHandler;
import bookapp.exception.ExceptionUtil;
import bookapp.ui.settings.Preferences;
import bookapp.util.UtilitiesBookLibrary;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Magician
 */
public class LoginViewController implements Initializable {

    private final static Logger LOGGER = LogManager.getLogger(LoginViewController.class.getName());

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    Preferences preference;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preferences.getPreferences();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String uname = username.getText().trim();
        String pword = DigestUtils.shaHex(password.getText().trim());

        if (uname.equals(preference.getUsername()) && pword.equals(preference.getPassword())) {
            closeStage();
            loadMain();
            LOGGER.log(Level.INFO, "User successfully logged in {}", uname);
        } else {
            System.out.println("credentials");
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/bookapp/ui/main/homeView.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            stage.setScene(new Scene(parent));
            stage.setTitle("Library");

            UtilitiesBookLibrary.setStageIcon(stage);
            stage.show();
            //this should run on it's own thred
            new Thread(() -> {
                ExceptionUtil.init();
                DatabaseHandler.getInstance();
            }).start();

        } catch (IOException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }

}
