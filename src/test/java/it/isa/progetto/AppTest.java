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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test for simple App.
 */
@RunWith(JUnitQuickcheck.class)
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    
    @Mock
    private App apm;
    @Mock
    private AppTest service;
    @Mock
    private Connection conn;
    @Mock
    private Statement stmt;
    @Mock
    private PreparedStatement pstmt;
    @Mock
    private ResultSet rs;

    //@Before
    public void initTest() {
        System.out.print("🐙init");
        MockitoAnnotations.initMocks(this);
        
    }
    
    
    //@Test
    public void testPDip1() throws Exception {
        String query = "SELECT d.NomeDipartimento, COUNT(p.IdProgetto) AS NumeroProgetti "
                + "FROM Dipartimento d " + "LEFT JOIN Progetto p ON d.NomeDipartimento = p.Afferenza "
                + "GROUP BY d.NomeDipartimento;";

        System.out.print("🐙in testpdip1");
        //when(conn.prepareStatement(query)).thenReturn(pstmt);
        //when(pstmt.executeQuery()).thenReturn(rs);
        //when(rs.next()).thenReturn(true, false); // Simulate one row in ResultSet
        //when(rs.getString("NomeDipartimento")).thenReturn("TestDipartimento");
        when(rs.getInt("NumeroProgetti")).thenReturn(3);

        System.out.print("🐙precall");
        // Call the function to be tested
        App a = new App();
        a.pDip1(conn);

        // Verify expected behavior
        verify(conn).prepareStatement(query);
        verify(pstmt).executeQuery();
        verify(rs).next();
        verify(rs).getString("NomeDipartimento");
        verify(rs).getInt("NumeroProgetti");
    }
    
    @Property
    public void prop_calcolaCostoPersonale(int numeroDipendenti, int stipendioMedio, int tasseLavoro){
        App a = new App();
        int x = a.calcolaCostoPersonale(numeroDipendenti, stipendioMedio, tasseLavoro);
        double y = numeroDipendenti * stipendioMedio;
        if(x != -1){
            assertTrue(y < x);
        }
    }

    @Property
    public void prop_calcolaTasse(int redditoAziendale, int aliquoteFiscali, int deduzioni, int detrazioniFiscali){
        App a = new App();
        int x = a.calcolaTasse(redditoAziendale,aliquoteFiscali,deduzioni,detrazioniFiscali);
        double y = redditoAziendale * aliquoteFiscali;
        if(x != -1){
            assertTrue(y <= x);
        }
    }
    
    @Property
    public void prop_budgetAnnuale(int venditePreviste, int costiProduzione, int profittoLordo, int speseOperative){
        App a = new App();
        int x = a.calcolaBudgetAnnuale(venditePreviste,costiProduzione,profittoLordo,speseOperative);
        double y = venditePreviste * profittoLordo;
        if(x != -1){
            assertTrue(y > x);
        }
    }

    @Property
    public void prop_calcolaStipendio(LocalDate dataAssunzione) {
        App a = new App();
        int x = a.calcolaStipendio(dataAssunzione);
        if(x != -1){
            assertTrue(x >= 1200);
        }
    }
}