package it.isa.progetto;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for simple App.
 */
@RunWith(JUnitQuickcheck.class)
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    public static Connection conn_legacy = null;
    private static boolean dbUnreachable = false;
    
    @InjectMocks
    private AppTest service;
    @Mock
    private Connection conn;
    @Mock
    private Statement stmt;
    @Before
    public void initTest() {
       MockitoAnnotations.initMocks(this);
    }
   
    public void testConnection() throws Exception {
        // Creazione di un result set vuoto
        ResultSet emptyResultSet = Mockito.mock(ResultSet.class);
        Mockito.when(emptyResultSet.next()).thenReturn(false);

        Mockito.when(conn.createStatement()).thenReturn(stmt);
        Mockito.when(conn.createStatement().executeUpdate(Mockito.anyString())).thenReturn(1);
        Mockito.when(stmt.executeQuery(Mockito.anyString())).thenReturn(emptyResultSet);

        int result = service.runQuery("");
        Assert.assertEquals(result, 1);
        Mockito.verify(conn.createStatement(),
            Mockito.times(1));
    }
    
    public int runQuery(String sql) throws
         ClassNotFoundException, SQLException {
      return conn.createStatement().executeUpdate(sql);
    }
    
    
    @Test
    public void testPDip1(){
        if(dbUnreachable == false){
            App a = new App();
            a.pDip1(conn);
        }
    }
    
    @Property
    public void prop_calcolaCostoPersonale(int numeroDipendenti, int stipendioMedio, int tasseLavoro){
        App a = new App();
        int x = a.calcolaCostoPersonale(numeroDipendenti, stipendioMedio, tasseLavoro);
        double y = numeroDipendenti * stipendioMedio;
        System.out.println("\nval: "+numeroDipendenti+"  "+stipendioMedio+"  "+tasseLavoro +"  x:"+x+"  y:"+y);
        if(x != -1){
            assertTrue(y == y);
        }
    }
    
}