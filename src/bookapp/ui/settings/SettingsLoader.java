package bookapp.ui.settings;

import bookapp.database.DatabaseHandler;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsLoader extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/bookapp/ui/settings/settings.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
        stage.setTitle("Settings");

 /*       new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();*/
    }

    public static void main(String[] args) {
        launch(args);
    }

}
