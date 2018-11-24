/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.issuedList;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author Magician
 */
public class IssuedListViewController implements Initializable {

    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private TableView<?> tableView;
    @FXML
    private TableColumn<?, ?> idCol;
    @FXML
    private TableColumn<?, ?> bookIDCol;
    @FXML
    private TableColumn<?, ?> bookNameCol;
    @FXML
    private TableColumn<?, ?> holderNameCol;
    @FXML
    private TableColumn<?, ?> issueCol;
    @FXML
    private TableColumn<?, ?> daysCol;
    @FXML
    private TableColumn<?, ?> fineCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleReturn(ActionEvent event) {
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
    }

    @FXML
    private void exportAsPDF(ActionEvent event) {
    }

    @FXML
    private void closeStage(ActionEvent event) {
    }
    
}
