/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.ui.addBook;

import bookapp.data.model.Book;
import bookapp.database.DBHelper;
import bookapp.database.DatabaseHandler;
import bookapp.ui.alert.AlertMaker;
import bookapp.ui.listBook.BookListViewController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Magician
 */
public class AddBookViewController implements Initializable {

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField author;
    @FXML
    private JFXTextField publisher;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    DatabaseHandler dbHandler;
    @FXML
    private AnchorPane root;

    private Boolean isInEditMode = Boolean.FALSE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dbHandler = DatabaseHandler.getInstance();
        checkData();
    }

    @FXML
    private void addBook(ActionEvent event) {
        String bookID = id.getText().trim();
        String bookTitle = title.getText().trim();
        String bookAuthor = author.getText().trim();
        String bookPublisher = publisher.getText().trim();
        if (bookID.isEmpty() || bookAuthor.isEmpty() || bookTitle.isEmpty() || bookPublisher.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter all fields");
            alert.showAndWait();
            return;
        }
        
        if (isInEditMode) {
            handleEditOperation();
            isInEditMode = Boolean.FALSE;
            return;
        }
        
        if (DBHelper.isBookExists(bookID)) {
            AlertMaker.showErrorMessage("Duplicate book id", "Book with same Book ID exists.\nPlease use new ID");
            return;
        }
        
        Book book = new Book(bookID, bookTitle, bookAuthor, bookPublisher, Boolean.TRUE);
        boolean result = DBHelper.insertNewBook(book);

        if (result) {
            AlertMaker.showSimpleAlert(null, "New book added" + bookTitle + " has been added");
            clearEntries();

        } else {
            AlertMaker.showErrorMessage(null, "Failed to add new book" + "Check all the entries and try again");
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private void clearEntries() {
        title.clear();
        id.clear();
        author.clear();
        publisher.clear();
    }

    private void checkData() {
        String qu = "SELECT title FROM BOOK";
        ResultSet rs = dbHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String titlex = rs.getString("title");
                System.out.println(titlex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddBookViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void inflateUI(BookListViewController.Book book) {
        title.setText(book.getTitle());
        id.setText(book.getId());
        author.setText(book.getAuthor());
        publisher.setText(book.getPublisher());
        id.setEditable(false);//make id uneditable
        isInEditMode = Boolean.TRUE;
    }

    private void handleEditOperation() {
        BookListViewController.Book book = new BookListViewController.Book(title.getText(), id.getText(), author.getText(), publisher.getText(), true);
        if (dbHandler.updateBook(book)) {
            AlertMaker.showSimpleAlert("Success", "Book updated");
        } else {
            AlertMaker.showSimpleAlert("Failed", "can't update book");

        }
    }

}
