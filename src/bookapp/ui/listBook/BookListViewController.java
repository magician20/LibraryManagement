package bookapp.ui.listBook;

import bookapp.database.DatabaseHandler;
import bookapp.ui.addBook.AddBookViewController;
import bookapp.ui.alert.AlertMaker;
import bookapp.util.UtilitiesBookLibrary;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class BookListViewController implements Initializable {

    ObservableList<Book> observableListBook = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> tableViewBook;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML
    private TableColumn<Book, Boolean> availabilityCol;

    private DatabaseHandler dbHandler;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // tableViewBook.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initCol();
        dbHandler = DatabaseHandler.getInstance();
        localData();

    }

    private void initCol() {

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availabilty"));
    }

    private void localData() {
        String qu = "SELECT * FROM BOOK";
        ResultSet rs = dbHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String title = rs.getString("title");
                String id = rs.getString("id");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                Boolean avail = rs.getBoolean("isAvail");
                observableListBook.add(new Book(title, id, author, publisher, avail));

            }
        } catch (SQLException ex) {
            Logger.getLogger(BookListViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableViewBook.setItems(observableListBook);//clear and add
    }

    /**
     *
     * Handle Table View Operation
     */
    @FXML
    private void handleRefresh(ActionEvent event) {
        observableListBook.clear();
        localData();
    }

    @FXML
    private void handleBookEditOption(ActionEvent event) {
          String loc="/bookapp/ui/addBook/addBookView.fxml";
          String title="Edit Book";
    
         //fetch the selected row
        Book selectedItem = tableViewBook.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertMaker.showErrorMessage("No book selected", "Please select book for Edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(loc));
            Parent parent = loader.load();
            
              AddBookViewController controller =(AddBookViewController) loader.getController();
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
    private void handleBookDeleteOption(ActionEvent event) {
        //fetch the selected row
        Book selectedItem = tableViewBook.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            AlertMaker.showErrorMessage("No book selected", "Please select book for deletion.");
            return;
        }
         if (DatabaseHandler.getInstance().isBookAlreadyIssued(selectedItem)) {
            AlertMaker.showErrorMessage("Cant be deleted", "This book is already issued and cant be deleted.");
            return;
        }
        //confirm the opertion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Deleting Book");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        UtilitiesBookLibrary.setStageIcon(stage);
        alert.setHeaderText(null);
        alert.setContentText("Are u sure u want to Delete the book ?");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            
            boolean result = dbHandler.deleteBook(selectedItem);
            if (result == true) {
                AlertMaker.showSimpleAlert("Book delete", selectedItem.getTitle() + " was deleted successfully.");
              observableListBook.remove(selectedItem);
            } else {
                AlertMaker.showSimpleAlert("Failed", selectedItem.getTitle() + " unable to delete.");

            }
        } else {
            AlertMaker.showSimpleAlert("Deletion Canclled", "Deletion process canclled.");

        }

    }

    // JavaFx use JavaBean with Properties
    public static class Book {

        private final SimpleStringProperty title;
        private final SimpleStringProperty id;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleStringProperty availabilty;

        public Book(String title, String id, String author, String pub, Boolean avail) {
            this.title = new SimpleStringProperty(title);
            this.id = new SimpleStringProperty(id);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(pub);
            if (avail) {
                this.availabilty = new SimpleStringProperty("Available");
            } else {
                this.availabilty = new SimpleStringProperty("Issued");
            }
        }

        public String getTitle() {
            return title.get();
        }

        public String getId() {
            return id.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public String getAvailabilty() {
            return availabilty.get();
        }

    }

}
