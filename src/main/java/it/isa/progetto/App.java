package it.isa.progetto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.time.LocalDate;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        System.out.println("hello guyz");
        
        Scanner myObj = new Scanner(System.in);
        LocalDate dateObj = LocalDate.now();
        System.out.println("Start "+ dateObj);
        int looper = 1;
        
        //String test = myObj.nextLine();
        //System.out.println("hai scritto"+test);
        
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Please include Classpath  Where your DB2 Driver is located");
            e.printStackTrace();
            return;
        }
        System.out.println("DB2 driver is loaded successfully");
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        boolean found = false;
        try {
            conn = DriverManager.getConnection("jdbc:db2://localhost:50000/isa:retrieveMessagesFromServerOnGetMessage=true;","db2inst1","pass");
                
            
        } catch (SQLException e) {
            System.out.println("DB2 Database connection Failed");
            e.printStackTrace();
            return;
        }
    }
    
}
