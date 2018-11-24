/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookapp.database;

import bookapp.ui.listBook.BookListViewController;
import bookapp.ui.listBook.BookListViewController.Book;
import bookapp.ui.listMember.MemberListViewController;
import bookapp.ui.listMember.MemberListViewController.Member;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Magician
 */
public final class DatabaseHandler {

    private final static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() {
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupIssueTable();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }
    
     public Connection getConnection() {
        return conn;
    }

    private void createConnection() { //error happen here
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    private void setupBookTable() {
        String Table_Name = "BOOK";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, Table_Name.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + Table_Name + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + Table_Name + "("
                        + "id varchar(200) primary key,\n"
                        + "title varchar(200),\n"
                        + "author varchar(200),\n"
                        + "publisher varchar(100),\n"
                        + "isAvail boolean default true"
                        + ")");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupMemberTable() {
        String Table_Name = "MEMBER";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, Table_Name.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + Table_Name + " already exists.");
            } else {
                stmt.execute("CREATE TABLE " + Table_Name + "("
                        + "id varchar(200) primary key,\n"
                        + "name varchar(200),\n"
                        + "mobile varchar(13),\n"
                        + "email varchar(100)"
                        + ")");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setupIssueTable() {
        String Table_Name = "ISSUE";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, Table_Name.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + Table_Name + " already exists.");

            } else {
                stmt.execute("CREATE TABLE " + Table_Name + "("
                        + "bookID varchar(200) primary key,\n"
                        + "memberID varchar(200),\n"
                        + "issueTime timestamp DEFAULT CURRENT_TIMESTAMP,\n"
                        + "renew_count integer DEFAULT 0,\n"
                        + "FOREIGN KEY (bookID) REFERENCES BOOK(id),\n"
                        + "FOREIGN KEY (memberID) REFERENCES MEMBER(id)"
                        + " )");
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }

    }

    /**
     * Delete Operations
     */
    public boolean deleteBook(Book book) {
        try {
            String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, book.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {//res == 1 OR RES >0
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", ex);
        }
        return false;
    }

    public boolean deleteMember(Member member) {
        try {
            String deleteStatement = "DELETE FROM MEMBER WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
            stmt.setString(1, member.getId());
            int res = stmt.executeUpdate();
            if (res == 1) {//res == 1 OR RES >0
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", ex);
        }
        return false;
    } 
    
     public boolean isBookAlreadyIssued(Book book) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM ISSUE WHERE bookid=?";
            PreparedStatement stmt = conn.prepareStatement(checkstmt);
            stmt.setString(1, book.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println("test:"+count);//test
                return (count > 0);
            }
        }
        catch (SQLException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", ex);
        }
        return false;
    }

        public boolean updateBook(BookListViewController.Book book) {
        try {
            String update = "UPDATE BOOK SET TITLE=?, AUTHOR=?, PUBLISHER=? WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getPublisher());
            stmt.setString(4, book.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        }
        catch (SQLException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", ex);
        }
        return false;
    }

    public boolean updateMember(MemberListViewController.Member member) {
        try {
            String update = "UPDATE MEMBER SET NAME=?, EMAIL=?, MOBILE=? WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(update);
            stmt.setString(1, member.getName());
            stmt.setString(2, member.getEmail());
            stmt.setString(3, member.getMobile());
            stmt.setString(4, member.getId());
            int res = stmt.executeUpdate();
            return (res > 0);
        }
        catch (SQLException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", ex);
        }
        return false;
    }
    public boolean isMemberHasAnyBooks(MemberListViewController.Member member) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM ISSUE WHERE memberID=?";
            PreparedStatement stmt = conn.prepareStatement(checkstmt);
            stmt.setString(1, member.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        }
        catch (SQLException ex) {
            LOGGER.log(org.apache.logging.log4j.Level.ERROR, "{}", ex);
        }
        return false;
    }

}
