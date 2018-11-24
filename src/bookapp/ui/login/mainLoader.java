/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.login;

import bookapp.database.DatabaseHandler;
import bookapp.exception.ExceptionUtil;
import bookapp.util.UtilitiesBookLibrary;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Magician
 */
public class mainLoader extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/bookapp/ui/login/loginView.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Library Login");

        UtilitiesBookLibrary.setStageIcon(stage);
        stage.show();
        //this should run on it's own thred
        new Thread(() -> {
            ExceptionUtil.init();
            DatabaseHandler.getInstance();
        }).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
