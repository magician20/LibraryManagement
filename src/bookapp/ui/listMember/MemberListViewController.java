/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.listMember;

import bookapp.database.DatabaseHandler;
import bookapp.ui.addMember.AddMemberViewController;
import bookapp.ui.alert.AlertMaker;
import bookapp.ui.listBook.BookListViewController;
import bookapp.util.UtilitiesBookLibrary;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Magician
 */
public class MemberListViewController implements Initializable {

    ObservableList<Member> observableListMember = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> mobileCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    private DatabaseHandler dbHandler;
    @FXML
    private TableView<Member> tableViewMember;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        dbHandler = DatabaseHandler.getInstance();
        localData();
    }

    private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void localData() {
        String qu = "SELECT * FROM MEMBER";
        ResultSet rs = dbHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String name = rs.getString("name");
                String id = rs.getString("id");
                String mobile = rs.getString("mobile");
                String email = rs.getString("email");
                observableListMember.add(new Member(name, id, mobile, email));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookListViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableViewMember.setItems(observableListMember);//clear and add
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
          observableListMember.clear();
          localData();
    }

    @FXML
    private void handleMemberEdit(ActionEvent event) {
        String loc = "/bookapp/ui/addMember/addMemberView.fxml";
        String title = "Edit Book";

        //fetch the selected row
        Member selectedItem = tableViewMember.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertMaker.showErrorMessage("No member selected", "Please select member for edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
            Parent parent = loader.load();

            AddMemberViewController controller = (AddMemberViewController) loader.getController();
            controller.inflateUI(selectedItem);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
          
            UtilitiesBookLibrary.setStageIcon(stage);
            stage.show();
            //this callback the method handleRefresh() after the window close
            stage.setOnCloseRequest(e->{
                handleRefresh(new ActionEvent());
            });
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(BookListViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handleMemberDelete(ActionEvent event) {
        //fetch the selected row
        Member selectedItem = tableViewMember.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertMaker.showErrorMessage("No member selected", "Please select member for deletion.");
            return;
        }

        //confirm the opertion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Member");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        UtilitiesBookLibrary.setStageIcon(stage);
        alert.setHeaderText(null);
        alert.setContentText("Are u sure u want to Delete the member ?");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            boolean result = dbHandler.deleteMember(selectedItem);
            if (result == true) {
                AlertMaker.showSimpleAlert("Member delete", selectedItem.getName() + " was deleted successfully.");
                observableListMember.remove(selectedItem);
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedItem.getName() + " unable to delete.");

            }
        } else {
            AlertMaker.showSimpleAlert("Deletion Canclled", "Deletion process canclled.");

        }
    }

    // JavaFx use JavaBean with Properties
    public static class Member {

        private final SimpleStringProperty name;
        private final SimpleStringProperty id;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;

        public Member(String name, String id, String mobile, String email) {
            this.name = new SimpleStringProperty(name);
            this.id = new SimpleStringProperty(id);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);
        }

        public String getName() {
            return name.get();
        }

        public String getId() {
            return id.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }

    }

}
