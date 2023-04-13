package it.isa.progetto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    public static Connection conn = null;
    
    
    @BeforeAll
    @Ignore
    public static void iniziaConnessione(){
        System.out.print("inizializzo connessione per i test...");
        System.out.print("QUESTO TEST DOVREBBE ESSERE IGNORATo");
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Please include Classpath  Where your DB2 Driver is located");
            e.printStackTrace();
            return;
        }
        System.out.println("DB2 driver is loaded successfully");
        try{
        conn = DriverManager.getConnection("jdbc:db2://localhost:50000/isa:retrieveMessagesFromServerOnGetMessage=true;","db2inst1","pass");
        }catch(SQLException e){
            System.out.println("DB2 Database connection Failed");
            e.printStackTrace();
            return;
        }
            
    }
    
    
    @Test
    @Ignore
    public void testPDip1(){
        App a = new App();
        a.pDip1(conn);
    }
    
    
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
}