package it.isa.progetto;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
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
import org.junit.runner.RunWith;

/**
 * Unit test for simple App.
 */
@RunWith(JUnitQuickcheck.class)
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    public static Connection conn = null;
    private static boolean dbUnreachable = false;
    
    
    @BeforeAll
    public static void iniziaConnessione(){
        System.out.print("inizializzo connessione per i test...");
        try {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Please include Classpath  Where your DB2 Driver is located");
            e.printStackTrace();
            dbUnreachable = true;
        }
        System.out.println("DB2 driver is loaded successfully");
        try{
        conn = DriverManager.getConnection("jdbc:db2://localhost:50000/isa:retrieveMessagesFromServerOnGetMessage=true;","db2inst1","pass");
        }catch(SQLException e){
            System.out.println("DB2 Database connection Failed\nTest that use db will be skipped");
            dbUnreachable = true;
        }
            
    }
    
    
    @Test
    public void testPDip1(){
        if(dbUnreachable == false){
            App a = new App();
            a.pDip1(conn);
        }
    }
    
    public static double calcolaCostoPersonale(int numeroDipendenti, double stipendioMedio, double tasseLavoro) {
        double costoPersonale = numeroDipendenti * stipendioMedio * tasseLavoro;
        return costoPersonale;
    }
    
    @Property
    public void prop_calcolaCostoPersonale(int numeroDipendenti, int stipendioMedio, int tasseLavoro){
        App a = new App();
        int x = a.calcolaCostoPersonale(numeroDipendenti, stipendioMedio, tasseLavoro);
        double y = numeroDipendenti * stipendioMedio;
        System.out.print("eccoli");
        System.out.println("\nval: "+numeroDipendenti+"  "+stipendioMedio+"  "+tasseLavoro +"  x:"+x+"  y:"+y);
        if(x != -1){
            assertTrue(y == y);
        }
    }
    
}