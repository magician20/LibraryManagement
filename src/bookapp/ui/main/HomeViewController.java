/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.main;

import bookapp.database.DatabaseHandler;
import bookapp.util.UtilitiesBookLibrary;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * FXML Controller class
 *
 * @author Magician
 */
public class HomeViewController implements Initializable {

    private final static Logger LOGGER = LogManager.getLogger(HomeViewController.class.getName());
    //static data
    private final static String NO_SUCH_BOOK_AVAILABLE = "No Such Book Available";
    private final static String NO_SUCH_MEMBER_AVAILABLE = "No Such Member Available";
    //flag 
    private Boolean isReadyForSubmission = false;

    private DatabaseHandler dbHandler;

    @FXML
    private JFXButton addMemberButton;
    @FXML
    private JFXButton viewMembersButton;
    @FXML
    private JFXButton addBookButton;
    @FXML
    private JFXButton viewBooksButton;
    @FXML
    private JFXButton settingButton;
    @FXML
    private JFXTextField bookID;
    @FXML
    private JFXButton renewButton;
    @FXML
    private JFXButton submissionButton;
    @FXML
    private JFXTextField bookIDInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookStatus;
    @FXML
    private JFXTextField memberIDInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberMobile;
    @FXML
    private JFXButton btnIssue1;

    @FXML
    private JFXListView<String> issueDataList;
    @FXML
    private AnchorPane rootPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // JFXDepthManager.setDepth(, 1); used for 2 HBox
        dbHandler = DatabaseHandler.getInstance();

    }

    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            UtilitiesBookLibrary.setStageIcon(stage);
            stage.show();
            // setStageIcon(stage);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(HomeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * *************************************************************************
     *                                                                         *
     * View Screen Navigation * *
     * ************************************************************************
     */
    @FXML
    private void loadAddMemberView(ActionEvent event) {

        loadWindow("/bookapp/ui/addMember/addMemberView.fxml", "Add Member");
    }

    @FXML
    private void loadMembersView(ActionEvent event) {
        loadWindow("/bookapp/ui/listMember/memberListView.fxml", "Member List");
    }

    @FXML
    private void loadAddBookView(ActionEvent event) {
        loadWindow("/bookapp/ui/addBook/addBookView.fxml", "Add Book");
    }

    @FXML
    private void loadBooksView(ActionEvent event) {
        loadWindow("/bookapp/ui/listBook/bookListView.fxml", "Book List");
    }

    @FXML
    private void loadSettingsView(ActionEvent event) {
        loadWindow("/bookapp/ui/settings/settingsView.fxml", "Settings");
    }

    /**
     * *************************************************************************
     *                                                                         *
     * Issue Operations *
     * ************************************************************************
     */
    @FXML
    private void loadBookInfo(ActionEvent event) {
        String bookId = bookIDInput.getText().trim();
        if (bookId.isEmpty()) {
            return;
        }
        clearTextCache("book");
        String qu = "SELECT * FROM BOOK WHERE ID='" + bookId + "'";
        ResultSet rs = dbHandler.execQuery(qu);
        Boolean flag = false;
        try {
            while (rs.next()) {
                String bName = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bStatus = rs.getBoolean("isAvail");

                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                String statusMessage = bStatus ? "Available" : "Not Available";
                bookStatus.setText(statusMessage);

                flag = true;
            }
            if (!flag) {
                bookName.setText(NO_SUCH_BOOK_AVAILABLE);
            }

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(HomeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        String memberId = memberIDInput.getText().trim();
        if (memberId.isEmpty()) {
            return;
        }
        clearTextCache(memberId);
        String qu = "SELECT * FROM MEMBER WHERE ID='" + memberId + "'";
        ResultSet rs = dbHandler.execQuery(qu);
        Boolean flag = false;
        try {
            while (rs.next()) {
                String mName = rs.getString("name");
                String mMobile = rs.getString("mobile");

                memberName.setText(mName);
                memberMobile.setText(mMobile);
                flag = true;
            }
            if (!flag) {
                memberName.setText(NO_SUCH_MEMBER_AVAILABLE);
            }

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(HomeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void clearTextCache(String textName) {
        if (textName.equals("book")) {
            bookIDInput.clear();
            bookName.setText("");
            bookAuthor.setText("");
            bookStatus.setText("");
        } else {
            memberIDInput.clear();
            memberIDInput.clear();
            memberName.setText("");
            memberMobile.setText("");

        }
    }

    @FXML
    private void loadIssueOperation(ActionEvent event) {
        String memberId = memberIDInput.getText().trim();
        String bookId = bookIDInput.getText().trim();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("are u sure u want to issue the book " + bookName.getText()
                + "\n to " + memberName.getText() + " ?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String str1 = "INSERT INTO ISSUE (bookID,memberID) VALUES ("
                    + "'" + bookId + "',"
                    + "'" + memberId + "')";
            String str2 = "UPDATE BOOK SET isAvail=false WHERE id='" + bookId + "'";
            System.out.println(str1 + "and" + str2);

            if (dbHandler.execAction(str1) && dbHandler.execAction(str2)) {
                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("Success");
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("Book Issue Completed.");
                alertInfo.showAndWait();
            } else {
                Alert alertErr1 = new Alert(Alert.AlertType.ERROR);
                alertErr1.setTitle("Failed");
                alertErr1.setHeaderText(null);
                alertErr1.setContentText("Book Issue Failed.");
                alertErr1.showAndWait();

            }
        } else {
            Alert alertErr2 = new Alert(Alert.AlertType.INFORMATION);
            alertErr2.setTitle("Cancelled");
            alertErr2.setHeaderText(null);
            alertErr2.setContentText("Issue Operation Cancelled.");
            alertErr2.showAndWait();
        }

    }

    /**
     *
     * Tab2 operations
     *
     */
    @FXML
    private void loadBookInfo2(ActionEvent event) {
        ObservableList<String> issueData = FXCollections.observableArrayList();
        isReadyForSubmission = false;

        String bookId = bookID.getText().trim();
        //query ISSUE taable
        String qu = "SELECT * FROM ISSUE WHERE bookId='" + bookId + "'";//not work bcs no table exist
        ResultSet res = dbHandler.execQuery(qu);

        try {
            while (res.next()) {
                String mBookID = bookId;
                String mMemberID = res.getString("memberID");
                Timestamp timestamp = res.getTimestamp("issueTime");
                int renewCount = res.getInt("renew_count");

                issueData.add("Issue Date and Time :" + UtilitiesBookLibrary.formatDateTimeString(timestamp));
                issueData.add("RenewCount :" + renewCount);

                //query BOOK taable
                issueData.add("Book Information: ");
                qu = "SELECT * FROM BOOK WHERE id='" + mBookID + "'";
                ResultSet bResult = dbHandler.execQuery(qu);
                while (bResult.next()) {
                    issueData.add("Book Name: " + bResult.getString("title"));
                    issueData.add("Book Name: " + bResult.getString("id"));
                    issueData.add("Book Author: " + bResult.getString("author"));
                    issueData.add("Book Publisher: " + bResult.getString("publisher"));

                }
                //query MEMBER table
                issueData.add("Member Information: ");
                qu = "SELECT * FROM MEMBER WHERE id='" + mMemberID + "'";
                ResultSet mResult = dbHandler.execQuery(qu);
                while (mResult.next()) {
                    issueData.add("Member Name: " + mResult.getString("name"));
                    issueData.add("Member Mobile: " + mResult.getString("mobile"));
                    issueData.add("Member Email: " + mResult.getString("email"));
                }
                isReadyForSubmission = true;
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(HomeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        issueDataList.setItems(issueData);
    }

    @FXML
    private void loadSubmissionOp(ActionEvent event) {
        if (!isReadyForSubmission) {
            Alert alertWar = new Alert(Alert.AlertType.WARNING);
            alertWar.setTitle("Failed");
            alertWar.setHeaderText(null);
            alertWar.setContentText("Please select book for submission.");
            alertWar.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirm Submission Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are u sure u want to return the book ?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String bookId = bookID.getText().trim();
            String ac1 = "DELETE FROM ISSUE WHERE bookID='" + bookId + "'";
            String ac2 = "UPDATE BOOK SET isAvail=true WHERE id='" + bookId + "'";
            if (dbHandler.execAction(ac1) && dbHandler.execAction(ac1)) {
                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("Success");
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("Book has been submitted.");
                alertInfo.showAndWait();
            } else {
                Alert alertErr = new Alert(Alert.AlertType.ERROR);
                alertErr.setTitle("Failed");
                alertErr.setHeaderText(null);
                alertErr.setContentText("Book submmission has been Failed.");
                alertErr.showAndWait();
            }
        } else {
            Alert alertErr2 = new Alert(Alert.AlertType.INFORMATION);
            alertErr2.setTitle("Cancelled");
            alertErr2.setHeaderText(null);
            alertErr2.setContentText("Submission Operation Cancelled.");
            alertErr2.showAndWait();
        }

    }

    @FXML
    private void loadRenewOp(ActionEvent event) {
        if (!isReadyForSubmission) {
            Alert alertWar = new Alert(Alert.AlertType.WARNING);
            alertWar.setTitle("Failed");
            alertWar.setHeaderText(null);
            alertWar.setContentText("Please select book for renew.");
            alertWar.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("confirm Renew Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are u sure u want to renew the book ?");

        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String ac = "UPDATE ISSUE SET issueTime=CURRENT_TIMESTAMP ,renew_count=renew_count +1 WHERE bookID='" + bookID.getText() + "'";
            if (dbHandler.execAction(ac)) {
                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("Success");
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("Book has been Renew.");
                alertInfo.showAndWait();
            } else {
                Alert alertErr = new Alert(Alert.AlertType.ERROR);
                alertErr.setTitle("Failed");
                alertErr.setHeaderText(null);
                alertErr.setContentText("Book renew has been Failed.");
                alertErr.showAndWait();
            }
        } else {
            Alert alertErr2 = new Alert(Alert.AlertType.INFORMATION);
            alertErr2.setTitle("Cancelled");
            alertErr2.setHeaderText(null);
            alertErr2.setContentText("Submission Operation Cancelled.");
            alertErr2.showAndWait();
        }

    }

    private void disableEnableControls(Boolean enableFlag) {
        if (enableFlag) {
            renewButton.setDisable(false);
            submissionButton.setDisable(false);
        } else {
            renewButton.setDisable(true);
            submissionButton.setDisable(true);
        }
    }

    /**
     * ***************************************************************************
     */
    @FXML
    private void handleIssueButtonKeyPress(KeyEvent event) {

    }

    /**
     * Handle Menu Action
     *
     */
    @FXML
    private void handleMenuSettings(ActionEvent event) {
        loadWindow("/bookapp/ui/settings/settingsView.fxml", "Settings");
    }

    @FXML
    private void handleMenuClose(ActionEvent event) {
        ((Stage) rootPane.getScene().getWindow()).close();

    }

    @FXML
    private void handleMenuAddBook(ActionEvent event) {
        loadWindow("/bookapp/ui/addBook/addBookView.fxml", "Add Book");
    }

    @FXML
    private void handleMenuAddMember(ActionEvent event) {
        loadWindow("/bookapp/ui/addMember/addMemberView.fxml", "Add Member");
    }

    @FXML
    private void handleMenuViewBook(ActionEvent event) {
        loadWindow("/bookapp/ui/listBook/bookListView.fxml", "Book List");
    }

    @FXML
    private void handleMenuViewMemberList(ActionEvent event) {
        loadWindow("/bookapp/ui/listMember/memberListView.fxml", "Member List");
    }

    @FXML
    private void handleMenuFullScreen(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    @FXML
    private void handleIssuedList(ActionEvent event) {

    }

    @FXML
    private void handleMenuOverdueNotification(ActionEvent event) {
    }

}
