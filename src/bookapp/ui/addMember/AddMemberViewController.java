/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.addMember;

import bookapp.database.DBHelper;
import bookapp.database.DatabaseHandler;
import bookapp.ui.alert.AlertMaker;
import bookapp.ui.listMember.MemberListViewController;
import bookapp.ui.listMember.MemberListViewController.Member;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Magician
 */
public class AddMemberViewController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField mobileNumber;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private DatabaseHandler dbHandler;
    private Boolean isInEditMode = Boolean.FALSE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbHandler = DatabaseHandler.getInstance();
    }

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText().trim();
        String mId = id.getText().trim();
        String mMobileNumber = mobileNumber.getText().trim();
        String mEmail = email.getText().trim();

        if (mName.isEmpty() || mId.isEmpty() || mMobileNumber.isEmpty() || mEmail.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter all fields");
            alert.showAndWait();
            return;
        }

        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        if (DBHelper.isMemberExists(mId)) {
            AlertMaker.showErrorMessage("Duplicate member id", "Member with same Book ID exists.\nPlease use new ID");
            return;
        }

        Member member = new Member(mName, mId, mMobileNumber, mEmail);
        boolean result = DBHelper.insertNewMember(member);

        if (result) {
            AlertMaker.showSimpleAlert(null, "New Member added" + mName + " has been added");
            clearEntries();

        } else {
            AlertMaker.showErrorMessage(null, "Failed to add new member" + "Check all the entries and try again");
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private void clearEntries() {
        name.clear();
        id.clear();
        email.clear();
        mobileNumber.clear();
    }

    public void inflateUI(MemberListViewController.Member member) {
        name.setText(member.getName());
        id.setText(member.getId());
        email.setText(member.getEmail());
        mobileNumber.setText(member.getMobile());
        id.setEditable(false);//make id uneditable
        isInEditMode = Boolean.TRUE;
    }

    private void handleEditOperation() {
        MemberListViewController.Member member = new MemberListViewController.Member(name.getText(), id.getText(), mobileNumber.getText(), email.getText());
        if (dbHandler.updateMember(member)) {
            AlertMaker.showSimpleAlert("Success", "Member updated");
            
        } else {
            AlertMaker.showSimpleAlert("Failed", "can't update member");

        }
    }

}
